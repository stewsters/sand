package com.stewsters.sand.game.math.pathfinder

import com.stewsters.sand.game.math.Matrix3d
import com.stewsters.sand.game.math.Vec3

fun findPath3d(
        size: Vec3,
        cost: (Vec3) -> Double,
        neighbors: (Vec3) -> List<Vec3>,
        start: Vec3,
        end: Vec3
): List<Vec3>? {

    val costs = Matrix3d(size.x, size.y, size.z, { _, _, _ -> Double.MAX_VALUE })
    costs[start] = 0.0
    val parent = Matrix3d<Vec3?>(size.x, size.y, size.z, { _, _, _ -> null })

    val openSet = mutableListOf<Vec3>()
    val closeSet = HashSet<Vec3>()

    openSet.add(start)
    while (openSet.isNotEmpty()) {

        // Grab the next node with the lowest cost
        val cheapestNode: Vec3 = openSet.minBy { costs[it] }!!

        if (cheapestNode == end) {
            // target found, we have a path
            val path = mutableListOf(cheapestNode)

            var node = cheapestNode
            while (parent[node] != null) {
                node = parent[node]!!
                path.add(node)
            }
            return path.reversed()
        }

        openSet.remove(cheapestNode)

        // get the neighbors
        // for each point, set the cost, and a pointer back if we set the cost
        neighbors(cheapestNode).forEach {
            val nextCost = costs[cheapestNode] + cost(it)

            if (nextCost < costs[it]) {
                costs[it] = nextCost
                parent[it] = cheapestNode


                if (closeSet.contains(it)) {
                    closeSet.remove(it)
                }
                openSet.add(it)
            }
        }

        closeSet.add(cheapestNode)
    }

    // could not find a path
    return null

}