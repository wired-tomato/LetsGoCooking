package net.wiredtomato.letsgocooking.screen.element

import net.minecraft.client.gui.Selectable
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder
import net.minecraft.client.gui.widget.ClickableWidget
import net.wiredtomato.letsgocooking.api.GameElement
import net.wiredtomato.letsgocooking.api.MouseClick
import net.wiredtomato.letsgocooking.api.MouseGestureInteraction
import net.wiredtomato.letsgocooking.api.Sized
import net.wiredtomato.letsgocooking.api.gui.GuiElement
import net.wiredtomato.letsgocooking.api.input.MouseGesture
import net.wiredtomato.letsgocooking.networking.interact
import org.joml.Vector2d
import java.util.function.Consumer

abstract class InteractableElement<T: GameElement>(protected val element: T) : GuiElement {
    private var focused = false

    override fun setFocused(focused: Boolean) {
        this.focused = focused
    }

    override fun isFocused() = focused

    override fun setX(x: Int) {
        element.pos.x = x.toDouble()
    }

    override fun setY(y: Int) {
        element.pos.y = y.toDouble()
    }

    fun sendMouseGesture(gesture: MouseGesture.Instance) {
        val interaction = MouseGestureInteraction(gesture)
        interact(element, interaction)
    }

    override fun getX(): Int = element.pos.x.toInt()
    override fun getY(): Int = element.pos.y.toInt()
    override fun getWidth(): Int = if (element is Sized) (element.size.x.toInt()) else 0
    override fun getHeight(): Int = if (element is Sized) (element.size.y.toInt()) else 0

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isMouseOver(mouseX, mouseY)) {
            val click = MouseClick(Vector2d(mouseX, mouseY), button)
            interact(element, click)
            return true
        }

        return false
    }

    override fun isMouseOver(mouseX: Double, mouseY: Double): Boolean {
        return mouseX >= this.getX().toDouble() && mouseY >= this.getY()
            .toDouble() && mouseX < (this.getX() + this.width).toDouble() && mouseY < (this.getY() + this.height).toDouble()
    }

    override fun getType(): Selectable.SelectionType = if (focused) Selectable.SelectionType.FOCUSED else Selectable.SelectionType.NONE

    override fun appendNarrations(builder: NarrationMessageBuilder) { }

    override fun visitWidgets(widgetConsumer: Consumer<ClickableWidget>) {}
}