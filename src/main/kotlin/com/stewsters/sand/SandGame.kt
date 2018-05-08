package com.stewsters.sand

import com.stewsters.sand.display.activities.Activity
import com.stewsters.sand.display.activities.MenuActivity
import org.codetome.zircon.api.Size
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.input.MouseAction
import org.codetome.zircon.api.resource.CP437TilesetResource
import org.codetome.zircon.api.terminal.Terminal
import java.util.function.Consumer

class SandGame(val terminal: Terminal, var activity: Activity? = null) {


    fun keyPress(keyStroke: KeyStroke) {
        activity?.keyPressed(keyStroke)
        activity?.render()

    }

    fun mouseAction(mouseAction: MouseAction) {
        if (activity?.mouseAction(mouseAction) == true) {
            activity?.render()
        }
    }

    // Force a rerender without input
    fun render() {
        activity?.render()
    }

}


fun main(args: Array<String>) {


    val terminal = TerminalBuilder.newBuilder()
            .title(GameInfo.gameName)
            .initialTerminalSize(Size.of(GameInfo.xSize, GameInfo.ySize))
            .font(CP437TilesetResource.REX_PAINT_20X20.toFont())
            .build()


    val game = SandGame(terminal)
    game.activity = MenuActivity(game)
    game.render()

//    val theme = ColorThemeResource.ADRIFT_IN_DREAMS.getTheme()
//
//    val image = TextImageBuilder.newBuilder()
//            .size(terminal.getBoundableSize())
//            .filler(TextCharacterBuilder.newBuilder()
//                    .foregroundColor(theme.getBrightForegroundColor())
//                    .backgroundColor(theme.getBrightBackgroundColor())
//                    .character('\u2588')
//                    .build())
//            .build()
//    screen.draw(image, Position.DEFAULT_POSITION)


    terminal.onInput(Consumer { input ->
        if (input.isKeyStroke()) {
            val keyStroke = input.asKeyStroke()
            // do something with the key stroke
            println(keyStroke.getCharacter())
            game.keyPress(keyStroke)

        } else if (input.isMouseAction()) {
            // do something with the mouse action
//            val mouseAction = input.asMouseAction()
//            println(mouseAction.position)
            game.mouseAction(input.asMouseAction())
        }
    })

}
