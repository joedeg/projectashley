package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.TagComponents.DestroyComponent;

/**
 * System responsible for removing entities from the engine that have been marked for destruction.
 * <p>
 * It processes any entity that has a {@link DestroyComponent} attached to it.
 * </p>
 */
public class DestroySystem extends IteratingSystem {

    private final Engine engine;
    private final ComponentMapper<DestroyComponent> dm =
        ComponentMapper.getFor(DestroyComponent.class);

    /**
     * Creates a new DestroySystem.
     *
     * @param engine The Ashley {@link Engine} used to remove entities.
     */
    public DestroySystem(Engine engine) {
        super(Family.all(
            DestroyComponent.class
        ).get());

        this.engine = engine;
    }

    /**
     * Removes the entity from the engine.
     *
     * @param entity The entity to be destroyed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        engine.removeEntity(entity);
    }
}
