package com.stewsters.sand.ecs

class Entity(val id: Int)

abstract class Component()

// probably should be singletons - nah, multiple stations
interface System {

    fun process()
}


data class LifeComponent(var hp: Int) : Component()
data class DamageComponent(var dam: Int) : Component()


class DamageSystem() : System {

    override fun process() {

    }
}


//class

fun main(str: Array<String>) {


}