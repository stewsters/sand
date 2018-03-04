package com.stewsters.sand.game.enums

import com.stewsters.sand.display.Appearance
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.TextColorFactory


enum class TileType(val material: Material, val floor: Boolean, val wall: Boolean, val appearance: TextCharacter? = null) {

    UNFINISHED(Material.AIR, false, false, TextCharacterBuilder.newBuilder()
            .character('*')
            .backgroundColor(Appearance.purple)
            .build()),

    AIR(Material.AIR, false, false),

    FLOOR(Material.STONE, true, false, TextCharacterBuilder.newBuilder()
            .character('.')
            .build()),

    WALL(Material.STONE, true, true, TextCharacterBuilder.newBuilder()
            .character('#')
            .foregroundColor(TextColorFactory.fromString("#F0E68C"))
            .backgroundColor(TextColorFactory.fromString("#BDB76B"))
            .build()),

    SAND_FLOOR(Material.SAND, true, false, TextCharacterBuilder.newBuilder()
            .character(',')
            .foregroundColor(TextColorFactory.fromString("#EDC9AF"))
            .backgroundColor(TextColorFactory.fromString("#C2B280"))
            .build())


    // sarcophagus_sealed
    // sarcophagus_open

}