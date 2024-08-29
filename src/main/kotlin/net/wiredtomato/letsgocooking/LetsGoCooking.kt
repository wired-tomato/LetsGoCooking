package net.wiredtomato.letsgocooking

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.player.gameData
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries
import net.wiredtomato.letsgocooking.init.LGCBlocks
import net.wiredtomato.letsgocooking.init.LGCGameTypes
import net.wiredtomato.letsgocooking.init.LGCItems
import net.wiredtomato.letsgocooking.networking.GameClosedPayload
import net.wiredtomato.letsgocooking.networking.GameElementInteractionPayload
import net.wiredtomato.letsgocooking.networking.OpenGameScreenPayload
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("unused")
object LetsGoCooking {
    const val MODID = "lets_go_cooking"

    @JvmField
    val log: Logger = LoggerFactory.getLogger(LetsGoCooking::class.simpleName)

    fun init() {
        LGCRegistries
        LGCItems
        LGCBlocks
        LGCGameTypes
        log.info("Hello from Common")

        PayloadTypeRegistry.playS2C().register(OpenGameScreenPayload.ID, OpenGameScreenPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(GameClosedPayload.ID, GameClosedPayload.CODEC)
        PayloadTypeRegistry.playC2S().register(GameElementInteractionPayload.ID, GameElementInteractionPayload.CODEC)

        ServerPlayNetworking.registerGlobalReceiver(GameClosedPayload.ID) { _, ctx ->
            val player = ctx.player()
            player.gameData.currentGame = null
        }

        ServerPlayNetworking.registerGlobalReceiver(GameElementInteractionPayload.ID) { payload, ctx ->
            val player = ctx.player()
            val currentGame = player.gameData.currentGame
            currentGame?.handleServerElementInteraction(payload.elementId, player, payload.interaction)
        }
    }

    fun id(path: String) = Identifier.of(MODID, path)
}
