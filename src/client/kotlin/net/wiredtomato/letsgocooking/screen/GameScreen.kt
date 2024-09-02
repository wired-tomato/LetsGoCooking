package net.wiredtomato.letsgocooking.screen

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.text.Text
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.gui.GuiElement
import net.wiredtomato.letsgocooking.api.input.Input
import net.wiredtomato.letsgocooking.api.input.MouseGestureRecognizer
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.init.LGCGameTypes
import net.wiredtomato.letsgocooking.networking.sendCloseGame
import org.joml.Vector2d
import org.joml.Vector2i

class GameScreen(
    handler: GameScreenHandler,
    inventory: PlayerInventory,
    name: Text,
    type: GameType<*> = LGCGameTypes.INTRO_GAME
) : HandledScreen<GameScreenHandler>(handler, inventory, name) {
    private val game: Game = type.createGame()
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

        gestureRecognizer.onEndGesture {
            val match = it.findMatchWithinDifference()
            println(match?.first?.name)
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
            graphics.matrices.pop()
        }
    }

    fun drawPoints(graphics: GuiGraphics, points: List<Vector2d>) {
        points.map { Vector2i(it.x.toInt(), it.y.toInt()) }.forEach { point ->
            graphics.drawGuiTexture(id("null"), point.x - 2, point.y - 2, 4, 4)
        }
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

    override fun closeScreen() {
        super.closeScreen()
        sendCloseGame()
    }
}