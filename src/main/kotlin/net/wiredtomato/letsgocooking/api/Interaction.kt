package net.wiredtomato.letsgocooking.api

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.codec.PacketCodecs
import net.wiredtomato.letsgocooking.api.input.MouseGesture
import org.joml.Vector2d

sealed class Interaction(val id: String) {
    companion object {
        val PACKET_CODEC: PacketCodec<PacketByteBuf, Interaction> = PacketCodecs.STRING.mapBuf<PacketByteBuf> { PacketByteBuf(it) }
            .dispatch({ it.id }, {
                when (it) {
                    "mouseClick" -> MouseClick.PACKET_CODEC
                    "mouseGestureInteraction" -> MouseGestureInteraction.PACKET_CODEC
                    else -> NoInteraction.PACKET_CODEC
                }
            })
    }
}

class MouseClick(val pos: Vector2d, val button: Int): Interaction("mouseClick") {
    companion object {
        val PACKET_CODEC = PacketCodec.create(::encode, ::decode)

        private fun encode(buf: PacketByteBuf, click: MouseClick) {
            buf.writeDouble(click.pos.x)
            buf.writeDouble(click.pos.y)
            buf.writeInt(click.button)
        }

        private fun decode(buf: PacketByteBuf): MouseClick {
            return MouseClick(Vector2d(buf.readDouble(), buf.readDouble()), buf.readInt())
        }
    }
}

class MouseGestureInteraction(val gesture: MouseGesture.Instance): Interaction("mouseGestureInteraction") {
    companion object {
        val PACKET_CODEC = PacketCodec.create(::encode, ::decode)

        private fun encode(buf: PacketByteBuf, gestureInteraction: MouseGestureInteraction) {
            MouseGesture.Instance.PACKET_CODEC.encode(buf, gestureInteraction.gesture)
        }

        private fun decode(buf: PacketByteBuf): MouseGestureInteraction {
            return MouseGestureInteraction(MouseGesture.Instance.PACKET_CODEC.decode(buf))
        }
    }
}

class NoInteraction(): Interaction("none") {
    companion object {
        val PACKET_CODEC = PacketCodec.create(::encode, ::decode)

        private fun encode(buf: PacketByteBuf, none: NoInteraction) {}
        private fun decode(buf: PacketByteBuf): NoInteraction = NoInteraction()
    }
}
