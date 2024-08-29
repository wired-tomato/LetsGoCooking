package net.wiredtomato.letsgocooking.init

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.wiredtomato.letsgocooking.util.register

@Suppress("unused", "MemberVisibilityCanBePrivate")
object LGCItems {
    val COOKING_TEST_BLOCK = item("cooking_test_block", BlockItem(LGCBlocks.COOKING_TEST_BLOCK, Item.Settings()))

    fun <T : Item> item(name: String, entry: T): T = Registries.ITEM.register(name, entry)
}