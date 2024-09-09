package net.wiredtomato.letsgocooking.api.inventory

import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.Slot
import org.joml.Vector3i

data class GameSlot<T : Inventory>(
    val pos: Vector3i,
    private val slotCreator: (T, Int, Int, Int) -> Slot = ::Slot
) {
    private lateinit var backer: Slot
    private lateinit var inventory: T
    var stack: ItemStack
        get() = if (::backer.isInitialized) backer.stack else ItemStack.EMPTY
        set(value) { if (::backer.isInitialized) backer.stack = value }

    fun createSlot(inventory: T, index: Int): Slot {
        val slot = slotCreator(inventory, index, pos.x, pos.y)
        backer = slot
        this.inventory = inventory
        return slot
    }

    fun slot(): Slot = backer
    fun inventory(): T = inventory
}
