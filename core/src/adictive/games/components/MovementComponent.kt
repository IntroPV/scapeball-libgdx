package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2

class MovementComponent : Component {
    val velocity = Vector2()
    val accel = Vector2()
    var friction = 0.2f
}
