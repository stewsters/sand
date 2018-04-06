package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import com.stewsters.sand.generator.RuinGen
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.component.builder.HeaderBuilder
import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.screen.Screen

class GeneratingActivity(var game: SandGame) : Activity {


    val screen: Screen

    init {
        screen = TerminalBuilder.createScreenFor(game.terminal)

        val header = HeaderBuilder.newBuilder()
                .position(Position.of(3, 3))
                .text("Generating Map")
                .build();

        screen.addComponent(header)

    }

    override fun render() {
        screen.display()

        val world = RuinGen.gen()

        game.activity = PlayActivity(game, world)
        game.render()
    }

    override fun keyPressed(keycode: KeyStroke) {

    }

}
