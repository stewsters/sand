package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import com.stewsters.sand.display.renderSystems.HudRenderSystem
import com.stewsters.sand.display.renderSystems.MapRenderSystem
import com.stewsters.sand.game.actions.AttachRopeAction
import com.stewsters.sand.game.actions.ClimbAction
import com.stewsters.sand.game.actions.DescendAction
import com.stewsters.sand.game.actions.DropTorchAction
import com.stewsters.sand.game.actions.LightTorchAction
import com.stewsters.sand.game.actions.WalkAction
import com.stewsters.sand.game.map.World
import com.stewsters.sand.game.math.Facing
import com.stewsters.sand.game.systems.LightSystem
import com.stewsters.sand.game.systems.TurnProcessSystem
import com.stewsters.sand.generator.RuinGen
import org.codetome.zircon.api.builder.TerminalBuilder
import org.codetome.zircon.api.input.KeyStroke
import org.codetome.zircon.api.screen.Screen


class PlayActivity(var game: SandGame) : Activity {

    var world: World

    var turnProcessSystem: TurnProcessSystem
    var lightSystem: LightSystem
    var mapRenderSystem: MapRenderSystem
    var hudRenderSystem: HudRenderSystem

    private var screen: Screen
//    private var panel:Panel

    init {

        world = RuinGen.gen()

        //set up systems
        turnProcessSystem = TurnProcessSystem(world)
        lightSystem = LightSystem()

        mapRenderSystem = MapRenderSystem()
        hudRenderSystem = HudRenderSystem()


        screen = TerminalBuilder.createScreenFor(game.terminal)

    }


    override fun keyPressed(keycode: KeyStroke) {
        // TODO: convert keycode into action, do that action


        val action = when (keycode.getCharacter()) {
            'w' -> WalkAction(Facing.UP)
            'a' -> WalkAction(Facing.LEFT)
            's' -> WalkAction(Facing.DOWN)
            'd' -> WalkAction(Facing.RIGHT)

            'e' -> ClimbAction()
            'q' -> DescendAction()

            't' -> LightTorchAction()
            'g' -> DropTorchAction()
            'r' -> AttachRopeAction()
            else -> null
        }

        println(action.toString())

        world.player.nextAction = action

        turnProcessSystem.process()
        lightSystem.process(world)

        if (world.player.health?.cur ?: 0 <= 0) {
            println("Switch to Lose Activity")
            game.activity = LoseActivity(game)
        }
    }


    override fun render() {

        mapRenderSystem.process(world, screen)
        hudRenderSystem.processSystem(world, screen)
        screen.display()

    }

}