package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.jdegnan.projectashley.LevelRuntime;


public class LevelEntityLoader {
    private final LevelEntityFactoryRegistry factoryRegistry;

    public LevelEntityLoader(
        LevelEntityFactoryRegistry factoryRegistry) {
        this.factoryRegistry = factoryRegistry;
    }

    public void load(
        TiledMap map,
        PooledEngine engine,
        LevelRuntime runtime
    ){
        MapLayer layer = map.getLayers().get("Entities");

        if(layer == null){
            return;
        }

        for(MapObject object : layer.getObjects()){
            Entity entity = factoryRegistry.create(object);

            if(entity == null){
                continue;
            }

            runtime.addEntity(
                engine,
                entity
            );
        }
    }

}
