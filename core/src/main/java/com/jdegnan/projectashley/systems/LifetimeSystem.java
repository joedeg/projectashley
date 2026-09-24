package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.LifetimeComponent;
import com.jdegnan.projectashley.components.TagComponents.DestroyComponent;

/**
 * System responsible for managing the lifespan of entities.
 * <p>
 * It decrements the timer in the {@link LifetimeComponent} of each entity.
 * When the timer reaches zero or less, a {@link DestroyComponent} is added
 * to the entity, marking it for removal by the {@link DestroySystem}.
 * </p>
 */
public class LifetimeSystem extends IteratingSystem {

    private final ComponentMapper<LifetimeComponent> lm =
        ComponentMapper.getFor(LifetimeComponent.class);

    /**
     * Creates a new LifetimeSystem.
     */
    public LifetimeSystem() {
        super(Family.all(
            LifetimeComponent.class
        ).get());
    }

    /**
     * Updates the lifetime timer for an entity and marks it for destruction if expired.
     *
     * @param entity The entity being processed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        LifetimeComponent time = lm.get(entity);

        time.timer -= deltaTime;

        if(time.timer <= 0)
            entity.add(new DestroyComponent());
    }
}
