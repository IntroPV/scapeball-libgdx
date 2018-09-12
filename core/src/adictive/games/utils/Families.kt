package adictive.games.utils

import adictive.games.components.BoundsComponent
import adictive.games.components.PlayerComponent
import adictive.games.components.TransformComponent
import com.badlogic.ashley.core.Family

/**
 * Created by arielalvarez on 2/11/18.
 */

object Families {
    val PLAYER = Family.all(PlayerComponent::class.java!!).get()
    val BOUNDS = Family.all(BoundsComponent::class.java, TransformComponent::class.java).get()
}
