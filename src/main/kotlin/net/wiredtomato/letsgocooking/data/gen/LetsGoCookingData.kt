package net.wiredtomato.letsgocooking.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.minecraft.registry.RegistrySetBuilder
import net.wiredtomato.letsgocooking.LetsGoCooking.log
import net.wiredtomato.letsgocooking.data.gen.provider.LGCModelProvider

@Suppress("unused")
object LetsGoCookingData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(gen: FabricDataGenerator) {
        log.info("Hello from DataGen")
        val pack = gen.createPack()

        pack.addProvider(::LGCModelProvider)
    }

    override fun buildRegistry(gen: RegistrySetBuilder) {

    }
}
