package net.wiredtomato.letsgocooking.api

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import org.joml.Vector2d

sealed class Interaction(val id: String) {
    companion object {
        val PACKET_CODEC = PacketCodecs.STRING.dispatch({ it.id }, {
            when (it) {
                "mouseClick" -> MouseClick.PACKET_CODEC
                else -> NoInteraction.PACKET_CODEC
            }
        })
    }
}

class MouseClick(val pos: Vector2d, val button: Int): Interaction("mouseClick") {
    companion object {
        val PACKET_CODEC = PacketCodec.create(::encode, ::decode)

        private fun encode(buf: ByteBuf, click: MouseClick) {
            buf.writeDouble(click.pos.x)
            buf.writeDouble(click.pos.y)
            buf.writeInt(click.button)
        }

        private fun decode(buf: ByteBuf): MouseClick {
            return MouseClick(Vector2d(buf.readDouble(), buf.readDouble()), buf.readInt())
        }
    }
}

class NoInteraction(): Interaction("none") {
    companion object {
        val PACKET_CODEC = PacketCodec.create(::encode, ::decode)

        private fun encode(buf: ByteBuf, none: NoInteraction) {}
        private fun decode(buf: ByteBuf): NoInteraction = NoInteraction()
    }
}
