package net.wiredtomato.letsgocooking.api

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import org.joml.Vector2d

sealed class Interaction(val id: String) {
    companion object {
        val PACKET_CODEC = PacketCodecs.STRING.dispatch<Interaction>({ it.id }, {
            when (it) {
                "mouseClick" -> MouseClick.PACKET_CODEC
                else -> throw IllegalArgumentException("Interaction type $it is not supported")
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
