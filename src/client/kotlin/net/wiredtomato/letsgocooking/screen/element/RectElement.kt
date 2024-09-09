package net.wiredtomato.letsgocooking.screen.element

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.Rect

class RectElement(override val id: Identifier, element: Rect) : InteractableElement<Rect>(element) {
    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        graphics.fill(
            element.pos.x.toInt(),
            element.pos.y.toInt(),
            (element.pos.x + element.size.x).toInt(),
            (element.pos.y + element.size.y).toInt(),
            element.pos.z.toInt(),
            element.color
        )
    }
}