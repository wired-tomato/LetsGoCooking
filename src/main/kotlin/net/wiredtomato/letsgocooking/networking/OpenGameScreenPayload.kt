package net.wiredtomato.letsgocooking.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.GameType
import net.wiredtomato.letsgocooking.api.registry.LGCRegistries

data class OpenGameScreenPayload(val type: GameType<*>) : CustomPayload {
    override fun getId(): CustomPayload.Id<OpenGameScreenPayload> = ID

    companion object {
        val ID = CustomPayload.Id<OpenGameScreenPayload>(id("open_game_screen"))
        val CODEC: PacketCodec<PacketByteBuf, OpenGameScreenPayload> = PacketCodec.create(::packetEncode, ::packetDecode)

        private fun packetEncode(buf: PacketByteBuf, payload: OpenGameScreenPayload) {
            val typeId = LGCRegistries.GAME_TYPE.getId(payload.type) ?: throw IllegalStateException("Unregistered GameType: ${payload.type}")
            buf.writeIdentifier(typeId)
        }

        private fun packetDecode(buf: PacketByteBuf): OpenGameScreenPayload {
            val typeId = buf.readIdentifier()
            val type = LGCRegistries.GAME_TYPE[typeId] ?: throw IllegalStateException("Unregistered GameType: $typeId")

            return OpenGameScreenPayload(type)
        }
    }
}