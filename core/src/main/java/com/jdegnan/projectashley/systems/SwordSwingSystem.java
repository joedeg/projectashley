package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.components.EffectLifetimeComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.SwordSwingComponent;

public class SwordSwingSystem extends EntitySystem {
    private final ComponentMapper<SwordSwingComponent> ssw =
        ComponentMapper.getFor(SwordSwingComponent.class);

    private final ComponentMapper<EffectLifetimeComponent> elm =
        ComponentMapper.getFor(EffectLifetimeComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);


    private ImmutableArray<Entity> effects;
    private final Array<Entity> expiredEffects = new Array<>();
    private Engine engine;

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;

        effects = engine.getEntitiesFor(
            Family.all(
                SwordSwingComponent.class,
                EffectLifetimeComponent.class,
                RenderComponent.class
            ).get());
    }


    @Override
    public void update(float deltaTime) {
        expiredEffects.clear();

        for (int i = 0; i < effects.size(); i++) {
            Entity effect = effects.get(i);

            SwordSwingComponent swing = ssw.get(effect);
            EffectLifetimeComponent lifetime = elm.get(effect);
            RenderComponent render = rm.get(effect);

            PositionComponent effectPos = pm.get(effect);

            if(swing.owner != null){
                PositionComponent ownerPos = pm.get(swing.owner);

                if(ownerPos != null){
                    effectPos.x = ownerPos.x + swing.offsetX;
                    effectPos.y = ownerPos.y + swing.offsetY;

                    }
            }

            swing.stateTime += deltaTime;
            lifetime.remainingTime -= deltaTime;

            int frameIndex = (int) (swing.stateTime / swing.frameDuration);

            if (frameIndex >= swing.frames.length ||
                lifetime.remainingTime <= 0f) {

                expiredEffects.add(effect);
                continue;
            }

            render.region = swing.frames[frameIndex];
        }
        for (int j = 0; j < expiredEffects.size; j++) {
            engine.removeEntity(expiredEffects.get(j));
        }
    }
}
