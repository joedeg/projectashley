package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.rendering.MapRenderer;
import com.jdegnan.projectashley.rendering.RenderContext;
import com.jdegnan.projectashley.systems.MapCollisionSystem;

/**
 * Manages the runtime state of a game level, including its assets, collision systems,
 * and associated entities.
 */
public class LevelRuntime {

    private final Level level;
    private final LevelAssetSet assetSet;
    private final MapCollisionSystem collisionSystem;
    private final MapRenderer mapRenderer;

    private final Array<Entity> entities = new Array<>();

    private final LevelEntityLoader entityLoader;

    /**
     * Constructs a new LevelRuntime for the specified level and asset set.
     *
     * @param level    The level instance to manage.
     * @param assetSet The set of assets loaded for this level.
     */
    public LevelRuntime(Level level, LevelAssetSet assetSet, LevelEntityLoader entityLoader) {
        this.level = level;
        this.assetSet = assetSet;
        this.entityLoader = entityLoader;

        this.collisionSystem = new MapCollisionSystem(
            level.getCollisionData());
        this.mapRenderer = new MapRenderer(level.getMap());
    }

    /**
     * Gets the definition of the level.
     *
     * @return The {@link LevelDefinition} associated with this level.
     */
    public LevelDefinition getDefinition() {
        return level.getDefinition();
    }

    /**
     * Gets the level instance.
     *
     * @return The {@link Level} being managed.
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the map renderer.
     *
     * @return The {@link MapRenderer} for this level.
     */
    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    /**
     * Renders the tiled map.
     *
     * @param context The render context to use.
     */
    public void renderMap(RenderContext context) {
        if (mapRenderer != null) {
            mapRenderer.render(context);
        }
    }

    /**
     * Adds an entity to the specified engine and tracks it within this level runtime.
     *
     * @param engine The Ashley {@link PooledEngine} to add the entity to.
     * @param entity The {@link Entity} to add.
     */
    public void addEntity(PooledEngine engine, Entity entity) {
        entities.add(entity);
        engine.addEntity(entity);
    }

    /**
     * Adds the level-specific systems (like collision) to the Ashley engine.
     *
     * @param engine The Ashley {@link PooledEngine} to configure.
     */
    public void addTo(PooledEngine engine) {
        engine.addSystem(collisionSystem);

        entityLoader.load(level.getMap(), engine, this);
    }

    /**
     * Removes the level-specific systems and all tracked entities from the Ashley engine.
     *
     * @param engine The Ashley {@link PooledEngine} to clean up.
     */
    public void removeFrom(PooledEngine engine) {
        engine.removeSystem(collisionSystem);

        for (Entity entity : entities) {
            engine.removeEntity(entity);
        }

        entities.clear();
    }

    /**
     * Gets the map boundary data associated with this level.
     *
     * @return The {@link Rectangle} representing the map boundaries.
     */
    public Rectangle getMapBounds() {
        return level.getMapBounds();
    }

    /**
     * Disposes of the level's assets by releasing the associated {@link LevelAssetSet}.
     */
    public void dispose() {
        mapRenderer.dispose();
        assetSet.release();
    }
}
