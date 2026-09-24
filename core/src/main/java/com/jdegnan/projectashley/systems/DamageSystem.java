package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.jdegnan.projectashley.components.DamageComponent;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.TagComponents.DestroyComponent;
import com.jdegnan.projectashley.events.CollisionEvent;
import com.jdegnan.projectashley.events.EventBus;

/**
 * System responsible for applying damage when entities collide.
 * It listens for {@link CollisionEvent}s in the {@link EventBus} and updates
 * the {@link HealthComponent} of victims based on the {@link DamageComponent} of attackers.
 */
public class DamageSystem extends EntitySystem {
    private ComponentMapper<DamageComponent> dm =
        ComponentMapper.getFor(DamageComponent.class);

    private ComponentMapper<HealthComponent> hm =
        ComponentMapper.getFor(HealthComponent.class);

    /**
     * Processes all pending collision events to resolve damage.
     * Removes all events after they have been handled.
     *
     * @param deltaTime The time in seconds since the last update.
     */
    @Override
    public void update(float deltaTime) {
        for(CollisionEvent event : EventBus.collisionEvents){
            handle(event.a, event.b);
            handle(event.b, event.a);
        }

        EventBus.collisionEvents.clear();
    }

    /**
     * Attempts to apply damage from an attacker entity to a victim entity.
     * Damage is only applied if the attacker has a {@link DamageComponent}
     * and the victim has a {@link HealthComponent}.
     *
     * @param attacker The entity potentially dealing damage.
     * @param victim The entity potentially receiving damage.
     */
    private void handle(Entity attacker, Entity victim){
        DamageComponent damage = dm.get(attacker);
        HealthComponent health = hm.get(victim);

        if(damage == null | health == null) return;

        health.hp -= damage.damage;

        attacker.add(new DestroyComponent());
    }
}
