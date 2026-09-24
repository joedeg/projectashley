package com.jdegnan.projectashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * A simple spatial partitioning structure that organizes entities into a grid of cells.
 * <p>
 * This is used to optimize spatial queries (like collision detection) by only checking
 * entities that are in the same or neighboring cells, rather than checking every entity
 * against every other entity.
 * </p>
 */
public class SpatialGrid {
    public int cellSize;
    public ObjectMap<GridPoint2, Array<Entity>> cells = new ObjectMap<>();

    /**
     * Creates a new SpatialGrid with the specified cell size.
     *
     * @param cellSize The width and height of each grid cell in world units.
     */
    public SpatialGrid(int cellSize){
        this.cellSize = cellSize;
    }

    /**
     * Inserts an entity into the grid based on its world coordinates.
     *
     * @param entity The entity to insert.
     * @param x The world x-coordinate of the entity.
     * @param y The world y-coordinate of the entity.
     */
    public void insert(Entity entity, float x, float y){

        int cellX = (int)(x / cellSize);
        int cellY = (int)(y / cellSize);

        GridPoint2 key = new GridPoint2(cellX, cellY);

        Array<Entity> bucket = cells.get(key);

        if(bucket == null){
            bucket = new Array<>();
            cells.put(key, bucket);
        }

        bucket.add(entity);
    }

    /**
     * Clears the grid.
     */
    public void clear(){
        cells.clear();
    }
}
