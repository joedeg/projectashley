package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.Direction;

public class FacingComponent implements Component, Pool.Poolable{

    public Direction direction = Direction.DOWN;
    @Override
    public void reset() {
        direction = Direction.DOWN;
    }
}
