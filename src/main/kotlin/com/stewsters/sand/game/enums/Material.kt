package com.stewsters.sand.game.enums

enum class Material(val blocks: Boolean) {

    // Game Types
    AIR(false),

    //    SNOW(true),
    WATER(false),
    STONE(true),
    SAND(true),
    WOOD(true),

    // Generation only types, these should be gone by the time the program starts.
    UNKNOWN(false)
}