package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import org.hexworks.zircon.api.builder.component.ButtonBuilder
import org.hexworks.zircon.api.builder.component.HeaderBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode

class LoseActivity(var game: SandGame) : Activity {

    val screen: Screen = ScreenBuilder.createScreenFor(game.tileGrid)

    init {

        val header = HeaderBuilder.newBuilder()
            .withPosition(Position.create(3, 3))
            .withText("You have died in the desert")
            .build()

        val main = ButtonBuilder.newBuilder()
            .withText("Return To Main Menu")
            .withPosition(Position.create(0, 2).relativeToBottomOf(header))
            .build()

        main.onActivated { mouseAction ->
            game.activity = MenuActivity(game)
            game.render()
        }

        screen.addComponent(header)
        screen.addComponent(main)
    }

    override fun render() {
        screen.display()
    }

    override fun keyPressed(keycode: KeyCode) {

    }


}
