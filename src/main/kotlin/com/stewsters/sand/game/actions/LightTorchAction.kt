package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.LightProducer
import com.stewsters.sand.game.pawn.Pawn

class LightTorchAction : Action() {

    override fun onPerform(world: World, pawn: Pawn): ActionResult {

        val inventory = pawn.inventory
        if (inventory == null) {
            return ActionResult.FAILURE
        }

        if (inventory.torches <= 0) {
            println("No torches")
            return ActionResult.FAILURE
        }

        // if underwater?

        inventory.torches--

        pawn.lightProducer = LightProducer(time = 100, radius = 8)
        return ActionResult.SUCCESS
    }


}
