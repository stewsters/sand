package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import com.stewsters.sand.display.Appearance
import com.stewsters.sand.game.enums.TileType
import com.stewsters.sand.game.map.Tile
import kaiju.math.Matrix3d
import kaiju.math.Vec3
import kaiju.math.matrix3dOf

import org.hexworks.zircon.api.builder.screen.ScreenBuilder
import org.hexworks.zircon.api.data.Position
import org.hexworks.zircon.api.screen.Screen
import org.hexworks.zircon.api.uievent.KeyCode
import org.hexworks.zircon.api.uievent.MouseEvent
import org.hexworks.zircon.api.uievent.MouseEventType
import java.io.File
import java.util.*
import kotlin.math.max
import kotlin.math.min


class CreateMapActivity(var game: SandGame) : Activity {

    private var screen: Screen
    val xScreenSize: Int
    val yScreenSize: Int

    var map: Matrix3d<Tile>
    val xSize = 30
    val ySize = 30
    val zSize = 30

    private var focus = Vec3(xSize / 2, ySize / 2, zSize / 2)

    val selectedType = mutableMapOf<Int, TileType>()

    val dir: File

    init {

        map = matrix3dOf(xSize, ySize, zSize) { x, y, z ->
            Tile(TileType.UNFINISHED)
        }

        screen = ScreenBuilder.createScreenFor(game.tileGrid)

        xScreenSize = screen.width
        yScreenSize = screen.height

        dir = File("chunks")
        dir.mkdirs()

    }


    override fun keyPressed(keycode: KeyCode) {
        when (keycode) {
            KeyCode.ESCAPE -> {
                game.activity = MenuActivity(game)
            }

            KeyCode.BACKSPACE -> {
            }

            KeyCode.DOWN -> {
                focus += Vec3(0, -1, 0)
            }

            KeyCode.LEFT -> {
                focus += Vec3(-1, 0, 0)
            }

            KeyCode.UP -> {
                focus += Vec3(0, 1, 0)
            }

            KeyCode.RIGHT -> {
                focus += Vec3(1, 0, 0)
            }

            KeyCode.KEY_S -> focus += Vec3(0, -1, 0)
            KeyCode.KEY_W -> focus += Vec3(0, 1, 0)
            KeyCode.KEY_A -> focus += Vec3(-1, 0, 0)
            KeyCode.KEY_D -> focus += Vec3(1, 0, 0)
            KeyCode.KEY_Q -> focus += Vec3(0, 0, -1)
            KeyCode.KEY_E -> focus += Vec3(0, 0, 1)
            KeyCode.KEY_P -> save()
            KeyCode.KEY_L -> load()
            else -> println("Unhandled Key $keycode")
        }

    }

    override fun mouseAction(mouseAction: MouseEvent): Boolean {

//        println(mouseAction)
        // If we click on the palette, select the color for that button.
        // If we click the

        if (mouseAction.type in listOf(
                MouseEventType.MOUSE_CLICKED,
                MouseEventType.MOUSE_PRESSED,
                MouseEventType.MOUSE_DRAGGED
            )
        ) {
            if (mouseAction.position.x == 0 && mouseAction.position.y < TileType.values().size) {
                // select color for that button

                selectedType[mouseAction.button] = TileType.values()[mouseAction.position.y]
                println(selectedType[mouseAction.button].toString() + " selected")
            } else {
                // TODO: draw on map if map contains this point

                val x = mouseAction.position.x - xScreenSize / 2 + focus.x
                val y = -(mouseAction.position.y - yScreenSize / 2 - focus.y) - 1
                val z = focus.z

                println("$x $y $z")

                val type = selectedType[mouseAction.button]
                if (type == null) {
                    println("Select a Tile First")

                } else if (map.contains(x, y, z)) {

                    map[x, y, z].type = type
                }

            }
        }
        return true
    }

    override fun render() {

        // Render map
        (0 until yScreenSize).forEach { y ->
            (0 until xScreenSize).forEach { x ->

                val worldX = x - xScreenSize / 2 + focus.x
                val worldY = y - yScreenSize / 2 + focus.y
                val worldZ = focus.z

                var textCharacter = Appearance.darkness
                var tint = 1.0
                for (down in 0 until 4) {

                    val tz = worldZ - down
                    if (map.outside(worldX, worldY, worldZ))
                        break

                    val appearance = map[worldX, worldY, worldZ].type.appearance
                    if (appearance != null) {
                        textCharacter = appearance
                        tint = 1.0 / (tz + 1.0)
                        break
                    }
                }
                screen.draw(textCharacter, Position.create(x, yScreenSize - y - 1))
            }
        }

        // Render palette
        TileType.values().forEachIndexed { index, tileType ->
            screen.draw(tileType.appearance ?: Appearance.darkness, Position.create(0, index))
        }

        screen.display()

    }


    // Format
    // x,y,z size
    // tiles in array

    private fun save() {
        // contract the bounding box around what is actually there, and save to a file

        var xMin = map.xSize - 1
        var yMin = map.ySize - 1
        var zMin = map.zSize - 1

        var xMax = 0
        var yMax = 0
        var zMax = 0

        map.forEachIndexed { x, y, z, e ->
            if (e.type != TileType.UNFINISHED) {
                xMin = min(x, xMin)
                yMin = min(y, yMin)
                zMin = min(z, zMin)

                xMax = max(x, xMax)
                yMax = max(y, yMax)
                zMax = max(z, zMax)
            }
        }

        if (xMin > xMax || yMin > yMax || zMin > zMax) {
            println("Box not set up, could not save")
            return
        }

//        val boundingRect = RectangularPrism(Vec3[xMin,yMin,zMin], Vec3[xMax,yMax,zMax])
//        val submap = map.submap(boundingRect).toList().map()
//
//                .each{
//
//        }


        val file = File(dir, Random().nextLong().toString() + ".cnk")

        file.appendText("${xMax - xMin + 1} ${yMax - yMin + 1} ${zMax - zMin + 1}\n")
        (zMin..zMax).forEach { z ->
            (yMin..yMax).forEach { y ->
                (xMin..xMax).forEach { x ->
                    file.appendText((map[x, y, z].type.ordinal + 65).toChar().toString())
                }
            }
        }

    }

    // Load up a saved chunk for further editing.
    private fun load() {

        val file = dir.listFiles().first()
        val (metadata, mapData) = file.readText().split('\n')

        val dimensions = metadata.split(" ").map { it.toInt() }
        val (localXSize, localYSize, localZSize) = dimensions

//        val map = Matrix3d(dimensions[0], dimensions[1], dimensions[2], { x, y, z -> Tile(TileType.UNFINISHED)})

        map = matrix3dOf(xSize, ySize, zSize) { x, y, z ->
            Tile(TileType.UNFINISHED)
        }

        val tiles = mapData.toCharArray().map { TileType.values()[it.code - 65] }

        val xOffset = xSize / 2 - localXSize / 2
        val yOffset = ySize / 2 - localYSize / 2
        val zOffset = zSize / 2 - localZSize / 2

        tiles.forEachIndexed { i, tile ->
            val x = xOffset + i % localXSize
            val y = yOffset + (i % (localXSize * localYSize)) / localXSize
            val z = zOffset + i / (localXSize * localYSize)
            map[x, y, z].type = tile
        }

    }

}