package net.wiredtomato.letsgocooking.init

import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries
import net.wiredtomato.letsgocooking.game.CuttingBoardGame
import net.wiredtomato.letsgocooking.game.IntroGame
import net.wiredtomato.letsgocooking.util.register

@Suppress("unused", "MemberVisibilityCanBePrivate")
object LGCGameTypes {
    val INTRO_GAME = gameType("intro_game", ::IntroGame)
    val CUTTING_BOARD_GAME = gameType("cutting_board_game", ::CuttingBoardGame)

    fun <T : Game> gameType(name: String, create: GameType.GameCreator<T>): GameType<T> = LGCRegistries.GAME_TYPE.register(name, GameType(create))
}