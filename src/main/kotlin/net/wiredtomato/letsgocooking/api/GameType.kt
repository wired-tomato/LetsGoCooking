package net.wiredtomato.letsgocooking.api

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.wiredtomato.letsgocooking.screen.GameScreenHandler

class GameType<T : Game>(private val creator: GameCreator<T>) : ExtendedScreenHandlerFactory<GameType<T>> {
    fun createGame(): T = creator.createGame()

    override fun getDisplayName(): Text = Text.literal("")
    override fun getScreenOpeningData(player: ServerPlayerEntity): GameType<T> = this
    override fun createMenu(i: Int, playerInventory: PlayerInventory, playerEntity: PlayerEntity): ScreenHandler {
        return GameScreenHandler(i, playerInventory, this)
    }

    fun interface GameCreator<T : Game> {
        fun createGame(): T
    }
}