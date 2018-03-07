package com.stewsters.sand.generator

import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.map.Chunk
import com.stewsters.sand.game.map.Tile
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Matrix3d
import com.stewsters.sand.game.math.Vec3
import com.stewsters.sand.game.math.pathfinder.findPath3d
import com.stewsters.sand.game.pawn.Health
import com.stewsters.sand.game.pawn.Pawn
import java.io.File
import java.util.*

object RuinGen {

//    val groundHeight = 2
//    val start = Vec3[10, 10, groundHeight]

    val maxChunks = Vec3[4, 4, 2]
    val chunkSize = Vec3[32, 32, 32]

    val maxSize = Vec3[maxChunks.x * chunkSize.y, maxChunks.y * chunkSize.y, maxChunks.z * chunkSize.z]
    fun gen(): World {

        val r = Random()

        val worldMap = World(
                player = Pawn(
                        name = "Player",
                        pos = Vec3[maxSize.x / 2, maxSize.y / 2 - 10, maxSize.z - 2],
                        health = Health(100, 100),
                        appearance = Appearance.player,
                        gameTurn = 0
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
                //                worldMap.setCellTypeAt(x, y, 0, TileType.FLOOR)
                worldMap.setCellTypeAt(x, y, maxSize.z - 1, TileType.AIR)
                worldMap.setCellTypeAt(x, y, maxSize.z - 2, TileType.SAND_FLOOR)
            }
        }

        // Dig an entry chasm
        (-1..1).forEach { x ->
            (-1..1).forEach { y ->
                (-6..-1).forEach { z ->
                    worldMap.setCellTypeAt(maxSize.x / 2 + x, maxSize.y / 2 + y, maxSize.z + z, TileType.AIR)
                }
            }
        }

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

        val placedRoomCenters = mutableListOf<Vec3>()
        placedRoomCenters.add(Vec3[maxSize.x / 2, maxSize.y / 2, maxSize.z - 6])

        (0 until maxSize.z - 2).forEach { z ->
            (0..50).forEach {

                val room = mapChunkList[r.nextInt(mapChunkList.size)]
                // todo: arbitrary rotation and reflection

                var placement = Vec3[
                        r.nextInt(worldMap.getXSize() - room.xSize),
                        r.nextInt(worldMap.getYSize() - room.ySize),
                        z]


                if (attemptPlacement(worldMap, room, placement)) {
                    placedRoomCenters.add(Vec3[placement.x + room.xSize / 2, placement.y + room.ySize / 2, placement.z])
                }

                println(placedRoomCenters)


            }
        }

        // A* to maintain connectivity.  Will need to account for climbing, ideally we want it to be necessary to find
        // All the treasure

        for (i in 0 until placedRoomCenters.size - 1) {
            val curRoom = placedRoomCenters[i]
            val nextRoom = placedRoomCenters[i + 1]

            val path = findPath3d(maxSize,
                    { worldMap.getCellTypeAt(it).cost },
                    { it.vonNeumanNeighborhood2d().filter { worldMap.contains(it) } },
                    curRoom,
                    nextRoom)

            if (path != null) {
                println("Success $curRoom $nextRoom")
                path.forEach {
                    val type = worldMap.getCellTypeAt(it)
                    if (type == TileType.UNFINISHED || type.wall) {
                        worldMap.setCellTypeAt(it, TileType.FLOOR)
                    }
                }

            } else {
                println("Failed $curRoom $nextRoom")
            }

        }

        // Any unknown should become sand
        (0 until maxSize.z).forEach { z ->
            (0 until maxSize.y).forEach { y ->
                (0 until maxSize.x).forEach { x ->
                    val tileType = worldMap.getCellTypeAt(x, y, z)

                    if (tileType == TileType.UNFINISHED) {
                        worldMap.setCellTypeAt(x, y, z, TileType.WALL)
                    }
                }
            }
        }


        // exposed wall should be floor
        (0 until maxSize.z).forEach { z ->
            (0 until maxSize.y).forEach { y ->
                (0 until maxSize.x).forEach { x ->
                    val tileType = worldMap.getCellTypeAt(x, y, z)

                    if (tileType == TileType.AIR && worldMap.getCellTypeAt(x, y, z - 1).wall) {
                        worldMap.setCellTypeAt(x, y, z, TileType.FLOOR)
                    }
                }
            }
        }

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


//
//    private fun buildChunk(mapChunk: Chunk) {
//
//        val lotChoice = d(10)
//        if (lotChoice == 1) {
//            flattenWorld(mapChunk, DIRT_WALL, GRASS, ROAD_FLOOR, AIR)
//            constructPark(mapChunk)
//
//        } else if (lotChoice < 3) {
//            flattenWorld(mapChunk, DIRT_WALL, SIDEWALK_FLOOR, ROAD_FLOOR, AIR)
//            constructSkyscraper(mapChunk)
//
//        } else {
//            flattenWorld(mapChunk, DIRT_WALL, SIDEWALK_FLOOR, ROAD_FLOOR, AIR)
//            constructBuildings(mapChunk, 8)
//        }
//
//    }
//
//    private fun constructPark(mapChunk: Chunk) {
//        val lot = Rect(2, 2, Chunk.xSize - 3, Chunk.ySize - 3)
//
//        //        genBuilding(mapChunk, lot);
//    }
//
//
//    private fun constructSkyscraper(mapChunk: Chunk) {
//
//        val lot = Rect(2, 2, Chunk.xSize - 3, Chunk.ySize - 3)
//
//        genBuilding(mapChunk, lot)
//    }
//
//    private fun constructBuildings(mapChunk: Chunk, minSize: Int) {
//
//        // make a lot representing the whole, except for the street
//        val whole = Rect(2, 2, Chunk.xSize - 3, Chunk.ySize - 3)
//
//        val lots = RectSubdivider.divide(whole, minSize)
//
//        for (lot in lots) {
//            genBuilding(mapChunk, lot)
//        }
//
//    }
//
//    private fun genBuilding(mapChunk: Chunk, lot: Rect) {
//
//        // ySize in floors
//        val totalFloors = MatUtils.getIntInRange(2, Chunk.zSize - groundHeight - 2)
//
//        // This gives you the separation level around the base
//        val extendedWalk = MatUtils.getIntInRange(1, 2)
//
//
//        val foundation = Rect(
//                lot.x1 + extendedWalk,
//                lot.y1 + extendedWalk,
//                lot.x2 - extendedWalk,
//                lot.y2 - extendedWalk)
//
//
//        val doorDiggerMover = DoorDiggerMover(mapChunk, RectPrism(foundation.x1, foundation.y1, groundHeight, foundation.x2, foundation.y2, groundHeight + totalFloors))
//        val p = AStarPathFinder3d(mapChunk, 1000, false)
//
//        for (floor in 0 until totalFloors) {
//
//            val roomCenters = ArrayList<Point3i>()
//
//            val z = groundHeight + floor
//
//            solidLevel(mapChunk, foundation, z, CONCRETE_FLOOR)
//
//            if (MatUtils.d(5) !== 1) {
//                wallWithWindows(mapChunk, foundation, z, 1, 3, CONCRETE_WALL, GLASS)
//            } else {
//                wall(mapChunk, foundation, z, 1, CONCRETE_WALL)
//            }
//
//
//            val rooms = RectSubdivider.divide(foundation, d(4) + 2)
//            for (room in rooms) {
//
//                val center = room.center()
//                roomCenters.add(Point3i(center.x, center.y, z))
//
//                for (x in room.x1..room.x2) {
//                    for (y in room.y1..room.y2) {
//
//                        if (x == room.x1 || x == room.x2 + 1 || (y == room.y1 || y == room.y2 + 1) && mapChunk.tiles[x][y][z] === CONCRETE_FLOOR)
//                            mapChunk.tiles[x][y][z] = CONCRETE_WALL
//                    }
//                }
//            }
//            Collections.shuffle(roomCenters)
//
//
//            for (room1 in roomCenters) {
//                for (room2 in roomCenters) {
//                    val path = p.findPath(doorDiggerMover, room1.x, room1.y, room1.z, room2.x, room2.y, room2.z)
//
//                    if (path != null) {
//                        for (i in 1 until path!!.getLength()) {
//                            if (mapChunk.tiles[path!!.getX(i)][path!!.getY(i)][path!!.getZ(i)].blocks) {
//                                mapChunk.tiles[path!!.getX(i)][path!!.getY(i)][path!!.getZ(i)] = CLOSED_DOOR
//                            }
//
//                        }
//                    }
//                    // if we are going up, make a stairs
//                    //otherwise make door if solid
//                }
//            }
//
//
//        }
//
//        // Roof
//        solidLevel(mapChunk, foundation, groundHeight + totalFloors, SIDEWALK_FLOOR)
//
//        // stairs up
//        for (floor in 0 until totalFloors) {
//
//            val stairsX = MatUtils.getIntInRange(foundation.x1 + 1, foundation.x2 - 1)
//            val stairsY = MatUtils.getIntInRange(foundation.y1 + 1, foundation.y2 - 1)
//            val z = groundHeight + floor
//
//            mapChunk.tiles[stairsX][stairsY][z] = UP_STAIR
//            mapChunk.tiles[stairsX][stairsY][z + 1] = DOWN_STAIR
//        }
//
//        // Corners
//        val top = totalFloors + groundHeight - 1
//        fillColumn(mapChunk, foundation.x1, foundation.y1, groundHeight, top, CONCRETE_WALL)
//        fillColumn(mapChunk, foundation.x2, foundation.y1, groundHeight, top, CONCRETE_WALL)
//        fillColumn(mapChunk, foundation.x1, foundation.y2, groundHeight, top, CONCRETE_WALL)
//        fillColumn(mapChunk, foundation.x2, foundation.y2, groundHeight, top, CONCRETE_WALL)
//
//
//        //Doors
//        randDoor(mapChunk, MatUtils.getIntInRange(foundation.x1 + 1, foundation.x2 - 1), foundation.y1, groundHeight)
//        randDoor(mapChunk, MatUtils.getIntInRange(foundation.x1 + 1, foundation.x2 - 1), foundation.y2, groundHeight)
//        randDoor(mapChunk, foundation.x1, MatUtils.getIntInRange(foundation.y1 + 1, foundation.y2 - 1), groundHeight)
//        randDoor(mapChunk, foundation.x2, MatUtils.getIntInRange(foundation.y1 + 1, foundation.y2 - 1), groundHeight)
//
//
//    }
//
//    private fun randDoor(mapChunk: Chunk, x: Int, y: Int, z: Int) {
//        mapChunk.tiles[x][y][z] = if (MatUtils.getBoolean()) CLOSED_DOOR else OPEN_DOOR
//    }
//
//    private fun flattenWorld(mapChunk: Chunk, ground: TileType, border: TileType, road: TileType, air: TileType) {
//
//        for (x in 0 until Chunk.xSize) {
//            for (y in 0 until Chunk.ySize) {
//                for (z in 0 until Chunk.zSize) {
//                    val t: TileType
//
//                    if (z < groundHeight) {
//                        t = ground
//                    } else if (z == groundHeight) {
//                        if (x <= 1 || x >= Chunk.xSize - 2 || y <= 1 || y >= Chunk.ySize - 2)
//                            t = road
//                        else
//                            t = border
//
//                    } else {
//                        t = air
//                    }
//
//                    mapChunk.tiles[x][y][z] = t
//
//                }
//            }
//        }
//    }
//
//
//    private fun fillColumn(mapChunk: Chunk, x: Int, y: Int, z: Int, height: Int, tileType: TileType) {
//        fillBlock(mapChunk, RectPrism(x, y, z, x, y, height), tileType)
//    }
//
//
//    private fun fillBlock(mapChunk: Chunk, prism: RectPrism, tileType: TileType) {
//
//        for (x in prism.x1..prism.x2) {
//            for (y in prism.y1..prism.y2) {
//                for (z in prism.z1..prism.z2) {
//
//                    mapChunk.tiles[x][y][z] = tileType
//                }
//            }
//        }
//    }
//
//
//    private fun solidLevel(mapChunk: Chunk, prism: Rect, z: Int, tileType: TileType) {
//
//        for (x in prism.x1..prism.x2) {
//            for (y in prism.y1..prism.y2) {
//
//                mapChunk.tiles[x][y][z] = tileType
//            }
//        }
//    }
//
//    // Draws a wall around the area
//    private fun wall(mapChunk: Chunk, prism: Rect, z: Int, wallHeight: Int, tileType: TileType) {
//
//        for (x in prism.x1..prism.x2) {
//            for (y in prism.y1..prism.y2) {
//                for (zp in 0 until wallHeight) {
//
//                    if (x == prism.x1 || x == prism.x2 || y == prism.y1 || y == prism.y2)
//                        mapChunk.tiles[x][y][z + zp] = tileType
//                }
//            }
//        }
//    }
//
//
//    // Draws a wall around the area
//    private fun wallWithWindows(mapChunk: Chunk, prism: Rect, z: Int, wallHeight: Int, windowSpacing: Int, wallType: TileType, windowType: TileType) {
//
//        for (x in prism.x1..prism.x2) {
//            for (y in prism.y1..prism.y2) {
//                for (zp in 0 until wallHeight) {
//
//                    if (x == prism.x1 || x == prism.x2) {
//                        if ((y - prism.y1) % windowSpacing === 0) {
//                            mapChunk.tiles[x][y][z + zp] = windowType
//                        } else {
//                            mapChunk.tiles[x][y][z + zp] = wallType
//                        }
//                    } else if (y == prism.y1 || y == prism.y2) {
//                        if ((x - prism.x1) % windowSpacing === 0) {
//                            mapChunk.tiles[x][y][z + zp] = windowType
//                        } else {
//                            mapChunk.tiles[x][y][z + zp] = wallType
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    fun populate(worldMap: World): World {
//
//
//        // Set up player
//        val px = worldMap.getXSize() / 2
//        val py = worldMap.getYSize() / 2
//        val pz = groundHeight
//
//        addPlayer(worldMap, px, py, pz)
//
//
//        for (i in 0..29) {
//            val x = d(worldMap.getXSize()) - 1
//            val y = d(worldMap.getYSize()) - 1
//            val z = groundHeight
//
//            if (!worldMap.getCellTypeAt(x, y, z).blocks) {
//                addEnemy(worldMap, x, y, z)
//            }
//        }
//
//
//        for (i in 0..29) {
//            val x = d(worldMap.getXSize()) - 1
//            val y = d(worldMap.getYSize()) - 1
//            val z = groundHeight
//
//            if (!worldMap.getCellTypeAt(x, y, z).blocks) {
//                addDog(worldMap, x, y, z)
//            }
//        }
//
//        return worldMap
//    }
//
//
//    fun addPlayer(  x: Int, y: Int, z: Int): World {
//
//        val pawn = Pawn(
//                name = "Player",
//                pos = Position(Vec3[x, y, z]),
//                health = Health(1, 1),
//                appearance = Appearance(TextureManager.player),
////                aiControl = AiControl(),
//                playerControl = PlayerControl(),
//                getAction = {  WalkAction(Facing.RIGHT) },
//                gameTurn = 0
//        )
//
//
////        player.appearance = Appearance(TextureManager.player)
////        player.doorOpener = true
////        player.shooter = true
////        player.smasher = true
//
//        Gdx.input.inputProcessor = pawn.playerControl
//
//        worldMap.addPawn(pawn)
//        worldMap.player = pawn
//
//        return worldMap
//
//    }
//
//    fun addEnemy(worldMap: World, x: Int, y: Int, z: Int): World {
//
//        val pawn = Pawn(
//                name = "Dog",
//                pos = Position(Vec3[x, y, z]),
//                health = Health(1, 1),
//                appearance = Appearance(TextureManager.soldier),
//                aiControl = AiControl(),
//                getAction = { WalkAction( Facing.RIGHT) },
//                gameTurn = 1
//        )
//
//        worldMap.addPawn(pawn)
//        return worldMap
//
//    }
//
//
//    fun addDog(worldMap: World, x: Int, y: Int, z: Int): World {
//
//        val pawn = Pawn(
//                name = "Dog",
//                pos = Position(Vec3[x, y, z]),
//                health = Health(1, 1),
//                appearance = Appearance(TextureManager.dog),
//                aiControl = AiControl(),
//                gameTurn = 1,
//                getAction = { WalkAction( Facing.RIGHT) }
//        )
////        pawn.doorOpener = false
////        pawn.chaser = true
////        pawn.shooter = false
////        pawn.snipe = Snipe()
//
//        worldMap.addPawn(pawn)
//        return worldMap
//
//    }

}
