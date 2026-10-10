package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AttackCooldownComponent implements Component, Pool.Poolable {

    public float remaningTime;

    public boolean isReady(){
        return remaningTime <= 0f;
    }
    @Override
    public void reset() {
        remaningTime = 0f;
    }
}
