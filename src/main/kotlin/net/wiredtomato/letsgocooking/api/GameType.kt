package net.wiredtomato.letsgocooking.api

class GameType<T : Game>(private val creator: GameCreator<T>) {
    fun createGame(): T = creator.createGame()

    fun interface GameCreator<T : Game> {
        fun createGame(): T
    }
}