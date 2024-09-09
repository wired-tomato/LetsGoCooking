package net.wiredtomato.letsgocooking.init

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType
import net.minecraft.network.codec.PacketCodecs
import net.minecraft.registry.Registries
import net.minecraft.screen.ScreenHandler
import net.minecraft.screen.ScreenHandlerType
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries
import net.wiredtomato.letsgocooking.screen.GameScreenHandler
import net.wiredtomato.letsgocooking.util.register

object LGCScreens {
    val GAME = handler("game", ExtendedScreenHandlerType(::GameScreenHandler, PacketCodecs.fromCodec(LGCRegistries.GAME_TYPE.codec)))

    fun <T : ScreenHandler, V : ScreenHandlerType<T>> handler(name: String, type: V): V {
        return Registries.SCREEN_HANDLER_TYPE.register(name, type)
    }
}