package net.wiredtomato.letsgocooking.api

import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.gui.GuiElement

fun interface GameElementCreator<T : GameElement> {
    fun create(id: Identifier, element: T): GuiElement
}
