package com.stewsters.sand.display.activities

import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.input.MouseAction

interface Activity {

//    fun getGame():SandGame

    fun render()

    fun keyPressed(keycode: KeyStroke)
    fun mouseAction(mouseAction: MouseAction): Boolean = false

}