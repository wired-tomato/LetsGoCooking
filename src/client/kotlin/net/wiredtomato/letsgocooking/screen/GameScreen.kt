package net.wiredtomato.letsgocooking.screen

import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.gui.GuiElement
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.networking.sendCloseGame

class GameScreen(type: GameType<*>) : Screen(Text.literal("Game")) {
    private val game: Game = type.createGame()

    override fun init() {
        game.onElementAdded { id, element ->
            val guiElement = GameElementCreatorRegistry.create(id, element)
            addDrawableSelectableElement(guiElement)
        }

        game.onElementRemoved { id, _ ->
            val guiElement = children().find { it is GuiElement && it.id == id }
            remove(guiElement)
        }
        game.init()
    }

    override fun tick() {
        game.handleClientTick()
    }

    override fun closeScreen() {
        super.closeScreen()
        sendCloseGame()
    }
}