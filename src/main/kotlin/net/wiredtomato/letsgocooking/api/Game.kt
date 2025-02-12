package net.wiredtomato.letsgocooking.api

import net.minecraft.entity.player.PlayerInventory
import net.minecraft.inventory.Inventory
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.api.inventory.GameSlot

abstract class Game(val type: GameType<out Game>) {
    private val elements = mutableMapOf<Identifier, GameElement>()
    private val onAdded = mutableListOf<ElementAdded>()
    private val onRemoved = mutableListOf<ElementRemoved>()

    abstract fun title(): Text
    open fun init() {}
    open fun clientTick() {}
    open fun postClientTick() {}
    open fun serverTick(player: ServerPlayerEntity) {}
    open fun postServerTick(player: ServerPlayerEntity) {}
    open fun createSlots(): List<GameSlot<Inventory>> = listOf()
    open fun createPlayerSlots(): List<GameSlot<PlayerInventory>> = listOf()

    fun <T : GameElement> addElement(id: Identifier, element: T): Boolean {
        if (elements.containsKey(id)) return false
        elements[id] = element
        onAdded.forEach { it.onAdded(id, element) }

        return true
    }

    fun <T : GameElement> removeElement(id: Identifier): Boolean {
        val removed = elements.remove(id) ?: return false
        onRemoved.forEach { it.onRemoved(id, removed) }

        return true
    }

    fun onElementAdded(action: ElementAdded) {
        onAdded.add(action)
    }

    fun onElementRemoved(action: ElementRemoved) {
        onRemoved.add(action)
    }

    fun elements(): Map<Identifier, GameElement> = elements

    fun handleClientTick() {
        clientTick()
        elements.forEach { (_, element) -> element.clientTick() }
        postClientTick()
    }

    fun handleServerTick(player: ServerPlayerEntity) {
        serverTick(player)
        elements.forEach { (_, element) -> element.serverTick(player) }
        postServerTick(player)
    }

    fun handleServerElementInteraction(id: Identifier, player: ServerPlayerEntity, type: Interaction) {
        elements[id]?.serverInteraction(player, type)
    }

    fun interface ElementAdded {
        fun onAdded(elementId: Identifier, element: GameElement)
    }

    fun interface ElementRemoved {
        fun onRemoved(elementId: Identifier, element: GameElement)
    }
}
