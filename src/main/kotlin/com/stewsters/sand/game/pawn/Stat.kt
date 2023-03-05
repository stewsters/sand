package com.stewsters.sand.game.pawn

class Stat(var cur: Int, val max: Int) {
    fun decrease(i: Int) {
        cur -= i
    }

    fun recover(i: Int) {
        cur = Integer.min(max, cur + i)
    }
}