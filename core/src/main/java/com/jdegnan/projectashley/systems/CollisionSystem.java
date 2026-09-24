package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.events.CollisionEvent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.events.EventBus;

/**
 * System responsible for detecting and handling collisions between entities.
 * <p>
 * It processes entities that have both {@link PositionComponent} and {@link ColliderComponent}.
 * </p>
 */
public class CollisionSystem extends EntitySystem {

    private ImmutableArray<Entity> collidables;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        collidables = engine.getEntitiesFor(Family.all(
            PositionComponent.class,
            ColliderComponent.class
        ).get());
    }

    /**
     * Checks for collisions between all collidable entities.
     * Currently, performs a simple O(n^2) check.
     *
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        int count = collidables.size();

        for(int i = 0; i < count; i++){
            Entity a = collidables.get(i);

            // Check against other entities in the list
            for(int j = i + 1; j < count; j++) {
                Entity b = collidables.get(j);

                if (overlaps(a, b)) {
                    handleCollision(a, b);
                }
            }
        }
    }

    /**
     * Determines if two entities overlap based on their world-space bounding boxes.
     *
     * @param a The first entity.
     * @param b The second entity.
     * @return True if the entities' colliders overlap.
     */
    private boolean overlaps(Entity a, Entity b){
        PositionComponent pa = pm.get(a);
        PositionComponent pb = pm.get(b);

        ColliderComponent ca = cm.get(a);
        ColliderComponent cb = cm.get(b);

        Rectangle ra = new Rectangle(
            pa.x + ca.localBounds.x,
            pa.y + ca.localBounds.y,
            ca.localBounds.width,
            ca.localBounds.height);

        Rectangle rb = new Rectangle(
            pb.x + cb.localBounds.x,
            pb.y + cb.localBounds.y,
            cb.localBounds.width,
            cb.localBounds.height
        );

        return ra.overlaps(rb);
    }

    /**
     * Called when a collision is detected between two entities.
     *
     * @param a The first entity.
     * @param b The second entity.
     */
    private void handleCollision(Entity a, Entity b){
      //  System.out.println("collision! " + a.toString() + " | " + b.toString());

        EventBus.collisionEvents.add(new CollisionEvent(a, b));
    }
}
