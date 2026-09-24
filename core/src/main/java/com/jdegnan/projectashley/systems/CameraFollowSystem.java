package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class CameraFollowSystem extends IteratingSystem {
    private final OrthographicCamera camera;

    private Rectangle mapBounds;

    public CameraFollowSystem(OrthographicCamera camera) {
        super(Family.all(PositionComponent.class, PlayerComponent.class).get());
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        camera.update();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if(mapBounds == null){
            return;
        }

        PositionComponent pos = entity.getComponent(PositionComponent.class);

        float halfViewportWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfViewportHeight = camera.viewportHeight * camera.zoom / 2f;

        float minX = mapBounds.x + halfViewportWidth;
        float maxX = mapBounds.x + mapBounds.width - halfViewportWidth;

        float minY = mapBounds.y + halfViewportHeight;
        float maxY = mapBounds.y + mapBounds.height - halfViewportHeight;

        float cameraX = clampCameraPosition(
            pos.x,
            minX,
            maxX,
            mapBounds.x + mapBounds.width / 2f
        );

        float cameraY = clampCameraPosition(
            pos.y,
            minY,
            maxY,
            mapBounds.y + mapBounds.height / 2f
        );

        camera.position.set(cameraX, cameraY, 0);
    }

    public void setMapBounds(Rectangle mapBounds) {
        if(mapBounds == null){
            throw new IllegalArgumentException(
                "mapBounds cannot be null."
            );
        }

        this.mapBounds = mapBounds;
    }

    private float clampCameraPosition(
        float target,
        float min,
        float max,
        float mapCenter
    ){
        if( min > max ){
            return mapCenter;
        }

        return MathUtils.clamp(target, min, max);
    }
}
