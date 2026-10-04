package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.BrazierComponent;
import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.DoorAnimationComponent;
import com.jdegnan.projectashley.components.DoorComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;

public class BrazierGateSystem extends EntitySystem {
    private ImmutableArray<Entity> braziers;
    private ImmutableArray<Entity> gates;

    private final ComponentMapper<BrazierComponent> bm =
        ComponentMapper.getFor(BrazierComponent.class);
    private final ComponentMapper<BrazierGateComponent> gm =
        ComponentMapper.getFor(BrazierGateComponent.class);
    private final ComponentMapper<DoorAnimationComponent> dam =
        ComponentMapper.getFor(DoorAnimationComponent.class);
    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        braziers = engine.getEntitiesFor(
            Family.all(
                BrazierComponent.class
            ).get()
        );

        gates = engine.getEntitiesFor(
            Family.all(
                BrazierGateComponent.class,
                DoorComponent.class
            ).get()
        );
    }

    @Override
    public void update(float deltaTime) {

        for(Entity gateEntity : gates){
            BrazierGateComponent gate = gm.get(gateEntity);

            if(gate.opened || gate.puzzleId == null){
                continue;
            }

            int litCount = 0;

            for(Entity brazierEntity : braziers){
                BrazierComponent brazier = bm.get(brazierEntity);

                if(!brazier.lit){
                    continue;
                }

                if(!brazier.puzzleId.equals(gate.puzzleId)){
                    continue;
                }

                litCount++;
            }

            if(litCount >= gate.requiredBraziers){
                openGate(gateEntity, gate);
            }
        }
    }

    private void openGate(
        Entity gateEntity,
        BrazierGateComponent gate) {

        DoorComponent door =
            gateEntity.getComponent(DoorComponent.class);

        DoorAnimationComponent animation =
            gateEntity.getComponent(DoorAnimationComponent.class);

        RenderComponent render =
            gateEntity.getComponent(RenderComponent.class);

        gate.opened = true;
        door.opened = true;
        door.locked = false;

        render.region = animation.openFrame;

        gateEntity.remove(WallComponent.class);
    }
}
