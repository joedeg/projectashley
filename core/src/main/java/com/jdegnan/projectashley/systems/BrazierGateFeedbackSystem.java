package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class BrazierGateFeedbackSystem extends IteratingSystem {

    private final ComponentMapper<BrazierGateComponent> gm =
        ComponentMapper.getFor(BrazierGateComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    public BrazierGateFeedbackSystem() {
        super(Family.all(
            BrazierGateComponent.class,
            RenderComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BrazierGateComponent gate = gm.get(entity);

        if(!gate.feedbackActive){
            return;
        }

        gate.feedbackTimer -= deltaTime;

        if(gate.feedbackTimer <= 0f){
            gate.feedbackTimer = 0f;
            gate.feedbackActive = false;
        }
    }
}
