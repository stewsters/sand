package com.stewsters.sand.display.renderSystems

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.map.World
import org.codetome.zircon.api.Position
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.TextColor
import org.codetome.zircon.api.color.TextColorFactory
import org.codetome.zircon.api.screen.Screen

class MapRenderSystem {


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

                    tint = 1.0 / (down + 1.0)
                    val pawn = world.pawnAt(worldX, worldY, tz)

                    if (pawn != null) {
                        textCharacter = pawn.appearance
                        break;
                    }

                    val appearance = world.getCellTypeAt(worldX, worldY, tz).appearance
                    if (appearance != null) {
                        textCharacter = appearance
                        break;
                    }
                }

                val tintedForeGround = darken(textCharacter.getForegroundColor(), tint)
                val tintedBackGround = darken(textCharacter.getBackgroundColor(), tint)

                screen.setCharacterAt(Position.of(x, ySize - y - 1), TextCharacterBuilder.newBuilder()
                        .character(textCharacter.getCharacter())
                        .foregroundColor(tintedForeGround)
                        .backgroundColor(tintedBackGround).build())
            }
        }
    }

    // This is not ideal, it creates a lot of garbage
    fun darken(textColor: TextColor, tint: Double): TextColor =
            TextColorFactory.fromRGB(
                    (textColor.getRed().toDouble() * tint).toInt(),
                    (textColor.getGreen().toDouble() * tint).toInt(),
                    (textColor.getBlue().toDouble() * tint).toInt()
            )


}