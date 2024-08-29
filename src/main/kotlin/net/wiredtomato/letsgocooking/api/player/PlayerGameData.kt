package net.wiredtomato.letsgocooking.api.player

import net.minecraft.server.network.ServerPlayerEntity
import net.wiredtomato.letsgocooking.api.Game

@Suppress("INAPPLICABLE_JVM_NAME")
interface PlayerGameData {
    @get:JvmName("letsGoCooking\$getCurrentGame")
    @set:JvmName("letsGoCooking\$setCurrentGame")
    var currentGame: Game?
}

val ServerPlayerEntity.gameData: PlayerGameData get() = (this as PlayerGameData)
