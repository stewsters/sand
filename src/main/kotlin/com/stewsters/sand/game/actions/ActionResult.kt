package com.stewsters.sand.game.actions

class ActionResult(
        val succeeded: Boolean,
        val alternative: Action? = null,
        val nextAction: Action? = null
) {
    companion object {

        val SUCCESS = ActionResult(true)
        val FAILURE = ActionResult(false)
    }
}

