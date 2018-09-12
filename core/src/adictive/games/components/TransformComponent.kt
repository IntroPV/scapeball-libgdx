package adictive.games.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

class TransformComponent : Component {
    val pos = Vector3()
    val size = Vector2()
    val lastPos = Vector3(0f, 0f, 0f)
    val scale = Vector2(1f, 1f)
    var rotation = 0.0f
}
