package com.stewsters.sand.display.activities

import com.stewsters.sand.GameInfo
import com.stewsters.sand.SandGame
import org.hexworks.zircon.api.builder.component.ButtonBuilder
import org.hexworks.zircon.api.builder.component.HeaderBuilder
import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import kotlin.system.exitProcess

class MenuActivity(val game: SandGame) : Activity {


    val screen: Screen = ScreenBuilder.createScreenFor(game.tileGrid)

    init {


        // this will be 1x1 left and down from the top left
        // corner of the panel
        val header = HeaderBuilder.newBuilder()
            .withPosition(Position.create(3, 0))
            .withText(GameInfo.subText)
            .build()

        val play = ButtonBuilder.newBuilder()
            .withText("Play")
            .withPosition(Position.create(0, 2).relativeToBottomOf(header))
            .build()

        val edit = ButtonBuilder.newBuilder()
            .withText("Edit")
            .withPosition(Position.create(0, 2).relativeToBottomOf(play))
            .build()

        val exit = ButtonBuilder.newBuilder()
            .withText("Exit")
            .withPosition(Position.create(0, 2).relativeToBottomOf(edit))
            .build()



        play.onActivated { mouseAction ->
            game.activity = GeneratingActivity(game)
            game.render()
        }

        edit.onActivated {
            game.activity = CreateMapActivity(game)
            game.render()
        }

        exit.onActivated { mouseAction ->
            println("Bye")
            exitProcess(0)
        }

        screen.addComponent(header)
        screen.addComponent(play)
        screen.addComponent(edit)
        screen.addComponent(exit)
    }


    override fun keyPressed(keycode: KeyCode) {
//        when (keycode.getCharacter()) {
//            'w'->
//        }
    }

    override fun render() {
        screen.display()
    }

}