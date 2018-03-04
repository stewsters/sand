package com.stewsters.sand.display.activities

import com.stewsters.sand.SandGame
import org.codetome.zircon.api.input.KeyStroke

class LoseActivity(var game: SandGame) : Activity {

    override fun render() {

        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun keyPressed(keycode: KeyStroke) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    lateinit var spriteBatch: SpriteBatch
//    lateinit var hudCamera: OrthographicCamera
//
//
//    override fun show() {
////        val w = Gdx.graphics.width.toFloat()
////        val h = Gdx.graphics.height.toFloat()
////        hudCamera = OrthographicCamera(w, h)
////        spriteBatch = SpriteBatch()
//
//    }
//
//    override fun render(delta: Float) {
////        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
////            Gdx.app.log("Lose Activity", "Switch to GameScreen")
////            game.setActivity(PlayActivity(game))
////        }
////
////        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f)
////        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
////
////        spriteBatch.projectionMatrix = hudCamera.combined
////
////        spriteBatch.setColor(1f, 1f, 1f, 1f)
////
////        spriteBatch.begin()
////
//////        TextureManager.bitmapFont.draw(spriteBatch, "You are dead. Press space to restart.", hudCamera.viewportWidth / 4f, hudCamera.viewportHeight / 2f)
////
////        spriteBatch.end()
//    }

}
