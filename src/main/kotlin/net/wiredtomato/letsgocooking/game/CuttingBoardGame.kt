package net.wiredtomato.letsgocooking.game

import net.minecraft.text.Text
import net.wiredtomato.letsgocooking.LetsGoCooking.id
import net.wiredtomato.letsgocooking.api.*
import net.wiredtomato.letsgocooking.init.LGCGameTypes
import org.joml.Vector3d

class CuttingBoardGame : Game(LGCGameTypes.CUTTING_BOARD_GAME) {
    override fun title(): Text = Text.translatable("")

    override fun init() {
        addElement(id("simple"), Rect(Vector3d(), Vector3d(100.0), 0xFFFFFFFF.toInt()))
    }
}