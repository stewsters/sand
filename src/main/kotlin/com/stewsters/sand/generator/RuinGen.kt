package com.stewsters.sand.generator

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.map.Chunk
import com.stewsters.sand.game.map.Tile
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.pawn.DangerNoodleAi
import com.stewsters.sand.game.pawn.Health
import com.stewsters.sand.game.pawn.Inventory
import com.stewsters.sand.game.pawn.Pawn
import com.stewsters.sand.game.pawn.TurnTaker
import kaiju.math.Facing
import kaiju.math.Matrix3d
import kaiju.math.Vec3
import kaiju.math.getManhattanDistance
import kaiju.pathfinder.findPath3d
import org.codetome.zircon.api.builder.TextCharacterBuilder
import java.io.File
import java.util.*

object RuinGen {

//    val groundHeight = 2
//    val start = Vec3[10, 10, groundHeight]

    val maxChunks = Vec3[2, 2, 2]
    val chunkSize = Vec3[32, 32, 32]

    val maxSize = Vec3[maxChunks.x * chunkSize.y, maxChunks.y * chunkSize.y, maxChunks.z * chunkSize.z]
    fun gen(): World {

        val groundHeight = maxSize.z / 2

        val r = Random()

        val worldMap = World(
                player = Pawn(
                        name = "Player",
                        pos = Vec3[maxSize.x / 2, maxSize.y / 2 - 10, groundHeight],
                        health = Health(100, 100),
                        appearance = Appearance.player,
                        turnTaker = TurnTaker(gameTurn = 0),
                        inventory = Inventory(4, 6),
                        canCatch = { worldMap: World, pos: Vec3 ->
                            // if we are on a rope, then we are caught
                            (worldMap.getCellTypeAt(pos) == TileType.ROPE) || pos.vonNeumanNeighborhood2d().any { worldMap.getCellTypeAt(it).isGrippable }
                        }
                ),
                map = Matrix3d(maxChunks, { x, y, z ->
                    Chunk(
                            tiles = Matrix3d(chunkSize, { x, y, z -> Tile(TileType.UNFINISHED) }),
                            pawns = Matrix3d(chunkSize, { x, y, z -> null })
                    )
                })
        )

        // Modify the world

        (0 until maxSize.x).forEach { x ->
            (0 until maxSize.y).forEach { y ->
                (groundHeight until maxSize.z).forEach { z ->
                    worldMap.setCellTypeAt(x, y, z, TileType.AIR)
                }
                worldMap.setCellTypeAt(x, y, groundHeight, TileType.SAND_FLOOR)
            }
        }

        // Dig an entry chasm
        for (x in -1..1) {
            for (y in (-1..1)) {
                for (z in (-5..0)) {
                    worldMap.setCellTypeAt(maxSize.x / 2 + x, maxSize.y / 2 + y, groundHeight + z, TileType.AIR)
                }
            }
        }

        println("Loading Rooms")
        // load in directory of rooms
        val mapChunkList = File("chunks").listFiles()
                .filter { it.name.endsWith(".cnk") }
                .map { file ->
                    val (metadata, mapData) = file.readText().split('\n')

                    val dimensions = metadata.split(" ").map { it.toInt() }
                    val xSize = dimensions[0]
                    val ySize = dimensions[1]
                    val zSize = dimensions[2]

                    val map = Matrix3d(xSize, ySize, zSize, { x, y, z ->
                        Tile(TileType.UNFINISHED)
                    })

                    mapData.toCharArray()
                            .map { TileType.values().get(it.toInt() - 65) }
                            .forEachIndexed { i, tile ->
                                val x = i % xSize
                                val y = (i % (xSize * ySize)) / xSize
                                val z = i / (xSize * ySize)
                                map[x, y, z].type = tile
                            }
                    map
                }


        // loop through, sliding the rooms to the center. Probably start with the bottom
        println("Placing Rooms")
        val placedRoomCenters = mutableListOf<Vec3>()
        placedRoomCenters.add(Vec3[maxSize.x / 2, maxSize.y / 2, groundHeight - 5])

        (0 until groundHeight - 1).forEach { z ->
            (0..10).forEach {

                val room = mapChunkList[r.nextInt(mapChunkList.size)]
                //TODO: arbitrary rotation and reflection

                var placement = Vec3[
                        worldMap.getXSize() / 2, // r.nextInt(worldMap.getXSize() - room.xSize),
                        worldMap.getYSize() / 2, // r.nextInt(worldMap.getYSize() - room.ySize),
                        z]

                val shift = Vec3[
                        r.nextInt(7) - 3,
                        r.nextInt(7) - 3,
                        0
                ]
                if (shift != Vec3[0, 0, 0]) {


                    while (true) {
                        if (placement.x < 0 || placement.y < 0 || placement.z < 0 ||
                                placement.x + room.xSize >= worldMap.getXSize() ||
                                placement.y + room.ySize >= worldMap.getYSize() ||
                                placement.z + room.zSize >= worldMap.getZSize()) {
                            break
                        }
                        if (attemptPlacement(worldMap, room, placement)) {
                            placedRoomCenters.add(Vec3[placement.x + room.xSize / 2, placement.y + room.ySize / 2, placement.z])
                            break
                        }
                        placement += shift
                    }
                }
//                println(placedRoomCenters)


            }
        }

        println("Connecting")
        placedRoomCenters.groupBy { it.z }.forEach {
            val list = it.value.sortedBy { it.x }
            println(it.key)

            for (i in 0 until list.size - 1) {
                val curRoom = list[i]
                val nextRoom = list[i + 1]

                if (curRoom.z != nextRoom.z)
                    continue

                findPath3d(
                        maxSize,
                        { worldMap.getCellTypeAt(it).cost },
                        { p1, p2 -> 0.25 * getManhattanDistance(p1, p2) },
                        { it.vonNeumanNeighborhood2d().filter { worldMap.contains(it) } },
                        curRoom,
                        nextRoom
                )?.forEach {

                    val type = worldMap.getCellTypeAt(it)
                    if (type == TileType.UNFINISHED || type.wall) {
                        worldMap.setCellTypeAt(it, TileType.FLOOR)
                    }

                }

            }

        }

        // A* to maintain connectivity.  Will need to account for climbing, ideally we want it to be necessary to find
        // All the treasure


        println("Finishing")
        // Any unknown should become sand
        for (z in (0 until maxSize.z)) {
            for (y in (0 until maxSize.y)) {
                for (x in (0 until maxSize.x)) {
                    val tileType = worldMap.getCellTypeAt(x, y, z)

                    if (tileType == TileType.UNFINISHED) {
                        worldMap.setCellTypeAt(x, y, z, TileType.WALL)
                    }
                }
            }
        }


        // exposed wall should be floor
        for (z in (0 until maxSize.z)) {
            for (y in (0 until maxSize.y)) {
                for (x in (0 until maxSize.x)) {
                    val tileType = worldMap.getCellTypeAt(x, y, z)

                    if (tileType == TileType.AIR && worldMap.getCellTypeAt(x, y, z - 1).wall) {
                        worldMap.setCellTypeAt(x, y, z, TileType.FLOOR)
                    }
                }
            }
        }

//.forEachIndexed { index, vec3 ->
        worldMap.addPawn(Pawn(
                "Danger Noodle",
                worldMap.player.pos.plus(Facing.NORTH),
                TextCharacterBuilder.newBuilder().character('~').build(),
                Health(1, 1),
                TurnTaker((1).toLong()),
                DangerNoodleAi()
        ))
        //      }

        val playerArea = worldMap.getCellTypeAt(worldMap.player.pos)
        assert(playerArea.floor)
        assert(!playerArea.wall)


        return worldMap
    }

    private fun attemptPlacement(worldMap: World, room: Matrix3d<Tile>, placement: Vec3): Boolean {
        for (zMod in (0 until room.zSize)) {
            for (yMod in (0 until room.ySize)) {
                for (xMod in (0 until room.xSize)) {
                    if (worldMap.getCellTypeAt(placement.x + xMod, placement.y + yMod, placement.z + zMod) != TileType.UNFINISHED) {
                        return false
                    }
                }
            }
        }

        for (zMod in (0 until room.zSize)) {
            for (yMod in (0 until room.ySize)) {
                for (xMod in (0 until room.xSize)) {
                    worldMap.setCellTypeAt(placement.x + xMod, placement.y + yMod, placement.z + zMod, room[xMod, yMod, zMod].type)
                }
            }
        }
        return true
    }
}
