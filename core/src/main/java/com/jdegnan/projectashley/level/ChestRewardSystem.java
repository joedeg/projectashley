package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.ChestComponent;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.inventory.ItemType;

public class ChestRewardSystem extends IteratingSystem {

    private final ComponentMapper<ChestComponent> cm =
        ComponentMapper.getFor(ChestComponent.class);

    private ImmutableArray<Entity> players;

    public ChestRewardSystem() {
        super(Family.all(ChestComponent.class).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                InventoryComponent.class
            ).get()
        );
    }


    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ChestComponent chest = cm.get(entity);

        if(!chest.opened){
            return;
        }

        if(chest.item == null){
            return;
        }



        if(players.size() == 0){
            return;
        }

        Entity player = players.first();

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        inventory.add(chest.item);

        System.out.println("Rewarding " + chest.item);

        chest.item = null;

        System.out.println(
            "Forest keys: " +
                inventory.getCount(ItemType.FOREST_KEY)
        );

    }
}
