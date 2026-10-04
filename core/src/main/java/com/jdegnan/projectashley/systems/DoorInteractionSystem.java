package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.Direction;
import com.jdegnan.projectashley.assets.ItemAssetRegistry;
import com.jdegnan.projectashley.components.DoorAnimationComponent;
import com.jdegnan.projectashley.components.DoorComponent;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.InteractionMessageComponent;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.components.TagComponents.WallComponent;
import com.jdegnan.projectashley.inventory.ItemDefinition;

public class DoorInteractionSystem extends IteratingSystem {
    private final ComponentMapper<DoorComponent> dm =
        ComponentMapper.getFor(DoorComponent.class);

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    final ComponentMapper<InteractionRequestComponent> im =
        ComponentMapper.getFor(InteractionRequestComponent.class);

    private final ComponentMapper<FacingComponent> fm =
        ComponentMapper.getFor(FacingComponent.class);

    private final ComponentMapper<WallComponent> wm =
        ComponentMapper.getFor(WallComponent.class);

    private final ComponentMapper<DoorAnimationComponent> am =
        ComponentMapper.getFor(DoorAnimationComponent.class);

    private final ComponentMapper<RenderComponent> rm =
        ComponentMapper.getFor(RenderComponent.class);

    private final ComponentMapper<InteractionMessageComponent> mm =
        ComponentMapper.getFor(InteractionMessageComponent.class);


    private ImmutableArray<Entity> players;

    private static final float INTERACTION_DISTANCE = 48f;

    private final ItemAssetRegistry itemAssetRegistry;

    public DoorInteractionSystem(ItemAssetRegistry itemAssetRegistry) {
        super(Family.all(
            DoorComponent.class,
            PositionComponent.class
        ).get());

        this.itemAssetRegistry = itemAssetRegistry;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                PositionComponent.class,
                InteractionRequestComponent.class,
                FacingComponent.class,
                InventoryComponent.class
            ).get()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if (players.size() == 0) {
            return;
        }

        Entity player = players.first();

        DoorComponent doorComp = dm.get(entity);

        if (doorComp.opened) {
            return;
        }

        if(doorComp.requiredItem == null){
            return;
        }

        InteractionRequestComponent interaction =
            im.get(player);

        if (!interaction.interact) {
            return;
        }

        PositionComponent playerPos = pm.get(player);
        PositionComponent doorPos = pm.get(entity);

        float dx =
            doorPos.x - playerPos.x;
        float dy =
            doorPos.y - playerPos.y;

        if (dx * dx + dy * dy > INTERACTION_DISTANCE * INTERACTION_DISTANCE) {
            return;

        }

        FacingComponent facing = fm.get(player);

        if (!isFacingDoor(
            playerPos,
            doorPos,
            facing.direction)) {
            return;
        }

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);


        if (!inventory.has(doorComp.requiredItem)) {
            InteractionMessageComponent message = mm.get(player);

            ItemDefinition definition =
                itemAssetRegistry.get(doorComp.requiredItem);

            message.show(
                "You need a "
                    + definition.getDisplayName()
                    + " to open this door.",
                3f
            );

            return;
        }

        doorComp.opened = true;
        doorComp.locked = false;

        DoorAnimationComponent animation = am.get(entity);
        RenderComponent render = rm.get(entity);

        render.region = animation.openFrame;

        if (wm.has(entity)) {
            entity.remove(WallComponent.class);
        }

        System.out.println("Door Opened ");


    }


    private boolean isFacingDoor(
        PositionComponent playerPos,
        PositionComponent doorPos,
        Direction direction) {

        float dx = doorPos.x - playerPos.x;
        float dy = doorPos.y - playerPos.y;

        switch (direction) {
            case LEFT:
                return dx < 0;
            case RIGHT:
                return dx > 0;
            case UP:
                return dy > 0;
            case DOWN:
                return dy < 0;
            default:
                return false;

        }
    }
}
