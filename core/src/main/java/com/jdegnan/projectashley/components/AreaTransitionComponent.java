package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class AreaTransitionComponent implements Component, Pool.Poolable {
    public String destinationLevelId;
    public float spawnX;
    public float spawnY;
    @Override
    public void reset() {
        destinationLevelId = null;
        spawnX = 0;
        spawnY = 0;
    }
}
