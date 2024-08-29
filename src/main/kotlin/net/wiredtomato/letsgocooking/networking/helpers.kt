package net.wiredtomato.letsgocooking.networking

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.network.ServerPlayerEntity
import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.player.gameData

fun <T : Game> ServerPlayerEntity.sendGameScreen(type: GameType<T>) {
    val serverGame = type.createGame()
    serverGame.init()
    gameData.currentGame = serverGame
    ServerPlayNetworking.send(this, OpenGameScreenPayload(type))
}
