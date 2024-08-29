package net.wiredtomato.letsgocooking.util

import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.item.ItemConvertible
import net.minecraft.registry.Holder
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking.id

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier): TagKey<T> = TagKey.of(this, id)
fun <T, R : Registry<T>> RegistryKey<R>.key(id: Identifier): RegistryKey<T> = RegistryKey.of(this, id)

fun <T, V : T> Registry<T>.register(id: String, entry: V): V = Registry.register(this, id(id), entry)
fun <T> Registry<T>.registerHolder(id: String, entry: T): Holder<T> = Registry.registerHolder(this, id(id), entry)
fun <T, V : T> Registry<T>.register(id: Identifier, entry: V): V = Registry.register(this, id, entry)
fun <T> Registry<T>.registerHolder(id: Identifier, entry: T): Holder<T> = Registry.registerHolder(this, id, entry)


fun Item.toId() = Registries.ITEM.getId(this)
fun Block.toId() = Registries.BLOCK.getId(this)

fun ItemConvertible.toId() = this.asItem().toId()
val ItemConvertible.id get() = this.toId()
