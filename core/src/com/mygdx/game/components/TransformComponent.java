package com.mygdx.game.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class TransformComponent implements Component {
    public final Vector3 pos = new Vector3();
    public Vector2 scale = new Vector2(0.5f, 0.5f);
    public float rotation = 0.0f;
}