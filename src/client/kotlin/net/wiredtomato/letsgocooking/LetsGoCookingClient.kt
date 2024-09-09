package net.wiredtomato.letsgocooking

import net.minecraft.client.gui.screen.ingame.HandledScreens
import net.wiredtomato.letsgocooking.LetsGoCooking.log
import net.wiredtomato.letsgocooking.api.GameText
import net.wiredtomato.letsgocooking.api.Image
import net.wiredtomato.letsgocooking.api.Rect
import net.wiredtomato.letsgocooking.api.registry.GameElementCreatorRegistry
import net.wiredtomato.letsgocooking.init.LGCScreens
import net.wiredtomato.letsgocooking.screen.GameScreen
import net.wiredtomato.letsgocooking.screen.element.GameTextElement
import net.wiredtomato.letsgocooking.screen.element.ImageElement
import net.wiredtomato.letsgocooking.screen.element.RectElement

@Suppress("unused")
object LetsGoCookingClient{
    fun init() {
        log.info("Hello from Client")

        GameElementCreatorRegistry.register(GameText.ID, ::GameTextElement)
        GameElementCreatorRegistry.register(Image.ID, ::ImageElement)
        GameElementCreatorRegistry.register(Rect.ID, ::RectElement)

        HandledScreens.register(LGCScreens.GAME, ::GameScreen)
    }
}
