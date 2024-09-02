package net.wiredtomato.letsgocooking.api.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.data.DataPackOutput
import net.minecraft.registry.HolderLookup
import net.wiredtomato.letsgocooking.LetsGoCooking
import net.wiredtomato.letsgocooking.api.input.MouseGesture
import java.util.concurrent.CompletableFuture

abstract class MouseGestureProvider(
    dataOutput: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricCodecDataProvider<MouseGesture>(
    dataOutput,
    registriesFuture,
    DataPackOutput.Type.DATA_PACK,
    "${LetsGoCooking.MODID}/mouse_gesture",
    MouseGesture.CODEC
) {
    override fun getName(): String = "Mouse Gesture Provider"
}