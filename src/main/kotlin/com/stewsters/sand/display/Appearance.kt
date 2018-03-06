package com.stewsters.sand.display

import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.TextColorFactory

class Appearance {

    companion object {

        var white = TextColorFactory.fromString("#ffffff")
        var black = TextColorFactory.fromString("#000000")
        val purple = TextColorFactory.fromString("#9932CC")
        val green = TextColorFactory.fromString("#11ff11")

        var player = TextCharacterBuilder.newBuilder()
                .character('@')
                .foregroundColor(white)
                .backgroundColor(black)
                .build()

        var mummy = TextCharacterBuilder.newBuilder()
                .character('M')
                .foregroundColor(white)
                .backgroundColor(black)
                .build()

        var nopeRope = TextCharacterBuilder.newBuilder()
                .character('~')
                .foregroundColor(green)
                .backgroundColor(black)
                .build()

        var ankh = TextCharacterBuilder.newBuilder()
                .character('\u2625')
                .build()

        val darkness = TextCharacterBuilder.newBuilder()
                .character(' ')
                .foregroundColor(black)
                .build()


    }

}