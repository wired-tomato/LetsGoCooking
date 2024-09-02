package net.wiredtomato.letsgocooking.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.player.gameData
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries
import net.wiredtomato.letsgocooking.init.LGCGameTypes
import net.wiredtomato.letsgocooking.init.LGCScreens

class GameScreenHandler(syncId: Int, val playerInventory: PlayerInventory, val game: GameType<*> = LGCGameTypes.INTRO_GAME) : ScreenHandler(LGCScreens.GAME, syncId) {
    private val player = playerInventory.player

    init {
        if (player is ServerPlayerEntity) {
            player.gameData.currentGame = game.createGame()
        }
    }

    fun gestures() = player.world.registryManager.get(LGCRegistries.Keys.MOUSE_GESTURE).toList()

    override fun quickTransfer(player: PlayerEntity, fromIndex: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}