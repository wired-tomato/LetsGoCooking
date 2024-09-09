package net.wiredtomato.letsgocooking.api

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import org.joml.Vector3d
import kotlin.reflect.KClass

abstract class GameElement internal constructor(var pos: Vector3d, val id: Id<out GameElement>) {
    protected val behaviors = mutableListOf<ElementBehavior>()

    open fun clientTick() {
        behaviors.forEach { it.screenTick() }
    }

    open fun serverTick(player: ServerPlayerEntity) {
        behaviors.forEach { it.playerTick(player) }
    }

    open fun clientInteraction(interaction: Interaction) {
        behaviors.forEach { it.clientInteraction(interaction) }
    }

    open fun serverInteraction(player: ServerPlayerEntity, interaction: Interaction) {
        behaviors.forEach { it.serverInteraction(player, interaction) }
    }

    fun attachBehavior(behavior: ElementBehavior) {
        if (behaviors.any { it::class == behavior::class }) return
        behaviors.add(behavior)
    }

    fun detachBehavior(behaviorKlass: KClass<out ElementBehavior>) {
        behaviors.removeAll { it::class == behaviorKlass }
    }

    data class Id<T : GameElement>(val id: Identifier)
}

open class Image(pos: Vector3d, override var size: Vector3d, var textureId: Identifier) : GameElement(pos, ID), Sized {
    companion object {
        val ID = Id<Image>(id("image"))
    }
}

open class Rect(pos: Vector3d, override var size: Vector3d, var color: Int) : GameElement(pos, ID), Sized {
    companion object {
        val ID = Id<Rect>(id("rect"))
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

interface ElementBehavior {
    fun screenTick() {}
    fun playerTick(player: ServerPlayerEntity) {}
    fun clientInteraction(interaction: Interaction) {}
    fun serverInteraction(player: ServerPlayerEntity, interaction: Interaction) {}
}
