package net.wiredtomato.letsgocooking.screen.element

import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.GameText

class GameTextElement<T : GameText>(override val id: Identifier, element: T) : InteractableElement<T>(element) {
    override fun render(
        graphics: GuiGraphics,
        mouseX: Int,
        mouseY: Int,
        delta: Float
    ) {
        val textRenderer = MinecraftClient.getInstance().textRenderer
        graphics.drawText(textRenderer, element.text, x, y, element.color, element.shadowed)
    }
}