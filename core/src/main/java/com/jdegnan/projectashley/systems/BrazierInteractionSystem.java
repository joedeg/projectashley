package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.Direction;
import com.jdegnan.projectashley.components.BrazierComponent;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.InteractionMessageComponent;
import com.jdegnan.projectashley.components.InteractionRequestComponent;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.inventory.ItemType;

public class BrazierInteractionSystem extends IteratingSystem {

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<InteractionRequestComponent> im =
        ComponentMapper.getFor(InteractionRequestComponent.class);

    private final ComponentMapper<FacingComponent> fm =
        ComponentMapper.getFor(FacingComponent.class);

    private final ComponentMapper<InventoryComponent> invm =
        ComponentMapper.getFor(InventoryComponent.class);

    private final ComponentMapper<BrazierComponent> bm =
        ComponentMapper.getFor(BrazierComponent.class);

    private final ComponentMapper<InteractionMessageComponent> mm =
        ComponentMapper.getFor(InteractionMessageComponent.class);

    private ImmutableArray<Entity> players;



    public BrazierInteractionSystem() {
        super(Family.all(
            BrazierComponent.class,
            PositionComponent.class
        ).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players = engine.getEntitiesFor(Family.all(
            PositionComponent.class,
            FacingComponent.class,
            PlayerComponent.class,
            InteractionRequestComponent.class,
            InventoryComponent.class,
            InteractionMessageComponent.class
        ).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        if(players.size() == 0){
            return;
        }

        Entity player = players.first();

        BrazierComponent brazier = bm.get(entity);

        if(brazier.lit){
            return;
        }

        InteractionRequestComponent interaction = im.get(player);

        if(!interaction.interact){
            return;
        }

        PositionComponent playerPos = pm.get(player);
        PositionComponent brazierPos = pm.get(entity);

        FacingComponent playerFacing = fm.get(player);

        if(!isFacingBrazier(
            playerPos,
            brazierPos,
            playerFacing.direction
        )){
            return;
        }

        float dx = brazierPos.x - playerPos.x;
        float dy = brazierPos.y - playerPos.y;

        float distance = dx * dx + dy * dy;

        float interactionDistance = 48f;

        if(distance > interactionDistance * interactionDistance ){
            return;
        }

        InventoryComponent inventory = invm.get(player);

        if(!inventory.has(ItemType.FIRE_ROD)){
            InteractionMessageComponent message = mm.get(player);

            message.show(
                "You need to light it.",
                2f
            );

            interaction.interact = false;
            return;
        }

        brazier.lit = true;
        interaction.interact = false;

    }

    private boolean isFacingBrazier(
        PositionComponent playerPos,
        PositionComponent brazierPos,
        Direction direction) {

        float dx = playerPos.x - brazierPos.x;

        float dy = playerPos.y - brazierPos.y;

        switch(direction){
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
