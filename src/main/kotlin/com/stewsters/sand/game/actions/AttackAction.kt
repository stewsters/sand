package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn

class AttackAction(pawn: Pawn, internal var target: Pawn) : Action() {
    override fun onPerform(world: World, pawn: Pawn): ActionResult {

        val health = target.health
        if (health != null) {
            health.decrease(1)

            if (health.cur <= 0) {
                world.removePawn(target)
            }

            return ActionResult.SUCCESS

        }
        return ActionResult.FAILURE

    }


}
