package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.math.Vec3
import org.codetome.zircon.api.TextCharacter

class Pawn(
        var name: String,
        var pos: Vec3,
        val appearance: TextCharacter,
        val health: Health,
        var gameTurn: Long,
        var aiControl: AiControl? = null,
        var nextAction: Action? = null
//        ,
//        var playerControl: PlayerControl? = null

) : Comparable<Pawn> {

    override fun compareTo(other: Pawn): Int = gameTurn.compareTo(other.gameTurn)


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