package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class MovementCollisionSystem extends IteratingSystem {

    private ImmutableArray<Entity> walls;

    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);

    public MovementCollisionSystem() {
        super(Family.all(
            PositionComponent.class,
            VelocityComponent.class,
            ColliderComponent.class).exclude(
            WallComponent.class
        ).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        walls = engine.getEntitiesFor(
            Family.all(
                WallComponent.class,
                ColliderComponent.class).get()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        VelocityComponent vel = vm.get(entity);
        ColliderComponent col = cm.get(entity);

        moveX(pos, vel, col, deltaTime);
        resolveX(col, vel, pos);

        moveY(pos, vel, col, deltaTime);
        resolveY(col, vel, pos);
    }

    private void moveY(PositionComponent pos, VelocityComponent vel, ColliderComponent col, float deltaTime) {
        pos.y += vel.vy * deltaTime;

        col.localBounds.y = pos.y;
    }

    private void moveX(PositionComponent pos, VelocityComponent vel, ColliderComponent col, float deltaTime) {
        pos.x += vel.vx * deltaTime;

        col.localBounds.x = pos.x;
    }

    private void resolveX(ColliderComponent col, VelocityComponent vel, PositionComponent pos){
        for(Entity wall : walls){
            ColliderComponent wallCol = cm.get(wall);

            if(!col.localBounds.overlaps(wallCol.localBounds))
                continue;

            if(vel.vx > 0){
                col.localBounds.x =
                    wallCol.localBounds.x
                    -col.localBounds.width;
            }
            else if (vel.vx < 0){
                col.localBounds.x =
                    wallCol.localBounds.x
                    + wallCol.localBounds.width;
            }

            pos.x = col.localBounds.x;
            vel.vx = 0;
        }
    }

    private void resolveY(ColliderComponent col, VelocityComponent vel, PositionComponent pos) {
        for(Entity wall : walls){
            ColliderComponent wallCol = cm.get(wall);

            if(!col.localBounds.overlaps(wallCol.localBounds))
                continue;

            if(vel.vy > 0){
                col.localBounds.y =
                    wallCol.localBounds.y
                        -col.localBounds.height;
            }
            else if (vel.vy < 0){
                col.localBounds.y =
                    wallCol.localBounds.y
                        + wallCol.localBounds.height;
            }

            pos.y = col.localBounds.y;
            vel.vy = 0;
        }

    }
}
