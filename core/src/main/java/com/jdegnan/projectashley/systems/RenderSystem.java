package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Sort;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.SpriteComponent;import java.util.ArrayList;

/**
 * System responsible for rendering entities to the screen.
 * <p>
 * It iterates over all entities that have both a {@link PositionComponent} and a {@link SpriteComponent},
 * drawing their current texture region at their current world coordinates using a {@link SpriteBatch}.
 * </p>
 */
public class RenderSystem extends IteratingSystem {
    private SpriteBatch batch;
    private OrthographicCamera camera;

    private ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private ComponentMapper<SpriteComponent> sm =
        ComponentMapper.getFor(SpriteComponent.class);


    /**
     * Creates a new RenderSystem.
     *
     * @param batch  The {@link SpriteBatch} used to draw entity sprites.
     * @param camera The {@link OrthographicCamera} used for the projection matrix.
     */
    public RenderSystem(SpriteBatch batch, OrthographicCamera camera) {
        super(Family.all(
            PositionComponent.class,
            SpriteComponent.class

        ).get());


        this.batch = batch;
        this.camera = camera;
    }

    /**
     * Called by the engine each frame. Wraps the entity processing in batch.begin() and batch.end().
     *
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        super.update(deltaTime);
//
//        ImmutableArray<Entity> renderables = getEntities();
//
//        Array<Entity> renderQueue = new Array<>();
//
//        for (Entity e : renderables
//        ) {
//            renderQueue.add(e);
//        }
//
//        Sort sort = new Sort();
//
//        sort.sort(
//            renderQueue,
//
//            (a, b) -> {
//                PositionComponent pa = pm.get(a);
//
//                PositionComponent pb = pm.get(b);
//
//                return Float.compare(
//                    pb.y,
//                    pa.y
//                );
//            }
//        );
//
//
//        for (Entity entity : renderQueue) {
//
//
//            PositionComponent position = pm.get(entity);
//            SpriteComponent sprite = sm.get(entity);
//
//            batch.draw(sprite.textureRegion, position.x, position.y);
//        }
        batch.end();
    }

    /**
     * Renders a single entity's sprite at its current position.
     *
     * @param entity    The entity being processed.
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        PositionComponent pos = pm.get(entity);
        SpriteComponent spr = sm.get(entity);

        if (spr.textureRegion != null) {
            batch.draw(spr.textureRegion, pos.x, pos.y);
        }
    }
}
