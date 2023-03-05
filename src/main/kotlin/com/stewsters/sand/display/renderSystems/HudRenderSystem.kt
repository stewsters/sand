package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.game.map.World
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.data.Tile
import org.hexworks.zircon.api.graphics.StyleSet
import org.hexworks.zircon.api.screen.Screen

class HudRenderSystem {


    fun processSystem(gameMap: World, screen: Screen) {
        var line = 0
        drawLine( screen, line++, gameMap.player.name)

        val elevation = gameMap.player.pos.z
        drawLine(screen, line++, "Depth ${ -(32 -elevation )* 5}")

        gameMap.player.health?.let { playerHealth->
            drawLine(screen, line++, "HP: ${playerHealth.cur}")
        }

        gameMap.player.food?.let {foodHealth ->
            drawLine(screen, line++, "Fd: ${foodHealth.cur}")
        }

        gameMap.player.breath?.let { playerBreath->
            drawLine(screen, line++, "Br: ${playerBreath.cur}")
        }

        gameMap.player.inventory?.let { inv->
            drawLine(screen, line++, "Torches: ${inv.torches}")
            drawLine(screen, line++, "Rope: ${inv.ropes}")
        }
    }

    fun drawLine(screen: Screen, height: Int, text: String) {
        text.forEachIndexed { index, c ->
            screen.draw(Tile.createCharacterTile(c, StyleSet.defaultStyle()), Position.create(index, height))
        }

    }
}