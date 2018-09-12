package adictive.games.screens

import adictive.games.MainGame
import adictive.games.SquareWorld
import adictive.games.components.CoinComponent
import adictive.games.level.LevelLoader
import adictive.games.systems.*
import adictive.games.utils.GameData
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter

class PlayScreen(val superCubito: MainGame) : ScreenAdapter(), Reseteable {
    private val engine = PooledEngine()
    private val world = SquareWorld()
    private val levelLoader: LevelLoader
    var state = RUNNING

    private val debugSystem: DebugSystem
        get() = engine.getSystem(DebugSystem::class.java)

    private val designerSystem: DesignerSystem
        get() = engine.getSystem(DesignerSystem::class.java)

    init {
        this.levelLoader = LevelLoader(world, engine)
        generateSystems()
        reset()
    }

    override fun reset() {
        pause(true)
        val systems = engine.systems
        engine.removeAllEntities()
        for (system in systems) {
            (system as Reseteable).reset()
        }
        this.levelLoader.load(GameData.currentLevel)
        pause(false)
        this.state = RUNNING
    }

    fun generateSystems() {
        engine.addSystem(TileUpdateSystem(world))
        engine.addSystem(FollowCameraSystem())

        engine.addSystem(PlayerInputSystem())
        engine.addSystem(EnemySystem())
        engine.addSystem(BlackHoleSystem())
        engine.addSystem(FloorEffectSystem(world))
        engine.addSystem(MovementSystem())
        engine.addSystem(PlayerEffectsSystem())
        engine.addSystem(CollisionSystem(world, this))
        engine.addSystem(RenderingSystem(world))
        engine.addSystem(LightSystem(world))
        engine.addSystem(DebugSystem(world, this))
        engine.addSystem(DesignerSystem(world, this))

        designerSystem.setProcessing(false)
        debugSystem.setProcessing(false)

    }

    fun killed() {
        this.state = KILLED
    }

    fun win() {
        if (engine.getEntitiesFor(COINS_FAMILY).size() == 0) {
            this.state = WIN
        }
    }

    override fun resize(width: Int, height: Int) {
        super.resize(width, height)
        engine.getSystem<RenderingSystem>(RenderingSystem::class.java).resize(width, height)
        engine.getSystem<LightSystem>(LightSystem::class.java).resize(width, height)
    }

    fun updateMode() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.V)) {
            debugSystem.setProcessing(!debugSystem.checkProcessing())
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            val designerSystem = designerSystem
            val isDesignerActive = !designerSystem.checkProcessing()
            designerSystem.setProcessing(isDesignerActive)
            pause(isDesignerActive)
        }
    }

    private fun pause(pause: Boolean) {
        engine.getSystem<MovementSystem>(MovementSystem::class.java).setProcessing(!pause)
        engine.getSystem<PlayerEffectsSystem>(PlayerEffectsSystem::class.java).setProcessing(!pause)
        engine.getSystem<CollisionSystem>(CollisionSystem::class.java).setProcessing(!pause)
        engine.getSystem<FollowCameraSystem>(FollowCameraSystem::class.java).setProcessing(!pause)
        engine.getSystem<PlayerInputSystem>(PlayerInputSystem::class.java).setProcessing(!pause)
        engine.getSystem<EnemySystem>(EnemySystem::class.java).setProcessing(!pause)
        engine.getSystem<LightSystem>(LightSystem::class.java).setProcessing(!pause)
        engine.getSystem<BlackHoleSystem>(BlackHoleSystem::class.java).setProcessing(!pause)
        engine.getSystem<FloorEffectSystem>(FloorEffectSystem::class.java).setProcessing(!pause)
    }

    override fun render(delta: Float) {
        updateMode()
        when (state) {
            RUNNING -> engine.update(delta)
            KILLED -> {
                reset()
                superCubito.analyticsManager.loseLevel(GameData.currentLevel)
            }
            WIN -> {
                val level = GameData.incrementCurrentLevel()
                superCubito.analyticsManager.winLevel(level)
                reset()
            }
            DEBUG_CHANGE_LEVEL -> reset()
        }
    }

    companion object {
        private val COINS_FAMILY = Family.all(CoinComponent::class.java!!).get()

        val RUNNING = 1
        val KILLED = 2
        val WIN = 3
        val DEBUG_CHANGE_LEVEL = 4
    }

}
