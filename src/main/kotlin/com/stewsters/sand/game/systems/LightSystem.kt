package com.stewsters.sand.game.systems

import com.stewsters.sand.game.map.World

class LightSystem {

    fun process(world: World) {

        world.lightTurn()
        world.pawnList
                .filter { it.lightProducer?.time ?: 0 > 0 }
                .forEach {
                    val radius = it.lightProducer!!.radius

                    for (x in (-radius..radius)) {
                        for (y in (-radius..radius)) {
                            for (z in (-radius..radius)) {

                                val distSquared = (x * x + y * y + z * z)

                                if (distSquared < radius * radius)
                                    world.putLight(it.pos.x + x, it.pos.y + y, it.pos.z + z, 1)
                            }
                        }
                    }
                }
    }
}
