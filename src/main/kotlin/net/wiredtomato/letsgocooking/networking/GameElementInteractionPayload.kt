package net.wiredtomato.letsgocooking.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.Interaction

data class GameElementInteractionPayload(
    val elementId: Identifier,
    val interaction: Interaction
) : CustomPayload {
    override fun getId() = ID

    companion object {
        val ID = CustomPayload.Id<GameElementInteractionPayload>(id("game_element_interaction"))
        val CODEC = PacketCodec.create(::packetEncode, ::packetDecode)

        private fun packetEncode(buf: PacketByteBuf, payload: GameElementInteractionPayload) {
            buf.writeIdentifier(payload.elementId)
            Interaction.PACKET_CODEC.encode(buf, payload.interaction)
        }

        private fun packetDecode(buf: PacketByteBuf): GameElementInteractionPayload {
            val id = buf.readIdentifier()
            val interaction = Interaction.PACKET_CODEC.decode(buf)

            return GameElementInteractionPayload(id, interaction)
        }
    }
}