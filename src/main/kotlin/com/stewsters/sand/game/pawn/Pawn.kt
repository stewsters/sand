package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.map.World
import kaiju.math.Facing
import kaiju.math.Vec3

import org.hexworks.zircon.api.data.Tile

class Pawn(
    var name: String,
    var pos: Vec3,
    val appearance: Tile,
    val health: Health? = null,
    var turnTaker: TurnTaker? = null,
    var aiControl: AiControl? = null,
    var nextAction: Action? = null,
    var facing: Facing? = Facing.NORTH,
    var inventory: Inventory? = null,
    var lightProducer: LightProducer? = null,
    var canCatch: ((World, Vec3) -> Boolean)? = null
//        ,
//        var playerControl: PlayerControl? = null

) : Comparable<Pawn> {

    override fun compareTo(other: Pawn): Int = turnTaker?.gameTurn?.compareTo(other.turnTaker?.gameTurn ?: 0) ?: 0


    fun getAction(world: World): Action? {
//        if (playerControl != null) {
//            val action = playerControl?.nextAction
//            playerControl?.nextAction = null
//            return action
//        } else
        if (nextAction != null)
            return nextAction
        if (aiControl != null) {
            return aiControl?.getNextAction(world, this)
        }
        return null
    }


}
