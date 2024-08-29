package net.wiredtomato.letsgocooking.networking

import net.minecraft.network.PacketByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.payload.CustomPayload
import net.wiredtomato.letsgocooking.LetsGoCooking.id

object GameClosedPayload : CustomPayload {
    val ID: CustomPayload.Id<GameClosedPayload> = CustomPayload.Id<GameClosedPayload>(id("game_closed"))
    val CODEC = PacketCodec.unit<PacketByteBuf, GameClosedPayload>(GameClosedPayload)
    
    override fun getId(): CustomPayload.Id<GameClosedPayload> = ID
}