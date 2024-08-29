package net.wiredtomato.letsgocooking.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.wiredtomato.letsgocooking.init.LGCBlocks
import net.wiredtomato.letsgocooking.util.id

class LGCModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) = with(blockStateModelGenerator) {
        registerSimpleState(LGCBlocks.COOKING_TEST_BLOCK)
        registerParentedItemModel(LGCBlocks.COOKING_TEST_BLOCK, LGCBlocks.COOKING_TEST_BLOCK.id.withPrefix("block/"))
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) { }
}