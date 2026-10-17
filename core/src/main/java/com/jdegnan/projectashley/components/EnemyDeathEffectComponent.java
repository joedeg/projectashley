package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyDeathEffectComponent implements Component, Pool.Poolable
{
    public float elapsedTime;
    @Override
    public void reset() {
        elapsedTime = 0f;
    }
}
