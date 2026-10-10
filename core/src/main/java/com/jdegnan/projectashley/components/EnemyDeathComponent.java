package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyDeathComponent implements Component, Pool.Poolable{

    public boolean dying;

    public float remainingTime;

    @Override
    public void reset() {
        dying = false;
        remainingTime = 0f;
    }
}
