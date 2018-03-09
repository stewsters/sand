package com.stewsters.sand.game.light

import com.stewsters.sand.game.map.World


class LosEvaluator(private val map: World) : Evaluator3d {

    override fun isGood(sx: Int, sy: Int, sz: Int, tx: Int, ty: Int, tz: Int): Boolean {
        val startTileType = map.getCellTypeAt(sx, sy, sz)
        val targetTileType = map.getCellTypeAt(tx, ty, tz)

        if (sz > tz && startTileType.floor) {//go down
            return false
        }
        return if (sz < tz && targetTileType.floor) { // go up
            false
        } else !targetTileType.wall

    }
}
