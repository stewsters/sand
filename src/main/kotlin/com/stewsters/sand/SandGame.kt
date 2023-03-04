package com.stewsters.sand

import com.stewsters.sand.display.activities.Activity
import com.stewsters.sand.display.activities.MenuActivity
import org.hexworks.zircon.api.CP437TilesetResources.rexPaint16x16
import org.hexworks.zircon.api.SwingApplications.startTileGrid
import org.hexworks.zircon.api.application.AppConfig
import org.hexworks.zircon.api.grid.TileGrid
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.KeyboardEventType
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventType
import org.hexworks.zircon.api.uievent.UIEventPhase
import org.hexworks.zircon.api.uievent.UIEventResponse


class SandGame(val tileGrid: TileGrid, var activity: Activity? = null) {


    fun keyPress(keyStroke: KeyCode) {
        activity?.keyPressed(keyStroke)
        activity?.render()

    }

    fun mouseAction(mouseAction: MouseEvent) {
        if (activity?.mouseAction(mouseAction) == true) {
            activity?.render()
        }
    }

    // Force a rerender without input
    fun render() {
        activity?.render()
    }

}


fun main() {

    val tileGrid = startTileGrid(
        AppConfig.newBuilder() // The number of tiles horizontally, and vertically
            .withTitle(GameInfo.gameName)
            .withSize(
                GameInfo.xSize,
                GameInfo.ySize
            ) // You can choose from a wide array of CP437, True Type or Graphical tilesets
            // that are built into Zircon
            .withDefaultTileset(rexPaint16x16())
            .build()
    )

    val game = SandGame(tileGrid)
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

    tileGrid.handleKeyboardEvents(KeyboardEventType.KEY_PRESSED) { event, phase ->

        // do something with the key press
        println(event.key)
        game.keyPress(event.code)
        UIEventResponse.processed()
    }

    tileGrid.handleMouseEvents(MouseEventType.MOUSE_RELEASED) { event: MouseEvent, phase: UIEventPhase ->
        // we log the event we received
        println(String.format("Mouse event was: %s.", event))
        game.mouseAction(event)
        UIEventResponse.processed()
    }


}
