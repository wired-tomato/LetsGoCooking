package net.wiredtomato.letsgocooking.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.registry.HolderLookup
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.data.gen.MouseGestureProvider
import net.wiredtomato.letsgocooking.api.input.MouseGesture
import net.wiredtomato.letsgocooking.api.input.MouseGestureBuilder
import org.joml.Vector2d
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import kotlin.math.cos
import kotlin.math.sin

class LGCMouseGestureProvider(
    dataOutput: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : MouseGestureProvider(dataOutput, registriesFuture) {
    override fun configure(provider: BiConsumer<Identifier, MouseGesture>, lookup: HolderLookup.Provider?) {
        provider.accept(id("swipe_up"), createSwipe(id("swipe_up"), Vector2d(0.0, -1.0), 100.0))
        provider.accept(id("swipe_down"), createSwipe(id("swipe_down"), Vector2d(0.0, 1.0), 100.0))
        provider.accept(id("swipe_left"), createSwipe(id("swipe_left"), Vector2d(-1.0, 0.0), 100.0))
        provider.accept(id("swipe_right"), createSwipe(id("swipe_right"), Vector2d(1.0, 0.0), 100.0))
    }

    fun createLoop(name: Identifier, clockwise: Boolean, flipY: Boolean, angleFrom: Double, angleTo: Double, diameter: Double = 100.0, segments: Int = 30): MouseGesture {
        val radius = diameter / 2
        val angleStep = Math.toRadians(angleTo - angleFrom) / segments

        val builder = MouseGestureBuilder()
        for (i in segments downTo 0) {
            val theta = Math.toRadians(if (!clockwise) (angleFrom + 180) else angleFrom) + i * angleStep
            val y = (sin(theta) * radius)
            val point = Vector2d(cos(theta) * radius, if (flipY) -y else y)
            builder.point(point)
        }

        builder.name(name)

        return builder.build(requiredPoints = segments)
    }

    fun createSwipe(name: Identifier, direction: Vector2d, scale: Double): MouseGesture {
        val builder = MouseGestureBuilder()
        builder.name(name)
        builder.point(Vector2d())
        builder.point(direction.mul(scale, Vector2d()))
        return builder.build()
    }
}