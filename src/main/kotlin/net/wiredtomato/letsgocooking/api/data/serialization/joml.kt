package net.wiredtomato.letsgocooking.api.data.serialization

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.joml.Vector2d

class Vector2dSerializer : KSerializer<Vector2d> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Vector2d") {
        element("x", PrimitiveSerialDescriptor("x", PrimitiveKind.DOUBLE))
        element("y", PrimitiveSerialDescriptor("y", PrimitiveKind.DOUBLE))
    }

    override fun serialize(encoder: Encoder, value: Vector2d) {
        val obj = encoder.beginStructure(descriptor)
        obj.encodeDoubleElement(descriptor, 0, value.x)
        obj.encodeDoubleElement(descriptor, 1, value.y)
        obj.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Vector2d {
        val obj = decoder.beginStructure(descriptor)
        val x = decoder.decodeDouble()
        val y = decoder.decodeDouble()
        obj.endStructure(descriptor)
        return Vector2d(x, y)
    }
}

val Vector2dCodec: Codec<Vector2d> = RecordCodecBuilder.create { it ->
    it.group(
        Codec.DOUBLE.fieldOf("x").forGetter { it.x },
        Codec.DOUBLE.fieldOf("y").forGetter { it.y }
    ).apply(it, ::Vector2d)
}
