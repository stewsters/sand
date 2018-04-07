package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.game.map.World
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.screen.Screen

class HudRenderSystem {


    fun processSystem(gameMap: World, screen: Screen) {

        val playerHealth = gameMap.player.health
        if (playerHealth != null)
            drawLine(screen, 0, "HP: ${playerHealth.cur}")

        val inv = gameMap.player.inventory

        if (inv != null) {
            drawLine(screen, 1, "Torches: ${inv.torches}")
            drawLine(screen, 2, "Rope: ${inv.ropes}")
        }
    }

    fun drawLine(screen: Screen, height: Int, text: String) {
        text.forEachIndexed { index, c ->
            screen.setCharacterAt(Position.of(index, height), c)
        }

    }
}