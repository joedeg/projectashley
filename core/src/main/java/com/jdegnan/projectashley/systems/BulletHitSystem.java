package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;

import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.CollisionComponent;
import com.jdegnan.projectashley.components.TagComponents.BulletComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;

public class BulletHitSystem extends IteratingSystem {

    private final ComponentMapper<CollisionComponent> colm =
        ComponentMapper.getFor(CollisionComponent.class);

    public BulletHitSystem(){
        super(Family.all(
            BulletComponent.class,
            CollisionComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        CollisionComponent col = colm.get(entity);

        Entity other = col.other;

        if(other.getComponent(EnemyComponent.class) != null){
            System.out.println("Bullet hit enemy!");

        }

        entity.remove(CollisionComponent.class);
    }
}
