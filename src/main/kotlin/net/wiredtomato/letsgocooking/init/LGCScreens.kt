package net.wiredtomato.letsgocooking.init

import net.minecraft.feature_flags.FeatureFlags
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.wiredtomato.letsgocooking.screen.GameScreenHandler
import net.wiredtomato.letsgocooking.util.register

object LGCScreens {
    val GAME = handler("game", ScreenHandlerType(::GameScreenHandler, FeatureFlags.DEFAULT_SET))

    fun <T : ScreenHandler> handler(name: String, type: ScreenHandlerType<T>): ScreenHandlerType<T> {
        return Registries.SCREEN_HANDLER_TYPE.register(name, type)
    }
}