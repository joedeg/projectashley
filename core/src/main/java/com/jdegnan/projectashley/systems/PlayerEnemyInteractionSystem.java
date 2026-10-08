package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;

import com.badlogic.gdx.graphics.Color;
import com.jdegnan.projectashley.components.DamageCooldownComponent;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.RenderComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.events.CollisionEvent;
import com.jdegnan.projectashley.events.EventBus;

public class PlayerEnemyInteractionSystem extends EntitySystem {

    private static final float DAMAGE_COOLDOWN = 1.0f;

    private final ComponentMapper<HealthComponent> healthMapper =
        ComponentMapper.getFor(HealthComponent.class);

    private final ComponentMapper<DamageCooldownComponent> cooldownMapper =
        ComponentMapper.getFor(DamageCooldownComponent.class);

    private final ComponentMapper<RenderComponent> renderMapper =
        ComponentMapper.getFor(RenderComponent.class);


    private ImmutableArray<Entity> players;

    @Override
    public void addedToEngine(Engine engine) {

        super.addedToEngine(engine);

        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                HealthComponent.class,
                DamageCooldownComponent.class
            ).get()
        );
    }

    @Override
    public void update(float deltaTime) {

        updateCooldowns(deltaTime);
        processCollisionEvents();

        EventBus.collisionEvents.clear();
    }

    private void updateCooldowns(float deltaTime) {

        for (Entity player : players) {

            DamageCooldownComponent cooldown =
                cooldownMapper.get(player);

            RenderComponent render = renderMapper.get(player);

            if (cooldown.remaining > 0f) {

                cooldown.remaining -= deltaTime;

                if (cooldown.remaining < 0f) {
                    cooldown.remaining = 0f;
                }
            }

            if(cooldown.remaining <= 0){
                render.color.set(Color.WHITE);
            }
        }
    }

    private void processCollisionEvents() {

        for (CollisionEvent event : EventBus.collisionEvents) {

            Entity player = getPlayer(event);

            if (player == null) {
                continue;
            }

            HealthComponent health =
                healthMapper.get(player);

            DamageCooldownComponent cooldown =
                cooldownMapper.get(player);

            if (health.hp <= 0) {
                continue;
            }

            if (cooldown.remaining > 0f) {
                continue;
            }

            health.hp--;
            cooldown.remaining = DAMAGE_COOLDOWN;

            RenderComponent render = renderMapper.get(player);

            render.color.set(Color.RED);

            System.out.println(
                "Player hit! Health: "
                    + health.hp
            );
        }
    }

    private Entity getPlayer(CollisionEvent event) {

        if (event.a.getComponent(PlayerComponent.class) != null &&
            event.b.getComponent(EnemyComponent.class) != null) {

            return event.a;
        }

        if (event.b.getComponent(PlayerComponent.class) != null &&
            event.a.getComponent(EnemyComponent.class) != null) {

            return event.b;
        }

        return null;
    }
}

