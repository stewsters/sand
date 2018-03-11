package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.LightProducer
import com.stewsters.sand.game.pawn.Pawn
import org.codetome.zircon.api.builder.TextCharacterBuilder

class DropTorchAction : Action() {

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

        world.addPawn(Pawn(
                name = "torch",
                pos = pawn.pos + pawn.facing!!,
                appearance = TextCharacterBuilder.newBuilder().character('t').build(),
                lightProducer = LightProducer(100, 6)
        ))

        return ActionResult.SUCCESS
    }


}
