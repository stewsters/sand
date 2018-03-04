package com.stewsters.sand.game.actions

import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.Pawn

abstract class Action {
    abstract fun onPerform(world: World, pawn: Pawn): ActionResult
}


