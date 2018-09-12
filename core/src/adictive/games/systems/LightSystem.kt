package adictive.games.systems

import adictive.games.SquareWorld
import adictive.games.components.LightComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.ComponentMapper
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.EntitySystem
import com.badlogic.ashley.core.Family
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.math.Matrix4

class LightSystem(private val world: SquareWorld) : EntitySystem(10), Reseteable {

    private val transformMapper = ComponentMapper.getFor<TransformComponent>(TransformComponent::class.java)
    private val lightMapper = ComponentMapper.getFor<LightComponent>(LightComponent::class.java)
    private val lightBatch = SpriteBatch()
    private val screenProjection = Matrix4()

    private val lightBuffer: FrameBuffer
    private val lightBufferRegion: TextureRegion

    init {

        val lowDisplayW = Gdx.graphics.width.toFloat()
        val lowDisplayH = Gdx.graphics.height.toFloat()

        lightBuffer = FrameBuffer(Pixmap.Format.RGBA8888, powerOfTwo(lowDisplayW), powerOfTwo(lowDisplayH), false)
        lightBuffer.colorBufferTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)

        lightBufferRegion = TextureRegion(lightBuffer.colorBufferTexture)
        lightBufferRegion.flip(false, true)
    }

    override fun update(deltaTime: Float) {
        renderLights()
    }

    fun resize(w: Int, h: Int) {
        screenProjection.setToOrtho2D(0f, 0f, w.toFloat(), h.toFloat())
    }

    private fun renderLights() {
        lightBuffer.begin()
        Gdx.gl.glClearColor(0xFF / 255f, 0xFC / 255f, 0xEF / 255f, 0.1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ZERO)
        lightBatch.enableBlending()
        lightBatch.projectionMatrix = world.camera.combined
        lightBatch.begin()

        val lights = engine.getEntitiesFor(LIGHTS)
        for (entity in lights) {
            renderLightOn(entity)
        }

        lightBatch.end()
        lightBuffer.end()

        Gdx.gl.glBlendFunc(GL20.GL_DST_COLOR, GL20.GL_ZERO)
        lightBatch.projectionMatrix = screenProjection
        lightBatch.begin()
        lightBatch.color = Color.WHITE
        lightBatch.draw(lightBufferRegion, 0f, 0f, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
        lightBatch.end()
    }

    private fun renderLightOn(entity: Entity) {
        val transformComponent = transformMapper.get(entity)
        val lightComponent = lightMapper.get(entity)
        val lightSize = lightComponent.radius
        lightBatch.color = lightComponent.color

        this.lightBatch.draw(
                lightComponent.shape,
                transformComponent.pos.x + transformComponent.size.x / 2 - lightSize / 2,
                transformComponent.pos.y + transformComponent.size.y / 2 - lightSize / 2,
                lightSize, lightSize
        )
    }

    override fun reset() {
        // Do nothing. Does not hold game state.
    }

    companion object {

        val LIGHTS = Family.all(LightComponent::class.java!!).get()

        private fun powerOfTwo(n: Float): Int {
            return Math.pow(2.0, Math.ceil(Math.log(n.toDouble()) / Math.log(2.0))).toInt()
        }
    }
}
