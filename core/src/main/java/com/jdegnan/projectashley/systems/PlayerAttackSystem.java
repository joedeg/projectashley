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
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.AudioAssets;
import com.jdegnan.projectashley.audio.AudioManager;
import com.jdegnan.projectashley.components.AttackComponent;
import com.jdegnan.projectashley.components.BarrelComponent;
import com.jdegnan.projectashley.components.AttackCooldownComponent;
import com.jdegnan.projectashley.components.AttackRequestComponent;
import com.jdegnan.projectashley.components.ColliderComponent;
import com.jdegnan.projectashley.components.DamageFlashComponent;
import com.jdegnan.projectashley.components.FacingComponent;
import com.jdegnan.projectashley.components.HealthComponent;

import com.jdegnan.projectashley.components.KnockbackComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;
import com.jdegnan.projectashley.factories.SwordSwingFactory;

/**
 * Creates short-lived sword hitboxes for player attack requests.
 *
 * <p>Each hitbox damages each overlapping enemy once and destroys overlapping
 * barrel entities. Sword visuals and audio are delegated to their respective
 * factories and managers.</p>
 */
public class PlayerAttackSystem extends EntitySystem {

    private static final float ATTACK_LIFETIME = 0.20f;
    private static final float ATTACK_SIZE = 32f;
    private static final float ATTACK_OFFSET = 28f;

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

    private final ComponentMapper<DamageFlashComponent> dfm =
        ComponentMapper.getFor(DamageFlashComponent.class);

    private final ComponentMapper<KnockbackComponent> km =
        ComponentMapper.getFor(KnockbackComponent.class);

    private final ComponentMapper<AttackCooldownComponent> acm =
        ComponentMapper.getFor(AttackCooldownComponent.class);


    private final SwordSwingFactory swordSwingFactory;
    private final AudioManager audioManager;
    private final Assets assets;

    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> players;
    private ImmutableArray<Entity> attacks;
    private ImmutableArray<Entity> barrels;

    private final Array<Entity> expiredAttacks = new Array<>();
    private final Array<Entity> brokenBarrels = new Array<>();

    private final PooledEngine engine;

    private static final float ATTACK_COOLDOWN = 0.35f;

    /**
     * Creates the attack system and supplies its entity and presentation services.
     *
     * @param engine engine that owns attack entities
     * @param factory creates the visible sword swing
     * @param audioManager plays attack sounds
     * @param assets provides the sound assets
     */
    public PlayerAttackSystem(PooledEngine engine,
                              SwordSwingFactory factory,
                              AudioManager audioManager,
                              Assets assets) {
        this.engine = engine;
        this.swordSwingFactory = factory;
        this.audioManager = audioManager;
        this.assets = assets;
    }

    /**
     * Caches Ashley family views used to find players, attacks, enemies, and barrels.
     *
     * @param engine Ashley engine hosting this system
     */
    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        players =
            engine.getEntitiesFor(
                Family.all(
                    PlayerComponent.class,
                    PositionComponent.class,
                    FacingComponent.class,
                    AttackRequestComponent.class,
                    AttackCooldownComponent.class
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

        barrels =
            engine.getEntitiesFor(
                Family.all(
                    BarrelComponent.class,
                    PositionComponent.class,
                    ColliderComponent.class
                ).get());
    }

    /**
     * Advances active hitboxes and creates hitboxes requested by ready players.
     *
     * @param deltaTime elapsed time in seconds since the previous update
     */
    @Override
    public void update(float deltaTime) {
        updateExistingAttacks(deltaTime);
        createRequestedAttacks(deltaTime);

    }

    /** Ages active hitboxes, applies their delayed hit check, and removes expired ones. */
    private void updateExistingAttacks(float deltaTime) {
        expiredAttacks.clear();

        for (int i = 0; i < attacks.size(); i++) {
            Entity attackEntity = attacks.get(i);
            AttackComponent attack = am.get(attackEntity);


            if(attack.remainingTime <=0){
                expiredAttacks.add(attackEntity);
                continue;
            }

            attack.damageDelay -= deltaTime;

            if(attack.damageDelay <= 0f){
                damageOverlappingEnemies(attackEntity, attack);
            }

            attack.remainingTime -= deltaTime;

            if (attack.remainingTime <= 0) {
                expiredAttacks.add(attackEntity);
            }
        }

        for (int i = 0; i < expiredAttacks.size; i++) {
            engine.removeEntity(expiredAttacks.get(i));
        }
    }

    /**
     * Consumes player attack requests, respecting each player's attack cooldown.
     *
     * @param deltaTime elapsed time used to decrement cooldowns
     */
    private void createRequestedAttacks(float deltaTime){
        for(int i = 0; i < players.size(); i++){
            Entity player = players.get(i);

            AttackRequestComponent attackRequest = arm.get(player);
            AttackCooldownComponent cooldown = acm.get(player);
            cooldown.remaningTime = Math.max(
                0f,
                cooldown.remaningTime - deltaTime
            );

            if(!attackRequest.attack){
                continue;
            }

            attackRequest.attack = false;

            if(!cooldown.isReady()){
                continue;
            }

            createAttack(player);
            cooldown.remaningTime = ATTACK_COOLDOWN;
        }
    }

    /**
     * Creates a directional hitbox and the matching swing visual and sound.
     *
     * @param player player entity whose position and facing determine the attack
     */
    private void createAttack(Entity player) {

        PositionComponent playerPos = pm.get(player);
        FacingComponent playerFacing = fm.get(player);

        float attackX = playerPos.x;
        float attackY = playerPos.y;

        switch (playerFacing.direction) {
            case UP:
                attackY += ATTACK_OFFSET;
                break;
            case DOWN:
                attackY -= ATTACK_OFFSET;
                break;
            case LEFT:
                attackX -= ATTACK_OFFSET;
                break;
            case RIGHT:
                attackX += ATTACK_OFFSET;
                break;


        }

        Entity attackEntity = engine.createEntity();

        PositionComponent position =
            engine.createComponent(PositionComponent.class);

        position.x = attackX;
        position.y = attackY;

        ColliderComponent collider =
            engine.createComponent(ColliderComponent.class);

        AttackComponent attack =
            engine.createComponent(AttackComponent.class);

        attack.remainingTime = ATTACK_LIFETIME;
        attack.damageDelay = 0.06f;
        attack.damage = 1;

        attackEntity.add(position);
        attackEntity.add(collider);
        attackEntity.add(attack);

        engine.addEntity(attackEntity);

        float slashX = playerPos.x;
        float slashY = playerPos.y;

        /*
        We can adjust the sword swing positions in the cases below
         */
        switch (playerFacing.direction){
            case UP:
                collider.localBounds.set(
                    0,
                    0,
                    ATTACK_SIZE,
                    ATTACK_SIZE/2
                );
                slashX += 36f;
                slashY += 8f;
                break;

            case DOWN:
                collider.localBounds.set(
                    0,
                    0 + ATTACK_SIZE/2,
                    ATTACK_SIZE,
                    ATTACK_SIZE/2
                );
                slashX += 0f;
                slashY += 24f;
                break;

            case LEFT:
                collider.localBounds.set(
                    0 + ATTACK_SIZE/2,
                    0 ,
                    ATTACK_SIZE/2,
                    ATTACK_SIZE
                );
                slashX += 24f;
                slashY += 32f;
                break;

            case RIGHT:
                collider.localBounds.set(
                    0,
                    0 ,
                    ATTACK_SIZE/2,
                    ATTACK_SIZE
                );
                slashX += 8f;
                slashY += 0f;
                break;
        }

        swordSwingFactory.create(
            player,
            slashX,
            slashY,
            2,
            playerFacing.direction);

        audioManager.playSound(
            assets,
            AudioAssets.SWORD_SWING
        );
    }

    /**
     * Applies an attack to overlapping targets.
     *
     * <p>Each enemy can be damaged only once by a given hitbox. Barrels have no
     * health component, so an overlap removes the barrel entity immediately.</p>
     *
     * @param attackEntity entity carrying the attack's world-space collider
     * @param attack attack state tracking lifetime, damage, and enemies hit
     */
    private void damageOverlappingEnemies(
        Entity attackEntity, AttackComponent attack) {

        Rectangle attackBounds = getWorldBounds(attackEntity);

        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            if (attack.enemiesHit.contains(enemy, true)) {
                continue;
            }

            Rectangle enemyBounds = getWorldBounds(enemy);


            if (!attackBounds.overlaps(enemyBounds)) {
                continue;
            }

            HealthComponent enemyHealth = hm.get(enemy);

            if (enemyHealth.hp <= 0) {
                continue;
            }

            enemyHealth.hp -= attack.damage;
            attack.enemiesHit.add(enemy);

            applyKnockback(attackEntity, enemy);



            DamageFlashComponent flash = dfm.get(enemy);

            if(flash != null){
                flash.remainingTime = 0.12f;
                flash.flashing = true;
            }

        }

        brokenBarrels.clear();
        for (int i = 0; i < barrels.size(); i++) {
            Entity barrel = barrels.get(i);
            if (attackBounds.overlaps(getWorldBounds(barrel))) {
                brokenBarrels.add(barrel);
            }
        }

        for (int i = 0; i < brokenBarrels.size; i++) {
            engine.removeEntity(brokenBarrels.get(i));
        }
    }

    /** Applies a short impulse to an enemy away from the attack's position. */
    private void applyKnockback(Entity attackEntity, Entity enemy) {
        PositionComponent attackPos = pm.get(attackEntity);
        PositionComponent enemyPos = pm.get(enemy);

        KnockbackComponent knockback = km.get(enemy);

        if(knockback == null){
            return;
        }

        float dx = enemyPos.x - attackPos.x;
        float dy = enemyPos.y - attackPos.y;

        float length = (float) Math.sqrt(dx * dx + dy * dy);

        if(length == 0f){
            return;
        }

        knockback.velocityX = dx / length * 100f;
        knockback.velocityY = dy / length * 100f;
        knockback.remainingTime = 0.15f;
        knockback.active = true;
    }

    /** Returns the entity collider translated from local to world coordinates. */
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


