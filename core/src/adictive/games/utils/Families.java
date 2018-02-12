package adictive.games.utils;

import com.badlogic.ashley.core.Family;

import adictive.games.components.BoundsComponent;
import adictive.games.components.PlayerComponent;
import adictive.games.components.TransformComponent;

/**
 * Created by arielalvarez on 2/11/18.
 */

public class Families {
    public static final Family PLAYER = Family.all(PlayerComponent.class).get();
    public static final Family BOUNDS = Family.all(BoundsComponent.class, TransformComponent.class).get();
}
