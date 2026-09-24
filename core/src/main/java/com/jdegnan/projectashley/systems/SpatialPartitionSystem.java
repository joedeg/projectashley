package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.SpatialGrid;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;

/**
 * System responsible for maintaining a spatial partition of entities.
 * <p>
 * This system updates a {@link SpatialGrid} by clearing it each frame and re-inserting
 * all entities that have both a {@link PositionComponent} and a {@link ColliderComponent}.
 * It also provides a {@link #query(float, float)} method to efficiently find entities
 * near a specific world location.
 * </p>
 */
public class SpatialPartitionSystem extends IteratingSystem {

    private final SpatialGrid grid;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    /**
     * Creates a new SpatialPartitionSystem.
     *
     * @param grid The {@link SpatialGrid} to be maintained by this system.
     */
    public SpatialPartitionSystem(SpatialGrid grid) {
        super(Family.all(
            PositionComponent.class,
            ColliderComponent.class
        ).get());

        this.grid = grid;
    }

    /**
     * Clears the grid and then updates it with current entity positions by calling super.update().
     *
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        grid.clear();
        super.update(deltaTime);
    }

    /**
     * Inserts the entity into the spatial grid based on its current position.
     *
     * @param entity The entity being processed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PositionComponent pos = pm.get(entity);
        grid.insert(entity, pos.x, pos.y);
    }

    /**
     * Queries the spatial grid for entities in the cell containing (x, y) and its 8 neighbors.
     *
     * @param x The world x-coordinate to query around.
     * @param y The world y-coordinate to query around.
     * @return An {@link Array} of entities found in the specified and neighboring cells.
     */
    public Array<Entity> query(float x, float y){

        int cellX = (int)(x / grid.cellSize);
        int cellY = (int)(y / grid.cellSize);

        Array<Entity> results = new Array<>();

        for(int ox = -1; ox <= 1; ox++){
            for(int oy = -1; oy <= 1; oy++){

                GridPoint2 key = new GridPoint2(cellX + ox, cellY + oy);

                Array<Entity> bucket = grid.cells.get(key);

                if(bucket != null){
                    results.addAll(bucket);
                }
            }
        }

        return results;
    }
}
