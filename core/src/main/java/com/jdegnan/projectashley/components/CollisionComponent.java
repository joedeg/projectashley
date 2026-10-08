package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.utils.Pool;

public class CollisionComponent implements Component, Pool.Poolable {

    // TODO Bullet used this but I am unsure it's correct.
    public Entity other;

    public boolean collidedX;
    public boolean collidedY;

    public boolean hitLeft;
    public boolean hitRight;
    public boolean hitTop;
    public boolean hitBottom;


    @Override
    public void reset() {
        other = null;

        collidedX = false;
        collidedY = false;

        hitLeft = false;
        hitRight = false;
        hitTop = false;
        hitBottom = false;
    }
}
