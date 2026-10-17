package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.EnemyDeathEffectComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class EnemyDeathEffectSystem extends IteratingSystem {

    private static final float EFFECT_DURATION = 0.5f;

    private final ComponentMapper<EnemyDeathComponent> dm =
        ComponentMapper.getFor(EnemyDeathComponent.class);

    private final ComponentMapper<EnemyDeathEffectComponent> edem =
        ComponentMapper.getFor(EnemyDeathEffectComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    public EnemyDeathEffectSystem() {
        super(Family.all(
            EnemyDeathComponent.class,
            EnemyDeathEffectComponent.class,
            RenderComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
            EnemyDeathComponent death = dm.get(entity);

            if(!death.dying){
                return;
            }

            EnemyDeathEffectComponent effect = edem.get(entity);
            RenderComponent render = rm.get(entity);

            effect.elapsedTime += deltaTime;

            float progress = MathUtils.clamp(
                effect.elapsedTime / EFFECT_DURATION,
                0f,
                1f
            );

            float scale = 1f - progress;
            render.width = 27f * scale;
            render.height = 16f * scale;

            render.color.set(1f, 1f, 1f, 1f - progress);
    }
}
