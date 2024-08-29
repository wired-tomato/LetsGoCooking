package net.wiredtomato.letsgocooking.mixin;

import net.minecraft.server.network.ServerPlayerEntity;
import net.wiredtomato.letsgocooking.api.Game;
import net.wiredtomato.letsgocooking.api.player.PlayerGameData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin implements PlayerGameData {
    @Unique private Game currentGame = null;

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
