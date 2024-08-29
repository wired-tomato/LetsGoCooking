package net.wiredtomato.letsgocooking.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.wiredtomato.letsgocooking.block.CookingTestBlock
import net.wiredtomato.letsgocooking.block.tile.CookingTestTile
import net.wiredtomato.letsgocooking.util.register

@Suppress("unused", "MemberVisibilityCanBePrivate")
object LGCBlocks {
    val COOKING_TEST_BLOCK = block("cooking_test_block", CookingTestBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)))
    val COOKING_TEST_TILE = tile("cooking_test_tile", BlockEntityType.Builder.create(::CookingTestTile, COOKING_TEST_BLOCK).build())

    fun <T : Block> block(name: String, block: T): T = Registries.BLOCK.register(name, block)
    fun <T : BlockEntity> tile(name: String, tile: BlockEntityType<T>): BlockEntityType<T> = Registries.BLOCK_ENTITY_TYPE.register(name, tile)
}