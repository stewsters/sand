package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.component.builder.ButtonBuilder
import org.codetome.zircon.api.component.builder.HeaderBuilder
import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.screen.Screen
import java.util.function.Consumer

class LoseActivity(var game: SandGame) : Activity {

    val screen: Screen = TerminalBuilder.createScreenFor(game.terminal)

    init {

        val header = HeaderBuilder.newBuilder()
                .position(Position.of(3, 3))
                .text("You have died in the desert")
                .build()

        val main = ButtonBuilder.newBuilder()
                .text("Return To Main Menu")
                .position(Position.of(0, 2).relativeToBottomOf(header))
                .build()

        main.onMousePressed(Consumer { mouseAction ->
            game.activity = MenuActivity(game)
            game.render()
        })

        screen.addComponent(header)
        screen.addComponent(main)
    }

    override fun render() {
        screen.display()
    }

    override fun keyPressed(keycode: KeyStroke) {

    }

}
