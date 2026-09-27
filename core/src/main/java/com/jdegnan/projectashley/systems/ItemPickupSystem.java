package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.ItemPickupComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class ItemPickupSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<ItemPickupComponent> im =
        ComponentMapper.getFor(ItemPickupComponent.class);

    private ImmutableArray<Entity> players;

    private static final float PICKUP_DISTANCE = 24;

    public ItemPickupSystem() {
        super(Family.all(
            ItemPickupComponent.class,
            PositionComponent.class
        ).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players = engine.getEntitiesFor(
            Family.all(PlayerComponent.class,
                PositionComponent.class,
                InventoryComponent.class
            ).get()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(players.size() == 0){
            return;
        }

        Entity player = players.first();

        PositionComponent itemPosition = pm.get(entity);

        PositionComponent playerPosition = pm.get(player);

        float dx = itemPosition.x - playerPosition.x;
        float dy = itemPosition.y - playerPosition.y;

        if(dx * dx + dy * dy > PICKUP_DISTANCE * PICKUP_DISTANCE){
            return;
        }

        ItemPickupComponent pickup = im.get(entity);

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        inventory.add(pickup.item);

        System.out.println("Item picked up: " + pickup.item);

        getEngine().removeEntity(entity);
    }
}
