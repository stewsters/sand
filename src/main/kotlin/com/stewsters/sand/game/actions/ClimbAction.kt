package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn

class ClimbAction : Action() {

    override fun onPerform(world: World, pawn: Pawn): ActionResult {
        val nextPos = pawn.pos.up()

//        pawn.facing = dir

        if (world.outside(nextPos)) {
            return ActionResult.FAILURE
        }

        if (world.getCellTypeAt(nextPos).wall) {
            //blocked
            return ActionResult.FAILURE
        }

        if (!pawn.canCatch(world)) {
            return ActionResult.FAILURE
        }

        if (nextPos.vonNeumanNeighborhood2d().none { world.getCellTypeAt(it).isGrippable }) {

            val above = pawn.pos.up()
            val mantlePoint = above + pawn.facing!!
            if (!world.getCellTypeAt(above).floor && world.getCellTypeAt(mantlePoint).floor) {
                world.movePawn(pawn, mantlePoint)
                return ActionResult.SUCCESS
            }

            return ActionResult.FAILURE
        }

        world.movePawn(pawn, nextPos)
        return ActionResult.SUCCESS
    }
}
