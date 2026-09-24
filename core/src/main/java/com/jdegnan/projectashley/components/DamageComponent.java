package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageComponent implements Component, Pool.Poolable {
    public int damage;

    @Override
    public void reset() {
        damage = 0;
    }
}
