package com.jdegnan.projectashley;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.maps.tiled.TiledMap;

public  class WorldContext {

    private final Engine engine;

    private final TiledMap map;

    private int tileSize;

    private final SpatialGrid grid;

    public WorldContext(Engine engine, TiledMap map, int tileSize, SpatialGrid grid) {
        this.engine = engine;
        this.map = map;
        this.tileSize = tileSize;
        this.grid = grid;
    }

    public  WorldContext getWorldContext(){
        return this;
    }

    public Engine getEngine() {
        return engine;
    }

    public TiledMap getMap() {
        return map;
    }

    public int getTileSize() {
        return tileSize;
    }

    public SpatialGrid getGrid() {
        return grid;
    }
}
