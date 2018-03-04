package com.stewsters.sand.game.pawn

class Health(var cur: Int, val max: Int) {
    fun damage(i: Int) {
        cur -= i
    }
}