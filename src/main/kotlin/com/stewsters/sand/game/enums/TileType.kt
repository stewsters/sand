package com.stewsters.sand.game.enums

import com.stewsters.sand.display.Appearance
import org.hexworks.zircon.api.color.TileColor
import org.hexworks.zircon.api.data.Tile


enum class TileType(
    val material: Material,
    val floor: Boolean,
    val wall: Boolean,
    val cost: Double,
    val appearance: Tile? = null,
    val isGrippable: Boolean = false
) {

    UNFINISHED(
        Material.AIR, false, false, 1.0, Tile.newBuilder()
            .withCharacter('*')
            .withBackgroundColor(Appearance.purple)
            .build()
    ),

    AIR(Material.AIR, false, false, 3.0),

    FLOOR(
        Material.STONE, true, false, 0.25, Tile.newBuilder()
            .withCharacter('.')
            .withForegroundColor(TileColor.fromString("#dddddd"))
            .withBackgroundColor(TileColor.fromString("#999999"))
            .build()
    ),

    WALL(
        Material.STONE, true, true, 5.0, Tile.newBuilder()
            .withCharacter('#')
            .withForegroundColor(TileColor.fromString("#F0E68C"))
            .withBackgroundColor(TileColor.fromString("#BDB76B"))
            .build(),
        true
    ),

    SAND_FLOOR(
        Material.SAND, true, false, 0.25, Tile.newBuilder()
            .withCharacter(',')
            .withForegroundColor(TileColor.fromString("#EDC9AF"))
            .withBackgroundColor(TileColor.fromString("#C2B280"))
            .build()
    ),

    SAND_WALL(
        Material.SAND, true, true, 4.0, Tile.newBuilder()
            .withCharacter('~')
            .withForegroundColor(TileColor.fromString("#e0E080"))
            .withBackgroundColor(TileColor.fromString("#BDB76B"))
            .build()

    ),

    ROPE(
        Material.AIR, false, false, 1.0, Tile.newBuilder()
            .withCharacter('*')
            .build()
    )

    // sarcophagus_sealed
    // sarcophagus_open

}