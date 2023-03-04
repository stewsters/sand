package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.light.Bresenham3d
import com.stewsters.sand.game.light.LosEvaluator
import com.stewsters.sand.game.map.World
import kaiju.math.getEuclideanDistance
import kaiju.math.lerp
import kaiju.math.limit
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.screen.Screen

class MapRenderSystem {


    fun process(world: World, screen: Screen) {

        val los = LosEvaluator(world)
        val playerPos = world.player.pos

        val xSize = screen.width
        val ySize = screen.height
        for (y in 0 until ySize) {
            for (x in 0 until xSize) {

                val worldX = x - xSize / 2 + playerPos.x
                val worldY = y - ySize / 2 + playerPos.y
                val worldZ = playerPos.z
                val dist = getEuclideanDistance(playerPos.x, playerPos.y, playerPos.z, worldX, worldY, worldZ)
                val lightLevel = world.getLight(worldX, worldY, worldZ)
                if (
                    playerPos.z >= world.getZSize() - 2 || //above the ground
                    dist < 2 ||
                    (lightLevel > 0 &&
                            Bresenham3d.open(playerPos.x, playerPos.y, playerPos.z, worldX, worldY, worldZ, los))
                ) {

                    var textCharacter = Appearance.darkness
                    var brightness = 1.0
                    for (down in 0 until 4) {

                        val tz = worldZ - down
                        if (world.outside(worldX, worldY, worldZ))
                            break

                        brightness = lightLevel / (down + 1.0)
                        val pawn = world.pawnAt(worldX, worldY, tz)

                        if (pawn != null) {
                            textCharacter = pawn.appearance
                            break
                        }

                        val appearance = world.getCellTypeAt(worldX, worldY, tz).appearance
                        if (appearance != null) {
                            textCharacter = appearance
                            break
                        }
                    }

                    // TODO use or remove?
                    val tintedForeGround = darken(textCharacter.foregroundColor, Appearance.black, 0.0, brightness)
                    val tintedBackGround = darken(textCharacter.backgroundColor, Appearance.black, 0.0, brightness)

                    screen.draw(
                        textCharacter.createCopy()
                            .withForegroundColor(tintedForeGround)
                            .withBackgroundColor(tintedBackGround),
                        Position.create(x, ySize - y - 1)
                    )
                } else {
                    screen.draw(Appearance.darkness, Position.create(x, ySize - y - 1))
                }
            }
        }
    }

    // This is not ideal, it creates a lot of garbage
    private fun darken(textColor: TileColor, tintColor: TileColor, tint: Double, brightness: Double): TileColor {

        val r = lerp(tint, textColor.red.toDouble(), tintColor.red.toDouble())
        val g = lerp(tint, textColor.green.toDouble(), tintColor.green.toDouble())
        val b = lerp(tint, textColor.blue.toDouble(), tintColor.blue.toDouble())

        return TileColor.create(
            (r * brightness).limit(0.0, 255.0).toInt(),
            (g * brightness).limit(0.0, 255.0).toInt(),
            (b * brightness).limit(0.0, 255.0).toInt()
        )
    }

}
