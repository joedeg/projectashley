package com.jdegnan.projectashley.events;

import com.badlogic.ashley.core.Entity;

public class CollisionEvent {

    public Entity a;
    public Entity b;
    public CollisionEvent(Entity a, Entity b) {
        this.a = a;
        this.b = b;
    }
}
