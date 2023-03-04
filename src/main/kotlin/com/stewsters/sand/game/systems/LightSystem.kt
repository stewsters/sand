package com.stewsters.sand.game.systems

import com.stewsters.sand.game.map.World
import kotlin.math.sqrt

class LightSystem {

    fun process(world: World) {

        world.lightTurn()
        world.pawnList
            .filter { it.lightProducer?.time ?: 0 > 0 }
            .forEach {
                // TODO: if we are expired, light burned out and remove it


                val radius = it.lightProducer!!.radius

                for (x in -radius..radius) {
                    for (y in -radius..radius) {
                        for (z in -radius..radius) {

                            // Distance squared works better with lumens, but the human eye is more sensitive to
                            // dim changes, which kind of bends it back the other way
                            val dist = sqrt((x * x + y * y + z * z).toFloat())

                            if (dist < radius)
                                world.putLight(it.pos.x + x, it.pos.y + y, it.pos.z + z, 1.0 / dist)
                        }
                    }
                }
            }
    }
}
