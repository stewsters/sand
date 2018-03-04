package com.stewsters.sand.display.activities

import com.stewsters.sand.GameInfo
import com.stewsters.sand.SandGame
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.component.builder.ButtonBuilder
import org.codetome.zircon.api.component.builder.HeaderBuilder
import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.screen.Screen
import java.util.function.Consumer

class MenuActivity(var game: SandGame) : Activity {


    val screen: Screen

    init {


        screen = TerminalBuilder.createScreenFor(game.terminal)

        // this will be 1x1 left and down from the top left
        // corner of the panel
        val header = HeaderBuilder.newBuilder()
                .position(Position.of(3, 0))
                .text(GameInfo.subText)
                .build();

        val play = ButtonBuilder.newBuilder()
                .text("Play")
                .position(Position.of(0, 2).relativeToBottomOf(header))
                .build()

        val edit = ButtonBuilder.newBuilder()
                .text("Edit")
                .position(Position.of(0, 2).relativeToBottomOf(play))
                .build()

        val exit = ButtonBuilder.newBuilder()
                .text("Exit")
                .position(Position.of(0, 2).relativeToBottomOf(edit))
                .build()



        play.onMousePressed(Consumer { mouseAction ->
            game.activity = PlayActivity(game)
            game.render()
        })

        edit.onMousePressed(Consumer {
            game.activity = CreateMapActivity(game)
            game.render()
        })

        exit.onMousePressed(Consumer { mouseAction ->
            println("Bye")
            System.exit(0)
        })

        screen.addComponent(header)
        screen.addComponent(play)
        screen.addComponent(edit)
        screen.addComponent(exit)
    }


    override fun keyPressed(keycode: KeyStroke) {
//        when (keycode.getCharacter()) {
//            'w'->
//        }
    }

    override fun render() {
        screen.display()
    }

}