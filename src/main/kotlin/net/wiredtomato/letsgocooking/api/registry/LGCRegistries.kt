package net.wiredtomato.letsgocooking.api.registry

import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.input.MouseGesture

object LGCRegistries {
    val GAME_TYPE: Registry<GameType<*>> = FabricRegistryBuilder.createSimple(Keys.GAME_TYPE).buildAndRegister()

    init {
        DynamicRegistries.registerSynced(Keys.MOUSE_GESTURE, MouseGesture.CODEC)
    }

    object Keys {
        val GAME_TYPE = RegistryKey.ofRegistry<GameType<*>>(id("game_type"))
        val MOUSE_GESTURE = RegistryKey.ofRegistry<MouseGesture>(id("mouse_gesture"))
    }
}