package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.GateGlowComponent;

public class GateGlowSystem extends IteratingSystem {

    private final ComponentMapper<GateGlowComponent> gm =
        ComponentMapper.getFor(GateGlowComponent.class);

    public GateGlowSystem() {
        super(Family.all(
            GateGlowComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        GateGlowComponent glow = gm.get(entity);

        if(!glow.active){
            return;
        }

        glow.timer += deltaTime;

        if(glow.timer >= glow.duration){
            glow.active = false;
            glow.timer = glow.duration;
        }
    }
}
