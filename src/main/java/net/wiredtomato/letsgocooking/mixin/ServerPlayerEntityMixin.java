package net.wiredtomato.letsgocooking.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.wiredtomato.letsgocooking.api.Game;
import net.wiredtomato.letsgocooking.api.player.PlayerGameData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerGameData {
    @Unique private Game currentGame = null;

    @Inject(method = "tick", at = @At("HEAD"))
    public void gameTick(CallbackInfo ci) {
        if (currentGame != null) currentGame.handleServerTick((ServerPlayerEntity) (Object) this);
    }

    @Unique
    @Override
    public @Nullable Game letsGoCooking$getCurrentGame() {
        return currentGame;
    }

    @Unique
    @Override
    public void letsGoCooking$setCurrentGame(@Nullable Game game) {
        currentGame = game;
    }
}
