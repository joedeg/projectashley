package com.jdegnan.projectashley.components.TagComponents;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyComponent implements Component, Pool.Poolable {

    public String enemyType;


    @Override
    public void reset() {
        enemyType = null;
    }
}
