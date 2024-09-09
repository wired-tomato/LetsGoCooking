package net.wiredtomato.letsgocooking.networking

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.wiredtomato.letsgocooking.api.GameElement
import net.wiredtomato.letsgocooking.api.Interaction
import net.wiredtomato.letsgocooking.api.gui.GuiElement

fun <T: GuiElement> T.interact(element: GameElement, interaction: Interaction) {
    element.clientInteraction(interaction)
    ClientPlayNetworking.send(GameElementInteractionPayload(id, interaction))
}
