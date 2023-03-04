package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import com.stewsters.sand.generator.RuinGen

import org.hexworks.zircon.api.builder.component.HeaderBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode

class GeneratingActivity(var game: SandGame) : Activity {


    val screen: Screen = ScreenBuilder.createScreenFor(game.tileGrid)

    init {

        val header = HeaderBuilder.newBuilder()
            .withPosition(Position.create(3, 3))
            .withText("Generating Map")
            .build()

        screen.addComponent(header)

    }

    override fun render() {
        screen.display()

        val world = RuinGen.gen()

        game.activity = PlayActivity(game, world)
        game.render()
    }

    override fun keyPressed(keycode: KeyCode) {

    }

}
