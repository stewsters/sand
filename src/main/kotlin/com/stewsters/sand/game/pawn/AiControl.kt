package com.stewsters.sand.game.pawn

import com.stewsters.sand.game.actions.Action
import com.stewsters.sand.game.actions.WalkAction
import com.stewsters.sand.game.math.Facing

class AiControl {
    fun getNextAction(): Action {
        return WalkAction(Facing.UP)
    }
}