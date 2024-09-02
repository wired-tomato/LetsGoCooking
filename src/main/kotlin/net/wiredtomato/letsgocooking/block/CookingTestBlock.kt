package net.wiredtomato.letsgocooking.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.NamedScreenHandlerFactory
import net.minecraft.screen.ScreenHandler
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.wiredtomato.letsgocooking.block.tile.CookingTestTile
import net.wiredtomato.letsgocooking.screen.GameScreenHandler

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
        if (!world.isClient && player is ServerPlayerEntity) {
            player.openHandledScreen(object : NamedScreenHandlerFactory {
                override fun createMenu(
                    i: Int,
                    playerInventory: PlayerInventory,
                    playerEntity: PlayerEntity
                ): ScreenHandler {
                    return GameScreenHandler(i, playerInventory)
                }

                override fun getDisplayName(): Text = Text.literal("")
            })
        }

        return ActionResult.SUCCESS
    }
}