package com.stewsters.sand.game.light

import com.stewsters.sand.game.map.World


class ClearShotEvaluator(private val map: World) : Evaluator3d {

    override fun isGood(sx: Int, sy: Int, sz: Int, tx: Int, ty: Int, tz: Int): Boolean {
        val tileType = map.getCellTypeAt(tx, ty, tz)

        if (sz > tz && map.getCellTypeAt(sx, sy, sz).floor) {//go down
            return false
        }
        return if (sz < tz && tileType.floor) { // go up
            false
        } else !tileType.wall

    }
}
