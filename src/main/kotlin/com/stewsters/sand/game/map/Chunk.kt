package com.stewsters.sand.game.map

import com.stewsters.sand.game.pawn.Pawn
import kaiju.math.Matrix3d

class Chunk(val tiles: Matrix3d<Tile>, val pawns: Matrix3d<Pawn?>) {

    companion object {
        const val size = 32
    }

    fun getXSize() = tiles.xSize
    fun getYSize() = tiles.ySize
    fun getZSize() = tiles.zSize

}