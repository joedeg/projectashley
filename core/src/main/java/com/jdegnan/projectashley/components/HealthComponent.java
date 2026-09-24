package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;


public class HealthComponent implements Component, Pool.Poolable {
    public int hp;
    public int maxHp;

    @Override
    public void reset() {
        hp = 0;
        maxHp = 0;
    }
}
