package com.jdegnan.projectashley.systems;


import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.MapCollisionData;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;


public class MapCollisionSystem extends EntitySystem {

    private final MapCollisionData collisionData;

    private final Array<Entity> wallEntities = new Array<>();


    public MapCollisionSystem(
        MapCollisionData collisionData) {

        this.collisionData = collisionData;

    }


    /**
     * Called when this system is added to the engine. Triggers the build process.
     *
     * @param engine The engine this system was added to.
     **/
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        buildCollision(engine);
    }


    private void buildCollision(Engine engine) {
        for (Rectangle rect : collisionData.getCollisionRects()) {

            createWall(engine, rect);

        }

        createBoundaryWall(engine, collisionData.getMapBounds());
    }

    private void createWall(Engine engine, Rectangle rect) {
        Entity wall = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);

        pos.x = rect.x;
        pos.y = rect.y;

        ColliderComponent col = engine.createComponent(ColliderComponent.class);

        col.localBounds.set(
            rect
        );

        WallComponent wallTag = engine.createComponent(WallComponent.class);

        wall.add(pos);
        wall.add(col);
        wall.add(wallTag);

        engine.addEntity(wall);
        wallEntities.add(wall);
    }

    private void createBoundaryWall(Engine engine, Rectangle rect) {

        float thickness = 1f;

        // Bottom
        createWall(engine, new Rectangle(
            rect.x - thickness,
            rect.y - thickness,
            rect.width + 2 * thickness,
            thickness

        ));


        // Top
        createWall(engine, new Rectangle(
            rect.x - thickness,
            rect.y + rect.height,
            rect.width + 2 * thickness,
            thickness
        ));

        // Left
        createWall(engine, new Rectangle(
            rect.x - thickness,
            rect.y,
            thickness,
            rect.height
        ));

        // Right
        createWall(engine, new Rectangle(
            rect.x + rect.width,
            rect.y,
            thickness,
            rect.height
        ));


    }

    @Override
    public void removedFromEngine(Engine engine) {

        for(Entity wall : wallEntities){
            engine.removeEntity(wall);
        }

        wallEntities.clear();

        super.removedFromEngine(engine);
    }
}
