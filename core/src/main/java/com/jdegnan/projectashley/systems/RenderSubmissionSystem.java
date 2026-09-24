package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.jdegnan.projectashley.rendering.RenderCommand;
import com.jdegnan.projectashley.rendering.RenderQueue;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;

public class RenderSubmissionSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private final RenderQueue renderQueue;

    public RenderSubmissionSystem(RenderQueue renderQueue) {
        super(Family.all(
            PositionComponent.class,
            RenderComponent.class)
            .get());

        this.renderQueue = renderQueue;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent position = pm.get(entity);

        RenderComponent render = rm.get(entity);

        if (render.region == null) {
            return;
        }

        RenderCommand command =
            renderQueue.obtain();

        command.region = render.region;

        command.x = position.x + render.offsetX;
        command.y = position.y + render.offsetY;

        command.width = render.width;
        command.height = render.height;

        command.layer = render.layer;

        command.sortY = position.y;

        command.color.set(Color.WHITE);

        renderQueue.submit(command);
    }
}
