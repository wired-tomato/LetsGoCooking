package net.wiredtomato.letsgocooking.api.gui

import net.minecraft.client.gui.Drawable
import net.minecraft.client.gui.Element
import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.ScreenArea
import net.minecraft.client.gui.widget.Widget
import net.minecraft.util.Identifier

interface GuiElement : Element, Drawable, Widget, Selectable {
    val id: Identifier

    override fun getArea(): ScreenArea {
        return ScreenArea(x, y, width, height)
    }
}