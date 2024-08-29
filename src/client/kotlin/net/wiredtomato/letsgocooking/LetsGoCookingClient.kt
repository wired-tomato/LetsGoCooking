package net.wiredtomato.letsgocooking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.wiredtomato.letsgocooking.LetsGoCooking.log
import net.wiredtomato.letsgocooking.api.Image
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.networking.OpenGameScreenPayload
import net.wiredtomato.letsgocooking.screen.GameScreen
import net.wiredtomato.letsgocooking.screen.element.ImageElement

@Suppress("unused")
object LetsGoCookingClient{
    fun init() {
        log.info("Hello from Client")

        GameElementCreatorRegistry.register(Image.ID, ::ImageElement)

        ClientPlayNetworking.registerGlobalReceiver(OpenGameScreenPayload.ID) { payload, ctx ->
            val client = ctx.client()
            val gameType = payload.type

            val screen = GameScreen(gameType)
            client.setScreen(screen)
        }
    }
}
