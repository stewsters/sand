package com.stewsters.sand.display

import org.hexworks.zircon.api.color.ANSITileColor
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile

class Appearance {

    companion object {

        var white = ANSITileColor.WHITE //TextColorFactory.fromString("#ffffff")
        var black = ANSITileColor.BLACK //TextColorFactory.fromString("#000000")
        val purple = TileColor.fromString("#9932CC")
        val green = TileColor.fromString("#11ff11")

        var player = Tile.newBuilder() // TextCharacterBuilder.newBuilder()
            .withCharacter('@')
            .withForegroundColor(white)
            .withBackgroundColor(black)
            .build()

        var mummy = Tile.newBuilder()
            .withCharacter('M')
            .withForegroundColor(white)
            .withBackgroundColor(black)
            .build()

        var nopeRope = Tile.newBuilder()
            .withCharacter('~')
            .withForegroundColor(green)
            .withBackgroundColor(black)
            .build()

        var ankh = Tile.newBuilder()
            .withCharacter('\u2625')
            .build()

        val darkness = Tile.newBuilder()
            .withCharacter(' ')
            .withForegroundColor(black)
            .build()


    }

}