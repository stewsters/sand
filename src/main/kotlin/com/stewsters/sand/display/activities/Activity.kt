package com.stewsters.sand.display.activities

import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.MouseEvent


interface Activity {

//    fun getGame():SandGame

    fun render()

    fun keyPressed(keycode: KeyCode)
    fun mouseAction(mouseAction: MouseEvent): Boolean = false

}