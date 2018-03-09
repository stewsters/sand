package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Facing
import com.stewsters.sand.game.math.Vec3
import org.codetome.zircon.api.TextCharacter

class Pawn(
        var name: String,
        var pos: Vec3,
        val appearance: TextCharacter,
        val health: Health,
        var gameTurn: Long,
        var aiControl: AiControl? = null,
        var nextAction: Action? = null,
        var facing: Facing? = Facing.UP,
        var inventory: Inventory? = null,
        var lightProducer: LightProducer? = null
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

    fun canCatch(worldMap: World): Boolean {

        // if we are on a rope, then we are caught
        if (worldMap.getCellTypeAt(pos) == TileType.ROPE) {
            return true
        }

        for (point in pos.vonNeumanNeighborhood2d()) {
            if (worldMap.getCellTypeAt(point).isGrippable)
                return true
        }

        // if we have a ledge above us, then we can catch onto the edge

        // if are between 2 walls, we could do stemming

        return false

    }

}