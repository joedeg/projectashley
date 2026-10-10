package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.CollisionComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;
import com.jdegnan.projectashley.components.VelocityComponent;

public class MovementCollisionSystem extends IteratingSystem {

    private ImmutableArray<Entity> walls;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<VelocityComponent> vm =
        ComponentMapper.getFor(VelocityComponent.class);

    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);

    private final ComponentMapper<CollisionComponent> ccm =
        ComponentMapper.getFor(CollisionComponent.class);


    public MovementCollisionSystem() {
        super(Family.all(
            PositionComponent.class,
            VelocityComponent.class,
            ColliderComponent.class,
            CollisionComponent.class
        ).exclude(
            WallComponent.class
        ).get());

        walls = null;
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

        CollisionComponent collision = ccm.get(entity);
        collision.reset();


        moveX(pos, vel, col, deltaTime);
        resolveX(col, vel, pos, collision);

        moveY(pos, vel, col, deltaTime);
        resolveY(col, vel, pos, collision);
    }

    private void moveY(PositionComponent pos, VelocityComponent vel, ColliderComponent col, float deltaTime) {
        pos.y += vel.vy * deltaTime;
    }

    private void moveX(PositionComponent pos, VelocityComponent vel, ColliderComponent col, float deltaTime) {
        pos.x += vel.vx * deltaTime;
    }

    private Rectangle worldBounds(
        PositionComponent pos,
        ColliderComponent col) {
        return new Rectangle(
            pos.x + col.localBounds.x,
            pos.y + col.localBounds.y,
            col.localBounds.width,
            col.localBounds.height
        );
    }

    private void resolveX(
        ColliderComponent col,
        VelocityComponent vel,
        PositionComponent pos,
        CollisionComponent collision) {

        Rectangle bounds = worldBounds(pos, col);


        for (Entity wall : walls) {
            ColliderComponent wallCol = cm.get(wall);

            if (!bounds.overlaps(wallCol.localBounds))
                continue;

            if (vel.vx > 0) {
                pos.x =
                    wallCol.localBounds.x
                        - col.localBounds.x
                        - col.localBounds.width;

                collision.collidedX = true;
                collision.hitRight = true;


            } else if (vel.vx < 0) {
                pos.x =
                    wallCol.localBounds.x
                        + wallCol.localBounds.width
                        - col.localBounds.x;

                collision.collidedX = true;
                collision.hitLeft = true;
            }
            vel.vx = 0;

            bounds = worldBounds(pos, col);
        }
    }

    private void resolveY(
        ColliderComponent col,
        VelocityComponent vel,
        PositionComponent pos,
        CollisionComponent collision) {

        Rectangle bounds = worldBounds(pos, col);

        for (Entity wall : walls) {
            ColliderComponent wallCol = cm.get(wall);

            if (!bounds.overlaps(wallCol.localBounds))
                continue;

            if (vel.vy > 0) {
                pos.y =
                    wallCol.localBounds.y
                        - col.localBounds.y
                        - col.localBounds.height;

                collision.collidedY = true;
                collision.hitBottom = true;

            } else if (vel.vy < 0) {
                pos.y =
                    wallCol.localBounds.y
                        + wallCol.localBounds.height
                        - col.localBounds.y;

                collision.collidedY = true;
                collision.hitTop = true;
            }

            vel.vy = 0;

            bounds = worldBounds(pos, col);
        }

    }
}
