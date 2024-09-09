package net.wiredtomato.letsgocooking.screen

import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.SimpleInventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.player.gameData
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries
import net.wiredtomato.letsgocooking.init.LGCScreens

class GameScreenHandler(syncId: Int, val playerInventory: PlayerInventory, val gameType: GameType<*>) : ScreenHandler(LGCScreens.GAME, syncId) {
    private val player = playerInventory.player
    private val game = gameType.createGame()
    private val gameSlots = game.createSlots()
    private val playerSlots = game.createPlayerSlots()
    private val inventory = SimpleInventory(gameSlots.size)

    init {
        if (player is ServerPlayerEntity) {
            player.gameData.currentGame = game
            game.init()
        }

        gameSlots.forEachIndexed { i, gameSlot ->
            val slot = gameSlot.createSlot(inventory, i)
            addSlot(slot)
        }

        playerSlots.forEachIndexed { i, playerSlot ->
            val slot = playerSlot.createSlot(playerInventory, i)
            addSlot(slot)
        }
    }

    fun gestures() = player.world.registryManager.get(LGCRegistries.Keys.MOUSE_GESTURE).toList()

    override fun close(player: PlayerEntity?) {
        super.close(player)

        if (player is ServerPlayerEntity) {
            player.gameData.currentGame = null
        }
    }

    override fun quickTransfer(player: PlayerEntity, fromIndex: Int): ItemStack {
        return ItemStack.EMPTY
    }

    override fun canUse(player: PlayerEntity): Boolean {
        return true
    }
}