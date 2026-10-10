package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;

public class EnemyDeathAnimationSystem extends IteratingSystem {

    private static final float DEATH_DURATION = 1f;

    private final ComponentMapper<EnemyDeathComponent> edm =
        ComponentMapper.getFor(EnemyDeathComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    public EnemyDeathAnimationSystem() {
        super(Family.all(
            EnemyComponent.class,
            EnemyDeathComponent.class,
            RenderComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyDeathComponent death = edm.get(entity);

        if(!death.dying){
            return;
        }

        RenderComponent render = rm.get(entity);

        float progress = 1f -
            (death.remainingTime / DEATH_DURATION);

        progress = Math.max(0f, Math.min(1f, progress));

        float scale = 1f - progress;

        render.width = 27f * scale;
        render.height = 16f * scale;

        render.originX = render.width / 2f;
        render.originY = render.height / 2f;

        render.color.a = 1f - progress;

    }
}
