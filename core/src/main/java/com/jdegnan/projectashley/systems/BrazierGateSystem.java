package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.audio.Sound;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.PuzzleAssets;
import com.jdegnan.projectashley.components.BrazierComponent;
import com.jdegnan.projectashley.components.BrazierGateComponent;
import com.jdegnan.projectashley.components.DoorAnimationComponent;
import com.jdegnan.projectashley.components.DoorComponent;
import com.jdegnan.projectashley.components.GateGlowComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;
import com.jdegnan.projectashley.factories.ParticleFactory;

public class BrazierGateSystem extends EntitySystem {
    private final Assets assets;
    private ImmutableArray<Entity> braziers;
    private ImmutableArray<Entity> gates;

    private ParticleFactory particleFactory;

    private final ComponentMapper<BrazierComponent> bm =
        ComponentMapper.getFor(BrazierComponent.class);
    private final ComponentMapper<BrazierGateComponent> gm =
        ComponentMapper.getFor(BrazierGateComponent.class);
    private final ComponentMapper<DoorAnimationComponent> dam =
        ComponentMapper.getFor(DoorAnimationComponent.class);
    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private final ComponentMapper<GateGlowComponent> glm =
        ComponentMapper.getFor(GateGlowComponent.class);


    public BrazierGateSystem(
        Assets assets,
        ParticleFactory particleFactory) {
        this.assets = assets;
        this.particleFactory = particleFactory;
    }

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

        Sound sound = assets.get(PuzzleAssets.GATE_OPEN);
        sound.play();

        gate.feedbackActive = true;
        gate.feedbackTimer = 0.5f;

        GateGlowComponent glow = glm.get(gateEntity);

        if(glow != null){
            glow.active = true;
            glow.timer = 0f;
            glow.duration = 0.5f;
            glow.maxRadius = 56f;
        }

        PositionComponent pos = gateEntity.getComponent(PositionComponent.class);
        particleFactory.createBurst(
            pos.x + 16f,
            pos.y + 48f,
            12
        );

    }
}
