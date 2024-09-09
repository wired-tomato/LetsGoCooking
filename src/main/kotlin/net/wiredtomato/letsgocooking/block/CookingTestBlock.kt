package net.wiredtomato.letsgocooking.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.wiredtomato.letsgocooking.block.tile.CookingTestTile
import net.wiredtomato.letsgocooking.init.LGCGameTypes

class CookingTestBlock(settings: Settings) : BlockWithEntity(settings) {
    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec(::CookingTestBlock)

    override fun createBlockEntity(pos: BlockPos, state: BlockState): CookingTestTile {
        return CookingTestTile(pos, state)
    }

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            player.openHandledScreen(LGCGameTypes.CUTTING_BOARD_GAME)
        }

        return ActionResult.SUCCESS
    }
}