package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Facing
import com.stewsters.sand.game.math.Vec3
import org.codetome.zircon.api.TextCharacter

class Pawn(
        var name: String,
        var pos: Vec3,
        val appearance: TextCharacter,
        val health: Health? = null,
        var turnTaker: TurnTaker? = null,
        var aiControl: AiControl? = null,
        var nextAction: Action? = null,
        var facing: Facing? = Facing.UP,
        var inventory: Inventory? = null,
        var lightProducer: LightProducer? = null,
        var canCatch: ((World, Vec3) -> Boolean)? = null
//        ,
//        var playerControl: PlayerControl? = null

) : Comparable<Pawn> {

    override fun compareTo(other: Pawn): Int = turnTaker?.gameTurn?.compareTo(other.turnTaker?.gameTurn ?: 0) ?: 0


    fun getAction(): Action? {
//        if (playerControl != null) {
//            val action = playerControl?.nextAction
//            playerControl?.nextAction = null
//            return action
//        } else
        if (nextAction != null)
            return nextAction
        if (aiControl != null) {
            return aiControl?.getNextAction()
        }
        return null
    }


}
