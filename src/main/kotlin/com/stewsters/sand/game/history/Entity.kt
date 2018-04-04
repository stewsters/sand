package com.stewsters.sand.game.history

import java.util.*


data class Person(
        val name: String = randomName(),
        val spouse: Person? = null) {
    override fun toString(): String = name
}

data class Group(val name: String = randomGroupName()) {
    override fun toString(): String = name
}

data class City(val name: String = randomCityName()) {
    override fun toString(): String = name
}

data class Event(val name: String, val prereqs: (World) -> Boolean, val effects: (World) -> String) {
    override fun toString(): String = name
}

data class World(
        val people: MutableList<Person> = mutableListOf<Person>(),
        val cities: MutableList<City> = mutableListOf<City>(),
        val groups: MutableList<Group> = mutableListOf<Group>()
)

val events = mutableListOf<Event>(
        Event("City Founded", { true }, {
            val city = City()
            it.cities.add(city)
            "${city} was founded."
        }),
        Event("Formed Group", { it.people.isNotEmpty() }, {
            val person = random(it.people)
            val group = Group()
            it.groups.add(group)
            "$person formed $group"
        }),
        Event("Invasion", { it.cities.isNotEmpty() }, {
            val city = random(it.cities)
            "${city} was invaded"
        }),
        Event("Cataclysm", { it.cities.isNotEmpty() }, {
            val city = random(it.cities)
            "$city was destroyed by a cataclysm"
        }),
//        Event("Parlay", { it.people.size > 1 }, {
//            "Todo"
//        }),

        Event("Marriage", { it.people.filter { it.spouse == null }.size >= 2 }, {
            // get 2 people
            val peeps = it.people.filter { it.spouse == null }.shuffled().slice(0..1)
            "${peeps[0]} married ${peeps[1]}"
        }),
        Event("Childbirth", { true }, {
            val child = Person()
            it.people.add(child)
            "Begat ${child}"
        })
)


fun main(args: Array<String>) {

    val world = World()

    val log = mutableListOf<String>()

    while (log.size < 10) {
        val event = random(events)
        if (event.prereqs(world))
            log.add(event.effects(world))
    }

    log.forEach {
        println(it)
    }


}

var i = 0
fun randomName(): String {
    return "Joe ${i++}"
}

var j = 0
fun randomCityName(): String {
    return "Joeoppolis ${j++}"
}

var k = 0
fun randomGroupName(): String {
    return "Adherants of ${k++}"
}

val random = Random()
fun <T> random(list: List<T>): T {
    val i = random.nextInt(list.size)
    return list[i]
}