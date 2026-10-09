package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.components.AttackComponent;
import com.jdegnan.projectashley.components.AttackRequestComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.HealthComponent;

import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class PlayerAttackSystem extends EntitySystem {

    private static final float ATTACK_LIFETIME = 0.12f;
    private static final float ATTACK_SIZE = 24f;
    private static final float ATTACK_OFFSET = 24f;

    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<FacingComponent> fm =
        ComponentMapper.getFor(FacingComponent.class);
    private final ComponentMapper<AttackRequestComponent> arm =
        ComponentMapper.getFor(AttackRequestComponent.class);
    private final ComponentMapper<AttackComponent> am =
        ComponentMapper.getFor(AttackComponent.class);
    private final ComponentMapper<ColliderComponent> cm =
        ComponentMapper.getFor(ColliderComponent.class);
    private final ComponentMapper<HealthComponent> hm =
        ComponentMapper.getFor(HealthComponent.class);

    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> attacks;

    private final Array<Entity> expiredAttacks = new Array<>();

    private final PooledEngine engine;

    public PlayerAttackSystem(PooledEngine engine) {
        this.engine = engine;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players =
            engine.getEntitiesFor(
                Family.all(
                    PlayerComponent.class,
                    PositionComponent.class,
                    FacingComponent.class,
                    AttackRequestComponent.class
                ).get());

        enemies =
            engine.getEntitiesFor(
                Family.all(
                    EnemyComponent.class,
                    PositionComponent.class,
                    ColliderComponent.class,
                    HealthComponent.class
                ).get());

        attacks =
            engine.getEntitiesFor(
                Family.all(
                    AttackComponent.class,
                    PositionComponent.class,
                    ColliderComponent.class
                ).get());
    }

    @Override
    public void update(float deltaTime) {
        updateExistingAttacks(deltaTime);
        createRequestedAttacks();

    }

    private void updateExistingAttacks(float deltaTime) {

        for (int i = 0; i < attacks.size(); i++) {
            Entity attackEntity = attacks.get(i);

            AttackComponent attack = am.get(attackEntity);
            attack.remainingTime -= deltaTime;

            if (attack.remainingTime <= 0) {
                expiredAttacks.add(attackEntity);
            }

            damageOverlappingEnemies(attackEntity, attack);
        }

        for (int i = 0; i < expiredAttacks.size; i++) {
            engine.removeEntity(expiredAttacks.get(i));
        }
    }

    private void createRequestedAttacks(){
        for(int i = 0; i < players.size(); i++){
            Entity player = players.get(i);

            AttackRequestComponent attackRequest = arm.get(player);

            if(!attackRequest.attack){
                continue;
            }

            attackRequest.attack = false;
            createAttack(player);
        }
    }

    private void createAttack(Entity player) {

        PositionComponent playerPos = pm.get(player);
        FacingComponent playerFacing = fm.get(player);

        Entity attackEntity = engine.createEntity();

        PositionComponent position =
            engine.createComponent(PositionComponent.class);

        position.x = playerPos.x;
        position.y = playerPos.y;

        switch (playerFacing.direction) {
            case UP:
                position.y += ATTACK_OFFSET;
                break;
            case DOWN:
                position.y -= ATTACK_OFFSET;
                break;
            case LEFT:
                position.x -= ATTACK_OFFSET;
                break;
            case RIGHT:
                position.x += ATTACK_OFFSET;
                break;

        }

        ColliderComponent collider =
            engine.createComponent(ColliderComponent.class);

        collider.localBounds.set(
            0,
            0,
            ATTACK_SIZE,
            ATTACK_SIZE
        );

        AttackComponent attack =
            engine.createComponent(AttackComponent.class);

        attack.remainingTime = ATTACK_LIFETIME;
        attack.damage = 1;

        attackEntity.add(position);
        attackEntity.add(collider);
        attackEntity.add(attack);

        engine.addEntity(attackEntity);



    }

    private void damageOverlappingEnemies(Entity attackEntity, AttackComponent attack) {
        Rectangle attackBounds = getWorldBounds(attackEntity);

        for(int i = 0; i < enemies.size(); i++){
            Entity enemy = enemies.get(i);

            if(attack.enemiesHit.contains(enemy, true)){
                continue;
            }

            Rectangle enemyBounds = getWorldBounds(enemy);

            if(!attackBounds.overlaps(enemyBounds)){
                continue;
            }

            HealthComponent enemyHealth = hm.get(enemy);

            if(enemyHealth.hp <= 0){
                continue;
            }

            enemyHealth.hp -= attack.damage;

            attack.enemiesHit.add(enemy);
            System.out.println("hit enemy");
        }
    }

    private Rectangle getWorldBounds(Entity attackEntity) {
        PositionComponent position = pm.get(attackEntity);
        ColliderComponent collider = cm.get(attackEntity);

        return new Rectangle(
            position.x + collider.localBounds.x,
            position.y + collider.localBounds.y,
            collider.localBounds.width,
            collider.localBounds.height
        );
    }
}


