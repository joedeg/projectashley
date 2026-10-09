package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class AttackComponent implements Component, Pool.Poolable {

    public float remainingTime = 0.12f;
    public int damage = 1;

    public final Array<Entity> enemiesHit = new Array<>();
    @Override
    public void reset() {
        remainingTime = 0.12f;
        damage = 1;
    }
}
