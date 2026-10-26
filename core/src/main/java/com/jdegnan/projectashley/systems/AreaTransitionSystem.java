package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.jdegnan.projectashley.components.AreaTransitionComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.events.CollisionEvent;
import com.jdegnan.projectashley.events.EventBus;
import com.jdegnan.projectashley.level.LevelDefinition;
import com.jdegnan.projectashley.level.LevelManager;
import com.jdegnan.projectashley.level.Levels;
import com.jdegnan.projectashley.screens.AreaTransitionController;

public class AreaTransitionSystem extends EntitySystem {

    private final AreaTransitionController areaTransitionController;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> transitions;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);
    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);

    private final ComponentMapper<AreaTransitionComponent> am =
        ComponentMapper.getFor(AreaTransitionComponent.class);



    public AreaTransitionSystem(AreaTransitionController areaTransitionController) {
        this.areaTransitionController = areaTransitionController;
    }

    @Override
    public void addedToEngine(Engine engine) {
        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                PositionComponent.class,
                ColliderComponent.class
            ).get()
        );

        transitions = engine.getEntitiesFor(
            Family.all(
                AreaTransitionComponent.class,
                PositionComponent.class,
                ColliderComponent.class
            ).get()
        );

    }

    @Override
    public void update(float deltaTime) {


        if(areaTransitionController.isTransitionInProgress()){
            return;
        }

        for (Entity player : players) {
            for (Entity transitionEntity : transitions) {
                if(!overlaps(player, transitionEntity)){
                    continue;
                }

                AreaTransitionComponent transition =
                    am.get(transitionEntity);

                if(transition.destinationLevelId == null){
                    continue;
                }

                LevelDefinition destination = Levels.getById(transition.destinationLevelId);
                if(destination == null){
                    continue;
                }

                areaTransitionController.requestTransition(
                    destination,
                    transition.spawnX,
                    transition.spawnY);

                    return;


            }
        }
    }

    private boolean overlaps(Entity a, Entity b){
        PositionComponent pa = pm.get(a);
        PositionComponent pb = pm.get(b);

        ColliderComponent ca = cm.get(a);
        ColliderComponent cb = cm.get(b);

        Rectangle ra = new Rectangle(
            pa.x + ca.localBounds.x,
            pa.y + ca.localBounds.y,
            ca.localBounds.width,
            ca.localBounds.height);

        Rectangle rb = new Rectangle(
            pb.x + cb.localBounds.x,
            pb.y + cb.localBounds.y,
            cb.localBounds.width,
            cb.localBounds.height
        );

        return ra.overlaps(rb);
    }
}
