package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableArray;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.events.CollisionEvent;
import com.jdegnan.projectashley.events.EventBus;

public class PlayerEnemyCollisionSystem extends IteratingSystem {

    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);

    private ImmutableArray<Entity> enimies;

    public PlayerEnemyCollisionSystem( ) {


        super(Family.all(
            PlayerComponent.class,
            ColliderComponent.class
        ).get());
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        enimies = engine.getEntitiesFor(
            Family.all(
                EnemyComponent.class,
                ColliderComponent.class
            ).get()
        );
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

        ColliderComponent playerCol = cm.get(entity);

        for(Entity enemy : enimies){

            ColliderComponent enemyCol = cm.get(enemy);

            if(!playerCol.localBounds.overlaps(enemyCol.localBounds))
            {
                continue;
            }

            EventBus.collisionEvents.add(
                new CollisionEvent(entity, enemy)
            );

        }
    }
}
