package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.light.Bresenham3d
import com.stewsters.sand.game.light.LosEvaluator
import com.stewsters.sand.game.map.World
import kaiju.math.getEuclideanDistance
import kaiju.math.lerp
import kaiju.math.limit
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.TextColor
import org.codetome.zircon.api.color.TextColorFactory
import org.codetome.zircon.api.screen.Screen

class MapRenderSystem {


    fun process(world: World, screen: Screen) {

        val los = LosEvaluator(world)
        val playerPos = world.player.pos

        val xSize = screen.getBoundableSize().columns
        val ySize = screen.getBoundableSize().rows
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

                    var textCharacter: TextCharacter = Appearance.darkness
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

                    val tintedForeGround = darken(textCharacter.getForegroundColor(), Appearance.black, 0.0, brightness)
                    val tintedBackGround = darken(textCharacter.getBackgroundColor(), Appearance.black, 0.0, brightness)

                    screen.setCharacterAt(Position.of(x, ySize - y - 1), TextCharacterBuilder.newBuilder()
                            .character(textCharacter.getCharacter())
                            .foregroundColor(tintedForeGround)
                            .backgroundColor(tintedBackGround).build())
                } else {
                    screen.setCharacterAt(Position.of(x, ySize - y - 1), Appearance.darkness)
                }
            }
        }
    }

    // This is not ideal, it creates a lot of garbage
    private fun darken(textColor: TextColor, tintColor: TextColor, tint: Double, brightness: Double): TextColor {

        val r = lerp(tint, textColor.getRed().toDouble(), tintColor.getRed().toDouble())
        val g = lerp(tint, textColor.getGreen().toDouble(), tintColor.getGreen().toDouble())
        val b = lerp(tint, textColor.getBlue().toDouble(), tintColor.getBlue().toDouble())

        return TextColorFactory.fromRGB(
                (r * brightness).limit(0.0, 255.0).toInt(),
                (g * brightness).limit(0.0, 255.0).toInt(),
                (b * brightness).limit(0.0, 255.0).toInt()
        )
    }

}
