package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyMovementComponent implements Component, Pool.Poolable {
    public float speed = 20f;
    public float directionX = 1f;
    public float directionY = 0f;


    @Override
    public void reset() {
        speed = 20f;
        directionX = 1f;
        directionY = 0f;
    }
}
