package com.stewsters.selene.game.action

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.actions.ActionResult
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn

class AttackAction(pawn: Pawn, internal var target: Pawn) : Action() {
    override fun onPerform(world: World, pawn: Pawn): ActionResult {

        val health = target.health
        if(health!=null){
            health.damage(1)

            if (health?.cur <= 0) {
                world.removePawn(target)
            }

            return ActionResult.SUCCESS

        }
        return ActionResult.FAILURE

    }


}
