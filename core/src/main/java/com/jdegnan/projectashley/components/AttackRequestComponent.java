package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AttackRequestComponent implements Component, Pool.Poolable {

    public boolean attack;

    @Override
    public void reset() {
        attack = false;
    }
}
