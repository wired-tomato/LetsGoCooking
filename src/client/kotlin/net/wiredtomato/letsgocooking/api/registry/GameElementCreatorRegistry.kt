package net.wiredtomato.letsgocooking.api.registry

import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.GameElement
import net.wiredtomato.letsgocooking.api.GameElementCreator
import net.wiredtomato.letsgocooking.api.gui.GuiElement

@Suppress("UNCHECKED_CAST")
object GameElementCreatorRegistry {
    private val registry = mutableMapOf<GameElement.Id<*>, Holder<*>>()

    fun <T : GameElement> register(id: GameElement.Id<T>, creator: GameElementCreator<T>) {
        val existing = registry.put(id, Holder(creator))
        if (existing != null) throw IllegalStateException("Duplicate registration for id: $id")
    }

    operator fun <T : GameElement> get(id: GameElement.Id<T>): Holder<T> {
        return registry[id] as Holder<T>
    }

    fun create(id: Identifier, element: GameElement): GuiElement {
        return get(element.id).create(id, element)
    }

    data class Holder<T : GameElement>(val creator: GameElementCreator<T>) {
        fun create(id: Identifier, element: GameElement): GuiElement = creator.create(id, element as T)
    }
}