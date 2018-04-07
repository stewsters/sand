package com.stewsters.sand.game.map

import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.math.Matrix3d
import com.stewsters.sand.game.math.Vec3
import com.stewsters.sand.game.pawn.Pawn
import java.util.*


class World(
        val player: Pawn,
        val map: Matrix3d<Chunk>) {

    val light: Matrix3d<Double>
    val lightUpdate: Matrix3d<Int>
    var lightTurn: Int = 0

    // for ones who can do something
    val pawnQueue: PriorityQueue<Pawn>
    val pawnList: MutableList<Pawn>

    init {
        pawnQueue = PriorityQueue()
        pawnList = mutableListOf()

        light = Matrix3d(getXSize(), getYSize(), getZSize(), { x, y, z -> 0.0 })
        lightUpdate = Matrix3d(getXSize(), getYSize(), getZSize(), { x, y, z -> 0 })

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
        pawnList.add(pawn)
        if (pawn.turnTaker != null)
            pawnQueue.add(pawn)
        val x = pawn.pos.x
        val y = pawn.pos.y
        val z = pawn.pos.z

        map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)] = pawn
    }

    fun removePawn(pawn: Pawn) {
        pawnList.remove(pawn)
        pawnQueue.remove(pawn)

        val x = pawn.pos.x
        val y = pawn.pos.y
        val z = pawn.pos.z

        map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)] = null
    }

    fun movePawn(pawn: Pawn, next: Vec3) {
        val x = pawn.pos.x
        val y = pawn.pos.y
        val z = pawn.pos.z

        map[chunk(x), chunk(y), chunk(z)].pawns[tile(x), tile(y), tile(z)] = null

        pawn.pos = next

        map[chunk(next.x), chunk(next.y), chunk(next.z)].pawns[tile(next.x), tile(next.y), tile(next.z)] = pawn
    }

    fun outside(p: Vec3): Boolean = outside(p.x, p.y, p.z)

    fun outside(x: Int, y: Int, z: Int): Boolean {
        return x < 0 || x >= getXSize()
                || y < 0 || y >= getYSize()
                || z < 0 || z >= getZSize()
    }

    fun contains(pos: Vec3): Boolean = !outside(pos)


    fun lightTurn() {
        lightTurn++
    }

    fun putLight(x: Int, y: Int, z: Int, amount: Double) {
        if (outside(x, y, z))
            return

        if (lightUpdate[x, y, z] == lightTurn) {
            light[x, y, z] += amount
        } else {
            light[x, y, z] = amount
            lightUpdate[x, y, z] = lightTurn
        }

    }

    fun getLight(pos: Vec3): Double = getLight(pos.x, pos.y, pos.z)

    fun getLight(x: Int, y: Int, z: Int): Double {
        if (z >= getZSize() - 2)
            return 1.0
        if (lightUpdate[x, y, z] == lightTurn)
            return light[x, y, z]
        else
            return 0.0
    }

}