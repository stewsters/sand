package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.actions.WalkAction
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Facing
import com.stewsters.selene.game.action.AttackAction

interface AiControl {
    fun getNextAction(world: World, pawn: Pawn): Action
}

class DangerNoodleAi : AiControl {


    // gets map, and self
    override fun getNextAction(world: World, pawn: Pawn): Action {
        // find list of actions we can do
        // determine the value of doing each
        // do one

        val near = pawn.pos.vonNeumanNeighborhood2d()

        // if adjacent to player, bite them
        if (near.contains(world.player.pos)) {
            return AttackAction(pawn, world.player)
        }

        val facing = Facing.values().toList().shuffled().minBy { world.getLight(world.player.pos + it) }

        return WalkAction(facing ?: Facing.LEFT)

    }

}

//class