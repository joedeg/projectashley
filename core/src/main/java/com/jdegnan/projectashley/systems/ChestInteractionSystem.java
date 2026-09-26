package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.Direction;
import com.jdegnan.projectashley.components.ChestComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class ChestInteractionSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<InteractionRequestComponent> im =
        ComponentMapper.getFor(InteractionRequestComponent.class);

    private final ComponentMapper<ChestComponent> cm =
        ComponentMapper.getFor(ChestComponent.class);

    private final ComponentMapper<ColliderComponent> col =
        ComponentMapper.getFor(ColliderComponent.class);

    private final ComponentMapper<FacingComponent> fm =
        ComponentMapper.getFor(FacingComponent.class);


    private ImmutableArray<Entity> players;


    public ChestInteractionSystem() {
        super(Family.all(
            ChestComponent.class,
            PositionComponent.class
        ).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players =
            engine.getEntitiesFor(Family.all(
                    PlayerComponent.class,
                    PositionComponent.class,
                    InteractionRequestComponent.class
                ).get()
            );

    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        if (players.size() == 0) {
            return;
        }

        Entity player = players.first();

        FacingComponent facing = fm.get(player);

        ChestComponent chest = cm.get(entity);

        if(!isFacingChest(
            pm.get(player),
            pm.get(entity),
            facing.direction)){
            return;
        }


        if (chest.opened) {
            return;
        }

        InteractionRequestComponent interaction = im.get(player);

        if (!interaction.interact) {

            return;
        }
        PositionComponent playerPos = pm.get(player);

        PositionComponent chestPos = pm.get(entity);

        float dx = playerPos.x - chestPos.x;
        float dy = playerPos.y - chestPos.y;

        float distanceSquared = dx * dx + dy * dy;

        float interactionDistance = 48f;

        if (distanceSquared > interactionDistance * interactionDistance) {
            return;
        }

        if (chest.locked) {
            return;
        }

        chest.opened = true;

    }

    private boolean isFacingChest(
        PositionComponent playerPos,
        PositionComponent chestPos,
        Direction direction) {

        float dx = chestPos.x - playerPos.x;
        float dy = chestPos.y - playerPos.y;

        switch (direction) {
            case UP:
                return dy > 0;
            case DOWN:
                return dy < 0;
            case LEFT:
                return dx < 0;
            case RIGHT:
                return dx > 0;
            default:
                return false;
        }
    }
}
