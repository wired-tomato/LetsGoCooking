package net.wiredtomato.letsgocooking.api

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import org.joml.Vector3d

abstract class GameElement internal constructor(var pos: Vector3d, val id: Id<out GameElement>) {
    open fun clientTick() {}
    open fun clientInteraction(interaction: Interaction) {}
    open fun serverInteraction(player: ServerPlayerEntity, interaction: Interaction) {}

    data class Id<T : GameElement>(val id: Identifier)
}

open class Image(pos: Vector3d, override var size: Vector3d, var textureId: Identifier) : GameElement(pos, ID), Sized {
    companion object {
        val ID = Id<Image>(id("image"))
    }
}

open class GameText(pos: Vector3d, var text: Text, var color: Int, var shadowed: Boolean) : GameElement(pos, ID) {
    companion object {
        val ID = Id<GameText>(id("text"))
    }
}

interface Sized {
    var size: Vector3d
}
