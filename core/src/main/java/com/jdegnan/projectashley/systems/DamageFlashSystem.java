package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Color;
import com.jdegnan.projectashley.components.DamageFlashComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class DamageFlashSystem extends EntitySystem {
    private static final float FLASH_DURATION = 0.12f;

    private final ComponentMapper<DamageFlashComponent> dfm =
        ComponentMapper.getFor(DamageFlashComponent.class);
    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private ImmutableArray<Entity> entities;

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        entities = engine.getEntitiesFor(
            Family.all(
                DamageFlashComponent.class,
                RenderComponent.class
            ).get()
        );
    }

    @Override
    public void update(float deltaTime) {
        for(int i = 0; i < entities.size(); i++){
            Entity entity = entities.get(i);

            DamageFlashComponent flash = dfm.get(entity);
            RenderComponent render = rm.get(entity);

            if(!flash.flashing){
                continue;
            }

            flash.remainingTime -= deltaTime;

            if(flash.remainingTime > 0f){
                render.color.set(Color.RED);
            } else {
                flash.remainingTime = 0f;
                flash.flashing = false;
                render.color.set(Color.WHITE);
            }
        }
    }
}
