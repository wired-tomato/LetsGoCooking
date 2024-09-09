package net.wiredtomato.letsgocooking.screen

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.render.RenderLayer
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.gui.GuiElement
import net.wiredtomato.letsgocooking.api.input.Input
import net.wiredtomato.letsgocooking.api.input.MouseGestureRecognizer
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.screen.element.InteractableElement
import org.joml.Math
import org.joml.Vector2d

class GameScreen(
    handler: GameScreenHandler,
    inventory: PlayerInventory,
    name: Text
) : HandledScreen<GameScreenHandler>(handler, inventory, name) {
    private val game: Game = handler.gameType.createGame()
    private var mousePos = Vector2d()
    private var mouseDown = false
    private var delta = 0f
    private val gestureRecognizer = MouseGestureRecognizer(Input({ mousePos }, { mouseDown }, { delta }))
    private val debugRender = true
    private var init = false

    override fun init() {
        if (init) return

        gestureRecognizer.clearGestures()
        handler.gestures().run {
            gestureRecognizer.withGestures(this)
        }

        gestureRecognizer.onEndGesture { it ->
            val match = it.findMatchWithinDifference() ?: return@onEndGesture
            children().forEach {
                if (it is InteractableElement<*>) it.sendMouseGesture(match.first)
            }
        }

        game.onElementAdded { id, element ->
            val guiElement = GameElementCreatorRegistry.create(id, element)
            addDrawableSelectableElement(guiElement)
        }

        game.onElementRemoved { id, _ ->
            val guiElement = children().find { it is GuiElement && it.id == id }
            remove(guiElement)
        }
        game.init()

        init = true
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        super.render(graphics, mouseX, mouseY, delta)
        this.delta = delta
        gestureRecognizer.update()

        if (debugRender) {
            graphics.matrices.push()
            graphics.matrices.translate(this.x.toDouble(), this.y.toDouble(), 0.0)
            val points = gestureRecognizer.rawPoints()
            drawPoints(graphics, points)
            graphics.draw()
            graphics.matrices.pop()
        }
    }

    fun drawPoints(graphics: GuiGraphics, points: List<Vector2d>) {
        points.forEachIndexed { i, point ->
            val color = 0xffff6347.toInt()
            graphics.fill(point.x.toInt() - 2, point.y.toInt() - 2, point.x.toInt() + 2, point.y.toInt() + 2, color)
            val next = points.getOrNull(i + 1)
            if (next != null) {
                val offset = Vec2f((next.x - point.x).toFloat(), (next.x - point.x).toFloat()).perpendicular().invnormalize().multiply(1f)
                val matrix = graphics.matrices.peek()
                val buffer = graphics.vertexConsumers.getBuffer(RenderLayer.getGui())
                buffer.xyz(matrix, (point.x + offset.x).toFloat(), (point.y + offset.y).toFloat(), 0f).color(color)
                buffer.xyz(matrix, (point.x - offset.x).toFloat(), (point.y - offset.y).toFloat(), 0f).color(color)
                buffer.xyz(matrix, (next.x - offset.x).toFloat(), (next.y - offset.y).toFloat(), 0f).color(color)
                buffer.xyz(matrix, (next.x + offset.x).toFloat(), (next.y + offset.y).toFloat(), 0f).color(color)
                graphics.draw()
            }
        }
    }

    fun Vec2f.perpendicular() = Vec2f(y, -x)

    fun Vec2f.invnormalize(): Vec2f {
        val invLength = Math.invsqrt(x * x + y * y)
        return Vec2f(x * invLength, y * invLength)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        mousePos = Vector2d(mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) mouseDown = true
        return false
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0) mouseDown = false
        return false
    }

    override fun handledScreenTick() {
        game.handleClientTick()
    }

    override fun drawBackground(graphics: GuiGraphics, delta: Float, mouseX: Int, mouseY: Int) { }
    override fun drawForeground(graphics: GuiGraphics?, mouseX: Int, mouseY: Int) { }
}