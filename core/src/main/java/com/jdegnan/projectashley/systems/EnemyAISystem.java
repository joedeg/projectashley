package com.jdegnan.projectashley.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.jdegnan.projectashley.components.CollisionComponent;
import com.jdegnan.projectashley.components.EnemyAIComponent;
import com.jdegnan.projectashley.components.EnemyDeathComponent;
import com.jdegnan.projectashley.components.EnemyMovementComponent;
import com.jdegnan.projectashley.components.KnockbackComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.components.TagComponents.EnemyComponent;
import com.jdegnan.projectashley.components.TagComponents.PlayerComponent;

public class EnemyAISystem extends EntitySystem {
    private final ComponentMapper<PositionComponent> pm =
        ComponentMapper.getFor(PositionComponent.class);

    private final ComponentMapper<EnemyAIComponent> aim =
        ComponentMapper.getFor(EnemyAIComponent.class);

    private final ComponentMapper<EnemyMovementComponent> emm =
        ComponentMapper.getFor(EnemyMovementComponent.class);

    private ImmutableArray<Entity> enemies;
    private ImmutableArray<Entity> players;

    public EnemyAISystem() {

    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);

        enemies = engine.getEntitiesFor(
            Family.all(
                EnemyComponent.class,
                EnemyAIComponent.class,
                EnemyMovementComponent.class,
                PositionComponent.class
            ).get());

        players = engine.getEntitiesFor(
            Family.all(
                PlayerComponent.class,
                PositionComponent.class
            ).get());
    }

    @Override
    public void update(float deltaTime) {
        if (players.size() == 0) {
            return;
        }

        Entity player = players.first();
        PositionComponent playerPos = pm.get(player);

        for (int i = 0; i < enemies.size(); i++) {
            Entity enemy = enemies.get(i);

            EnemyDeathComponent death =
                enemy.getComponent(EnemyDeathComponent.class);

            if (death != null && death.dying) {
                continue;
            }

            KnockbackComponent knockback =
                enemy.getComponent(KnockbackComponent.class);

            if (knockback != null && knockback.active) {
                continue;
            }

            PositionComponent enemyPos = pm.get(enemy);
            EnemyAIComponent ai = aim.get(enemy);
            EnemyMovementComponent movement = emm.get(enemy);

            CollisionComponent collision =
                enemy.getComponent(CollisionComponent.class);

            float dx = playerPos.x - enemyPos.x;
            float dy = playerPos.y - enemyPos.y;

            float distanceSquared = dx * dx + dy * dy;

            float detectionRangeSquared = ai.detectionRange * ai.detectionRange;

            float loseInterestRangeSquared = ai.loseInterestRange * ai.loseInterestRange;

            if (ai.state == EnemyAIComponent.State.WANDERING) {
                if (distanceSquared <= detectionRangeSquared) {
                    ai.state = EnemyAIComponent.State.CHASING;
                }

            } else if (distanceSquared > loseInterestRangeSquared) {
                ai.state = EnemyAIComponent.State.WANDERING;

                chooseWanderingDirection(movement);

                ai.wanderInterval = MathUtils.random(1.5f, 3.5f);
                ai.wanderTimer = ai.wanderInterval;
            }

            if (ai.state == EnemyAIComponent.State.WANDERING) {
                ai.wanderTimer -= deltaTime;

                if (ai.wanderTimer < 0f) {
                    chooseWanderingDirection(movement);

                    ai.wanderInterval = MathUtils.random(1.5f, 3.5f);
                    ai.wanderTimer = ai.wanderInterval;
                }
            }


            if (ai.avoidingObstacle) {
                movement.directionX = ai.avoidanceDirectionX;
                movement.directionY = ai.avoidanceDirectionY;

                float avoidDx = enemyPos.x - ai.avoidanceStartX;
                float avoidDy = enemyPos.y - ai.avoidanceStartY;

                distanceSquared = avoidDx * avoidDx + avoidDy * avoidDy;

                // Continue avoiding until we have moved at least 64 pixels.
                if (distanceSquared >= 96f * 96f) {
                    ai.avoidingObstacle = false;
                }

            } else {
                boolean hitWall = collision != null &&
                    (collision.hitLeft ||
                        collision.hitRight ||
                        collision.hitTop ||
                        collision.hitBottom);

                if (hitWall && !ai.avoidingObstacle) {
                    ai.avoidingObstacle = true;
                    ai.avoidanceTimer = 1.5f;

                    Gdx.app.log(
                        "SlimeAI",
                        "STARTING avoidance; hitLeft=" + collision.hitLeft
                            + ", hitRight=" + collision.hitRight
                            + ", hitTop=" + collision.hitTop
                            + ", hitBottom=" + collision.hitBottom
                    );


                    if (collision.hitLeft || collision.hitRight) {
                        // Wall blocks horizontal movement.
                        // Choose the vertical direction that moves toward the player.
                        ai.avoidanceDirectionX = 0f;

                        if (Math.abs(dy) > 1f) {
                            ai.avoidanceDirectionY = dy > 0f ? 1f : -1f;
                        } else {
                            // Player is almost level: keep the previous preference.
                            ai.avoidanceDirectionY = ai.preferLeft ? 1f : -1f;
                        }
                    } else {
                        // Wall blocks vertical movement.
                        // Choose the horizontal direction that moves toward the player.
                        ai.avoidanceDirectionY = 0f;

                        if (Math.abs(dx) > 1f) {
                            ai.avoidanceDirectionX = dx > 0f ? 1f : -1f;
                        } else {
                            ai.avoidanceDirectionX = ai.preferLeft ? -1f : 1f;
                        }
                    }


                    ai.avoidanceStartX = enemyPos.x;
                    ai.avoidanceStartY = enemyPos.y;

                    ai.preferLeft = !ai.preferLeft;

                } else {
                    float length = (float) Math.sqrt(distanceSquared);

                    if (length > 0.001f) {
                        movement.directionX = dx / length;
                        movement.directionY = dy / length;
                    }
                }
            }
            if (collision != null && collision.hitRight) {
                Gdx.app.log(
                    "SlimeAI",
                    "avoiding=" + ai.avoidingObstacle
                        + ", timer=" + ai.avoidanceTimer
                        + ", avoidX=" + ai.avoidanceDirectionX
                        + ", avoidY=" + ai.avoidanceDirectionY
                        + ", preferLeft=" + ai.preferLeft
                );
            }
        }
    }


    private void chooseWanderingDirection(EnemyMovementComponent movement) {
        float angle = MathUtils.random(0f, MathUtils.PI2);

        movement.directionX = MathUtils.cos(angle);
        movement.directionY = MathUtils.sin(angle);
    }
}

