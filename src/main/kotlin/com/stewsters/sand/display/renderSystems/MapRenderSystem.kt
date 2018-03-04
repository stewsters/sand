package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.map.World
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.screen.Screen

class MapRenderSystem(val region: World) {


    fun process(world: World, screen: Screen) {


        val playerPos = world.player.pos

        val xSize = screen.getBoundableSize().columns
        val ySize = screen.getBoundableSize().rows
        (0 until ySize).forEach { y ->
            (0 until xSize).forEach { x ->

                val worldX = x - xSize / 2 + playerPos.x
                val worldY = y - ySize / 2 + playerPos.y
                val worldZ = playerPos.z

                var textCharacter: TextCharacter = Appearance.darkness
                var tint = 1.0
                for (down in 0 until 4) {

                    val tz = worldZ - down
                    if (world.outside(worldX, worldY, worldZ))
                        break

                    val pawn = world.pawnAt(worldX, worldY, tz)

                    if (pawn != null) {
                        // TODO: show that they are 'down' units
                        textCharacter = pawn.appearance
                        tint = 1.0 / (tz + 1.0)
                        break;
                    }

                    val appearance = world.getCellTypeAt(worldX, worldY, worldZ).appearance
                    if (appearance != null) {
                        textCharacter = appearance
                        tint = 1.0 / (tz + 1.0)
                        break;
                    }
                }
                screen.setCharacterAt(Position.of(x, ySize - y - 1), textCharacter)
            }
        }
//
//
//        var point = region.player.pos.current
//
//        val zLevel = point.z
//
//
//        //TODO: use getWidth and calculate how many tiles are visible
//
//        val lowX = Math.max(point.x - 15, 0)
//        val highX = Math.min(point.x + 15, region.getXSize())
//
//        val lowY = Math.max(point.y - 15, 0)
//        val highY = Math.min(point.y + 15, region.getYSize())
//
//
//        for (x in lowX until highX) {
//            for (y in lowY until highY) {
//
//
//                var zDown = 0
//                for (zD in 0..9) {
//
//                    val tz = zLevel - zD
//                    if (tz < 0) {
//                        break
//                    }
//
//                    zDown = zD
//                    val tileType = region.getCellTypeAt(x, y, tz)
//                    if (tileType.appearance != null) {
//                        val tint = 1f / (zD + 1f)
////                        spriteBatch.setColor(tint, tint, tint, 1f)
////                        spriteBatch.draw(tileType.texture, x.toFloat(), y.toFloat(), 1f, 1f)
//                        break
//                    }
//                }
//
//                // now draw sprite from bottom to top
//                for (zMod in zDown downTo 0) {
//                    val pawn = region.pawnAt(x, y, zLevel - zMod)
//                    if (pawn != null) {
//                        val tint = 1f / (zMod + 1f)
////                        spriteBatch.setColor(tint, tint, tint, 1f)
////                        spriteBatch.draw(pawn.appearance, pawn.pos.getRenderedX(), pawn.pos.getRenderedY(), 1f, 1f)
//                    }
//                }
//
//            }
//        }
    }

}