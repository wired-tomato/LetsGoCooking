@file:UseSerializers(Vector2dSerializer::class)

package net.wiredtomato.letsgocooking.api.input

import com.google.gson.GsonBuilder
import com.mojang.serialization.Codec
import com.mojang.serialization.JsonOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import kotlinx.serialization.UseSerializers
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.data.serialization.Vector2dCodec
import net.wiredtomato.letsgocooking.api.data.serialization.Vector2dSerializer
import org.joml.Vector2d
import java.nio.file.Files
import java.util.function.Supplier

//https://github.com/Oponn-1/Unity-Gesture-Recognizer ported for MC
class MouseGestureRecognizer(
    private val input: Input,
    private val gestures: MutableList<MouseGesture> = mutableListOf(),
    private val pointsPerGesture: Int = 30,
    private val samplingRate: Double = 0.4,
    private val limitSamples: Boolean = false,
    private val maxPointsAllowed: Int = 100,
    private val standardRatio: Double = 100.0,
) {
    private var recording = false
    private var gestureStarted = false
    private var gestureComplete = false
    private var inputReady = false
    private var currentGesture = MouseGestureBuilder()
    private var tempTime = 0f

    private var onEndGesture: MutableList<(recognizer: MouseGestureRecognizer) -> Unit> = mutableListOf()

    private val gson = GsonBuilder().setPrettyPrinting().create()

    fun update() {
        tempTime += input.delta()
        if (input.mouseDown() && inputReady) {
            if (!gestureStarted) startGesture()

            if (!gestureComplete && tempTime > samplingRate) {
                tempTime = 0f
                continueGesture()
            }
            if (gestureComplete) {
                endGesture()
            }
        } else if (gestureStarted) {
            endGesture()
        } else inputReady = true
    }

    private fun startGesture() {
        gestureStarted = true
        currentGesture.withStartingPoint(input.mousePos())
        gestureComplete = false
    }

    private fun continueGesture() {
        currentGesture.point(input.mousePos())
        if (limitSamples && currentGesture.points().size >= maxPointsAllowed) {
            endGesture()
        }
    }

    private fun endGesture() {
        if (inputReady) inputReady = false

        gestureStarted = false
        gestureComplete = true

        if (recording) {
            gestures += currentGesture.build(standardRatio, pointsPerGesture)
            gestures.forEach { mouseGesture ->
                val filePath = FabricLoader.getInstance().gameDir.resolve("gestures/${mouseGesture.name}.json")
                Files.createDirectories(filePath.parent)

                val result = MouseGesture.CODEC.encodeStart(JsonOps.INSTANCE, mouseGesture)
                result.ifSuccess { element ->
                    val json = gson.toJson(element)
                    Files.writeString(filePath, json)
                }
            }
        }

        onEndGesture.forEach { it(this) }

        reset()
    }

    fun findMatch(): MouseGesture.Instance? {
        return gestures.minByOrNull { averageDifference(currentGesture.build(standardRatio, pointsPerGesture), it) }
            ?.let { currentGesture.instanced(it) }
    }

    fun findMatchWithinDifference(differenceRange: ClosedFloatingPointRange<Double> = 0.0..150.0): Pair<MouseGesture.Instance, Double>? {
        val thisGesture = currentGesture.build(standardRatio, pointsPerGesture)
        val map = gestures.associateWith { averageDifference(thisGesture, it) }
        val minimumDifference = map.minByOrNull { (_, difference) -> difference } ?: return null

        return if (minimumDifference.value in differenceRange) {
            currentGesture.instanced(minimumDifference.key) to minimumDifference.value
        } else null
    }

    private fun averageDifference(thisGesture: MouseGesture, template: MouseGesture): Double {
        val size = thisGesture.points.size
        if (size != template.points.size) return Double.MAX_VALUE


        var total = 0.0
        (0..<size).forEach { i ->
            val thisPoint = thisGesture.points[i]
            val templatePoint = template.points[i]
            total += thisPoint.distance(templatePoint)
        }

        return total / size
    }

    private fun reset() {
        gestureStarted = false
        gestureComplete = false
        currentGesture = MouseGestureBuilder()
    }

    fun onEndGesture(action: (recognizer: MouseGestureRecognizer) -> Unit) = apply {
        onEndGesture.add(action)
    }

    fun startRecording() = apply {
        this.recording = true
    }

    fun stopRecording() = apply {
        this.recording = false
    }

    fun gestures() = gestures.toList()

    fun withGesture(gesture: MouseGesture) = apply {
        this.gestures.add(gesture)
    }

    fun withGestures(gestures: Collection<MouseGesture>) = apply {
        this.gestures.addAll(gestures)
    }

    fun clearGestures() = apply {
        this.gestures.clear()
    }

    fun currentGesture(): MouseGesture {
        return currentGesture.build()
    }

    fun rawPoints(): List<Vector2d> = currentGesture.points().map { Vector2d(it).add(currentGesture.startingPoint()) }
    fun expectedPath(): MouseGesture? = findMatch()
}

class MouseGestureBuilder(
    private val points: MutableList<Vector2d> = mutableListOf(),
    private var name: Identifier = Identifier.ofDefault("no_gesture"),
    private var max: Vector2d = Vector2d(),
    private var min: Vector2d = Vector2d()
) {
    private var startingPoint = Vector2d()

    fun startingPoint() = Vector2d(startingPoint)
    fun withStartingPoint(startingPoint: Vector2d): MouseGestureBuilder = apply {
        this.startingPoint = Vector2d(startingPoint)
        points.add(Vector2d())
    }

    fun points() = points.toList()
    fun point(point: Vector2d): MouseGestureBuilder = apply {
        if (points.isEmpty()) {
            withStartingPoint(point)
            return@apply
        }

        val adjustedPoint = Vector2d(point).sub(startingPoint)
        rawPoint(adjustedPoint)
    }

    fun rawPoint(point: Vector2d): MouseGestureBuilder = apply {
        if (point.x > max.x) max.x = point.x
        if (point.x < min.x) min.x = point.x
        if (point.y > max.y) max.y = point.y
        if (point.y < min.y) min.y = point.y

        points.add(point)
    }

    fun name() = name
    fun name(name: Identifier): MouseGestureBuilder = apply {
        this.name = name
    }

    fun max() = Vector2d(max)
    fun min() = Vector2d(min)

    private fun rescale(ratio: Double) {
        val range = max().sub(min())
        val scale = if (range.x >= range.y) {
            ratio / range.x
        } else ratio / range.y

        if (scale != -1.0) {
            points.forEach { point ->
                point.mul(scale)
            }
        }
    }

    private fun mapPoints(ratio: Double, requiredPoints: Int): MouseGesture {
        rescale(ratio)

        if (points.size <= 1) {
            return MouseGesture(listOf(), name, Vector2d(), Vector2d())
        }

        val reducedPoints = mutableListOf<Vector2d>()
        reducedPoints.add(0, points[0])
        var currentIdx = 1
        val totalDistance = totalDistance()
        var coveredDistance = 0.0
        var thisDistance: Double
        val idealInterval = totalDistance / requiredPoints

        (0..<points.size-1).forEach { i ->
            val next = points[i + 1]
            thisDistance = points[i].distance(next)
            var pastIdeal = (coveredDistance + thisDistance) >= idealInterval
            if (pastIdeal) {
                var ref = points[i]
                while (pastIdeal && currentIdx < requiredPoints) {
                    val needed = ((idealInterval - coveredDistance) / thisDistance).coerceIn(0.0..1.0)
                    val new = Vector2d(1 - needed).mul(ref).add(Vector2d(next).mul(needed))
                    reducedPoints.add(new)
                    ref = reducedPoints.last()
                    currentIdx++
                    thisDistance = (coveredDistance + thisDistance) - idealInterval
                    coveredDistance = 0.0
                    pastIdeal = (coveredDistance + thisDistance) >= idealInterval
                }
                coveredDistance = thisDistance
            } else coveredDistance += thisDistance
        }

        return MouseGesture(reducedPoints.toList(), name, max(), min())
    }

    private fun totalDistance(): Double {
        var totalDistance = 0.0

        points.forEachIndexed { i, point ->
            if (i >= points.size - 1) return@forEachIndexed

            totalDistance += point.distance(points[i + 1])
        }

        return totalDistance
    }

    fun instanced(gesture: MouseGesture) = gesture.instance(startingPoint)

    fun buildInstance(ratio: Double = 100.0, requiredPoints: Int): MouseGesture.Instance =
        build(ratio, requiredPoints).instance(startingPoint)

    fun build(ratio: Double = 100.0, requiredPoints: Int = 30): MouseGesture {
        return mapPoints(ratio, requiredPoints)
    }

    fun buildRaw(ratio: Double = 100.0): MouseGesture {
        rescale(ratio)
        return MouseGesture(points.toList(), name, max, min)
    }
}

open class MouseGesture(
    val points: List<Vector2d>,
    val name: Identifier,
    val max: Vector2d,
    val min: Vector2d
) {
    fun instance(startPos: Vector2d) = Instance(this, startPos)

    data class Instance(
        val gesture: MouseGesture,
        val startPos: Vector2d
    ) : MouseGesture(gesture.points, gesture.name, gesture.max, gesture.min) {
        val endPos = gesture.points.last().add(startPos, Vector2d())

        companion object {
            val PACKET_CODEC: PacketCodec<PacketByteBuf, Instance> = PacketCodec.create(::packetEncode, ::packetDecode)

            private fun packetEncode(buf: PacketByteBuf, instance: Instance) {
                buf.writeCollection(instance.points) { subBuf, vec ->
                    subBuf.writeVec2d(vec)
                }

                buf.writeIdentifier(instance.name)
                buf.writeVec2d(instance.max)
                buf.writeVec2d(instance.min)
                buf.writeVec2d(instance.startPos)
            }

            private fun packetDecode(buf: PacketByteBuf): Instance {
                val points = buf.readList { subBuf ->
                    subBuf.readVec2d()
                }

                val name = buf.readIdentifier()
                val max = buf.readVec2d()
                val min = buf.readVec2d()
                val startPos = buf.readVec2d()

                return Instance(MouseGesture(points, name, max, min), startPos)
            }

            private fun ByteBuf.writeVec2d(vec: Vector2d) {
                writeDouble(vec.x)
                writeDouble(vec.y)
            }

            private fun ByteBuf.readVec2d() = Vector2d(readDouble(), readDouble())
        }
    }

    companion object {
        val CODEC: Codec<MouseGesture> = RecordCodecBuilder.create {
            it.group(
                Vector2dCodec.listOf().fieldOf("points").forGetter(MouseGesture::points),
                Identifier.CODEC.fieldOf("name").forGetter(MouseGesture::name),
                Vector2dCodec.fieldOf("max").forGetter(MouseGesture::max),
                Vector2dCodec.fieldOf("min").forGetter(MouseGesture::min)
            ).apply(it, ::MouseGesture)
        }
    }
}

interface Input {
    fun mousePos(): Vector2d
    fun mouseDown(): Boolean
    fun delta(): Float
}

fun Input(mousePos: Supplier<Vector2d>, mouseDown: Supplier<Boolean>, delta: Supplier<Float>): Input = object : Input {
    override fun mousePos(): Vector2d = mousePos.get()
    override fun mouseDown(): Boolean = mouseDown.get()
    override fun delta(): Float = delta.get()
}
