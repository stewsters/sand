package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn
import kaiju.math.Facing

data class WalkAction(val dir: Facing) : Action() {
    override fun onPerform(world: World, pawn: Pawn): ActionResult {
        val nextPos = pawn.pos + dir

        pawn.facing = dir

        if (world.outside(nextPos)) {
            return ActionResult.FAILURE
        }

        if (world.getCellTypeAt(nextPos).wall) {
            //blocked
            return ActionResult.FAILURE
        }

        world.movePawn(pawn, nextPos)


        return ActionResult.SUCCESS
    }
}
