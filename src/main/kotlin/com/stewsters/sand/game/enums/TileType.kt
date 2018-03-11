package com.stewsters.sand.game.enums

import com.stewsters.sand.display.Appearance
import org.codetome.zircon.api.TextCharacter
import org.codetome.zircon.api.builder.TextCharacterBuilder
import org.codetome.zircon.api.color.TextColorFactory


enum class TileType(
        val material: Material,
        val floor: Boolean,
        val wall: Boolean,
        val cost: Double,
        val appearance: TextCharacter? = null,
        val isGrippable: Boolean = false) {

    UNFINISHED(Material.AIR, false, false, 1.0, TextCharacterBuilder.newBuilder()
            .character('*')
            .backgroundColor(Appearance.purple)
            .build()),

    AIR(Material.AIR, false, false, 3.0),

    FLOOR(Material.STONE, true, false, 0.25, TextCharacterBuilder.newBuilder()
            .character('.')
            .foregroundColor(TextColorFactory.fromString("#dddddd"))
            .backgroundColor(TextColorFactory.fromString("#999999"))
            .build()),

    WALL(Material.STONE, true, true, 5.0, TextCharacterBuilder.newBuilder()
            .character('#')
            .foregroundColor(TextColorFactory.fromString("#F0E68C"))
            .backgroundColor(TextColorFactory.fromString("#BDB76B"))
            .build(),
            true),

    SAND_FLOOR(Material.SAND, true, false, 0.25, TextCharacterBuilder.newBuilder()
            .character(',')
            .foregroundColor(TextColorFactory.fromString("#EDC9AF"))
            .backgroundColor(TextColorFactory.fromString("#C2B280"))
            .build()),

    SAND_WALL(Material.SAND, true, true, 4.0, TextCharacterBuilder.newBuilder()
            .character('~')
            .foregroundColor(TextColorFactory.fromString("#e0E080"))
            .backgroundColor(TextColorFactory.fromString("#BDB76B"))
            .build()

    ),

    ROPE(Material.AIR, false, false, 1.0, TextCharacterBuilder.newBuilder()
            .character('*')
            .build())

    // sarcophagus_sealed
    // sarcophagus_open

}