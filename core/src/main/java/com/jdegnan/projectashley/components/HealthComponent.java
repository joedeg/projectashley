package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class HealthComponent implements Component, Pool.Poolable {
    public int hp = 3;
    public int maxHp = 3;

    @Override
    public void reset() {
        hp = 3;
        maxHp = 3;
    }
}
