package com.stewsters.sand.game.systems

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn
import java.util.*

class GravitySystem(val worldMap: World) {

    fun processSystem() {
        val toDelete = ArrayList<Pawn>()

        for (pawn in worldMap.pawnQueue) {

            var fall = 0

            var next = pawn.pos.down()
            while (!(worldMap.outside(next)
                            || worldMap.getCellTypeAt(pawn.pos).floor
                            || worldMap.getCellTypeAt(next).material.blocks)) {

                worldMap.movePawn(pawn, next)

                fall++
                next = pawn.pos.down()
            }

            if (fall >= 2) {
                pawn.health.damage(fall - 1)
                if (pawn.health.cur <= 0) {
                    toDelete.add(pawn)
                }
            }

        }
        for (pawn in toDelete) {
            worldMap.removePawn(pawn)
        }
    }

}