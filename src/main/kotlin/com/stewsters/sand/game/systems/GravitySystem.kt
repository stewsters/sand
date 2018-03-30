package com.stewsters.sand.game.systems

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Facing
import com.stewsters.sand.game.pawn.Pawn
import java.util.*

class GravitySystem(val worldMap: World) {

    fun process() {
        val toDelete = ArrayList<Pawn>()

        for (pawn in worldMap.pawnList) {

            var fall = 0

            var next = pawn.pos.down()
            while (true) {

                if (worldMap.outside(next)
                        || worldMap.getCellTypeAt(pawn.pos).floor
                        || worldMap.getCellTypeAt(next).wall) {
                    // impact
                    break
                }

                if (pawn.canCatch?.invoke(worldMap, pawn.pos) == true) {
                    println("Caught edge")
                    if (!worldMap.getCellTypeAt(pawn.pos + pawn.facing!!).isGrippable) {
                        for (facing in Facing.values()) {
                            if (worldMap.getCellTypeAt(pawn.pos + facing).isGrippable) {
                                println("facing changed to $facing")
                                pawn.facing = facing
                                break
                            }
                        }
                    }

                    break
                }


                worldMap.movePawn(pawn, next)

                fall++
                next = pawn.pos.down()
            }

            if (fall > 3 && pawn.health != null) {
                pawn.health.damage(fall - 3)
                println("You fell ${fall * 8} feet.")
                if (pawn.health.cur <= 0) {
                    toDelete.add(pawn)
                }
            }

        }
        for (pawn in toDelete) {
            println("Removed ${pawn.name}")
            worldMap.removePawn(pawn)
        }
    }

}