package com.jdegnan.projectashley.factories;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.SpriteComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;

public class WallFactory {

    private PooledEngine engine;
    private TextureAtlas atlas;

    private final float TILE_SIZE;

    public WallFactory(PooledEngine engine, TextureAtlas atlas, float tileSize){
        this.engine = engine;
        this.atlas = atlas;
        TILE_SIZE = tileSize;
    }

    public Entity creatWall(int tileX, int tileY){
        Entity wall = engine.createEntity();

        PositionComponent pos = engine.createComponent(PositionComponent.class);

        pos.x = tileX * TILE_SIZE;
        pos.y = tileY * TILE_SIZE;

        ColliderComponent col = engine.createComponent(ColliderComponent.class);

        col.localBounds.set(pos.x, pos.y, TILE_SIZE, TILE_SIZE);

        wall.add(pos);
        wall.add(col);

        wall.add(engine.createComponent(WallComponent.class));

        engine.addEntity(wall);

        return wall;

    }
}

