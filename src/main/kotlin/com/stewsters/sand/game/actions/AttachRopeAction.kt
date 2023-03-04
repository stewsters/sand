package com.stewsters.sand.game.actions

import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn
import kaiju.math.Vec3

class AttachRopeAction : Action() {

    override fun onPerform(world: World, pawn: Pawn): ActionResult {

        val inventory = pawn.inventory
        if (inventory == null) {
            return ActionResult.FAILURE
        }

        if (inventory.ropes <= 0) {
            println("No torches")
            return ActionResult.FAILURE
        }

        val next = pawn.pos + pawn.facing!!

        if (world.getCellTypeAt(next).wall) {
            return ActionResult.FAILURE
        }

        inventory.ropes--


        for (it in 0..5) {

            val pos = Vec3(next.x, next.y, next.z - it)
            if (world.getCellTypeAt(pos).wall)
                break

            world.setCellTypeAt(pos, TileType.ROPE)

        }

        return ActionResult.SUCCESS
    }


}
