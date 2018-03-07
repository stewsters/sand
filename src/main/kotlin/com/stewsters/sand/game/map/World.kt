package com.stewsters.sand.game.map

import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.math.Matrix3d
import com.stewsters.sand.game.math.Vec3
import com.stewsters.sand.game.pawn.Pawn
import java.util.*


class World(
        val player: Pawn,
        val map: Matrix3d<Chunk>,
        val pawnQueue: PriorityQueue<Pawn> = PriorityQueue<Pawn>()) {

    init {
//        pawnQueue.add(player)
        addPawn(player)
    }

    private fun tile(globalCoord: Int): Int {
        return globalCoord % Chunk.size
    }

    private fun chunk(globalCoord: Int): Int {
        return globalCoord / Chunk.size
    }

    fun getXSize() = map.xSize * Chunk.size
    fun getYSize() = map.ySize * Chunk.size
    fun getZSize() = map.zSize * Chunk.size


    fun getCellTypeAt(p: Vec3): TileType = getCellTypeAt(p.x, p.y, p.z)
    fun getCellTypeAt(x: Int, y: Int, z: Int): TileType {
        return map[chunk(x), chunk(y), chunk(z)].tiles[tile(x), tile(y), tile(z)].type
    }

    fun setCellTypeAt(p: Vec3, type: TileType) = setCellTypeAt(p.x, p.y, p.z, type)
    fun setCellTypeAt(x: Int, y: Int, z: Int, type: TileType) {
        map[chunk(x), chunk(y), chunk(z)].tiles[tile(x), tile(y), tile(z)].type = type
    }

    fun pawnAt(p: Vec3): Pawn? = pawnAt(p.x, p.y, p.z)
    fun pawnAt(x: Int, y: Int, z: Int): Pawn? {
        return map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)]
    }

    fun addPawn(pawn: Pawn) {
        pawnQueue.add(pawn)
        val x = pawn.pos.x
        val y = pawn.pos.y
        val z = pawn.pos.z

        map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)] = pawn
    }

    fun removePawn(pawn: Pawn) {
        pawnQueue.remove(pawn)

        val x = pawn.pos.x
        val y = pawn.pos.y
        val z = pawn.pos.z

        map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)] = null
    }

    fun movePawn(pawn: Pawn, next: Vec3) {
        removePawn(pawn)
        pawn.pos = next
        addPawn(pawn)
    }

    fun outside(p: Vec3): Boolean = outside(p.x, p.y, p.z)

    fun outside(x: Int, y: Int, z: Int): Boolean {
        return x < 0 || x >= getXSize()
                || y < 0 || y >= getYSize()
                || z < 0 || z >= getZSize()
    }

    fun contains(pos: Vec3): Boolean = !outside(pos)

}