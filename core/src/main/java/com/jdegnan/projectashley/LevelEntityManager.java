package com.jdegnan.projectashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.LevelEntityComponent;

public class LevelEntityManager {
    private final PooledEngine engine;

    public LevelEntityManager(PooledEngine engine){
        this.engine = engine;
    }

    public void destroyLevelEntites(String levelId){
        ImmutableArray<Entity> entities =
            engine.getEntitiesFor(Family.all(
                LevelEntityComponent.class
            ).get());

        for(Entity entity : entities){
            LevelEntityComponent level =
                entity.getComponent(LevelEntityComponent.class);

            if(level.levelId.equals(levelId)){
                engine.removeEntity(entity);
            }
        }
    }
}
