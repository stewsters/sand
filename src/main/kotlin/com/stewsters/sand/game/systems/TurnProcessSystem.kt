package com.stewsters.sand.game.systems

import com.stewsters.sand.game.map.World

class TurnProcessSystem(val worldMap: World, val gravitySystem: GravitySystem = GravitySystem(worldMap)) {

    fun process() {

        while (worldMap.player.health.cur > 0) {

            // Break early if there is no one to work on
            if (worldMap.pawnQueue.size <= 0)
                return

            var currentPawn = worldMap.pawnQueue.peek()
            var action = currentPawn.getAction()

            if (action == null) {
                gravitySystem.process()
                return  // Looks like the player needs to choose what to do
            }

            // Do action until it succeeds
            while (true) {
                val result = action!!.onPerform(worldMap, currentPawn)

                // if it was not successful, then
                if (!result.succeeded) {
                    if (currentPawn.aiControl != null)
                        break
                    return
                }

                if (result.alternative == null) {
                    currentPawn.nextAction = result.nextAction
                    break
                }

                action = result.alternative
            }

            currentPawn = worldMap.pawnQueue.poll()
            // increment time,
            currentPawn.gameTurn = currentPawn.gameTurn + 100
            worldMap.pawnQueue.add(currentPawn)
        }
    }
}
