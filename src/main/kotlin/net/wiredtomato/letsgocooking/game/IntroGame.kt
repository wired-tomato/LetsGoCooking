package net.wiredtomato.letsgocooking.game

import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.wiredtomato.letsgocooking.LetsGoCooking
import net.wiredtomato.letsgocooking.api.Game
import net.wiredtomato.letsgocooking.api.Image
import net.wiredtomato.letsgocooking.init.LGCGameTypes
import org.joml.Vector3d

class IntroGame : Game(LGCGameTypes.INTRO_GAME) {
    override fun title(): Text = Text.literal("Cutting Board")

    override fun init() {
        addElement(LetsGoCooking.id(""), Image(Vector3d(), Vector3d(10.0), Identifier.ofDefault("null")))
    }

    override fun clientTick() { }
}