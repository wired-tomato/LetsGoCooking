package net.wiredtomato.letsgocooking.screen.element

import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.Image

class ImageElement<T : Image>(override val id: Identifier, element: T) : ClickableElement<T>(element) {
    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, delta: Float) {
        graphics.drawGuiTexture(element.textureId, x, y, width, height)
    }
}