package net.wiredtomato.letsgocooking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.wiredtomato.letsgocooking.LetsGoCooking.log
import net.wiredtomato.letsgocooking.api.Image
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.init.LGCScreens
import net.wiredtomato.letsgocooking.networking.OpenGameScreenPayload
import net.wiredtomato.letsgocooking.screen.GameScreen
import net.wiredtomato.letsgocooking.screen.element.ImageElement

@Suppress("unused")
object LetsGoCookingClient{
    fun init() {
        log.info("Hello from Client")

        GameElementCreatorRegistry.register(Image.ID, ::ImageElement)

        HandledScreens.register(LGCScreens.GAME, ::GameScreen)

        ClientPlayNetworking.registerGlobalReceiver(OpenGameScreenPayload.ID) { payload, ctx ->
            //val client = ctx.client()
            //val gameType = payload.type

            //val screen = GameScreen()
            //client.setScreen(screen)
        }
    }
}
