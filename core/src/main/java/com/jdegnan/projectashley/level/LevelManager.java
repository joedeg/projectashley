package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.jdegnan.projectashley.components.DamageCooldownComponent;
import com.jdegnan.projectashley.components.DoorComponent;
import com.jdegnan.projectashley.components.HealthComponent;
import com.jdegnan.projectashley.components.ItemPickupComponent;
import com.jdegnan.projectashley.components.PositionComponent;
import com.jdegnan.projectashley.factories.PlayerFactory;
import com.jdegnan.projectashley.save.CollectedPickupTracker;
import com.jdegnan.projectashley.systems.CameraFollowSystem;

/**
 * Manages the lifecycle and transitions of game levels.
 *
 * <p>The {@code LevelManager} coordinates asynchronous level loading via {@link LevelLoader},
 * switches active {@link LevelRuntime} instances, updates camera bounds, spawns and repositions
 * the player entity, and maintains item pickup collection persistence.</p>
 */
public class LevelManager {

    private final PooledEngine engine;
    private final LevelLoader levelLoader;
    private LevelRuntime currentRuntime;
    private boolean transitionRequested;

    private final PlayerFactory playerFactory;
    private final CameraFollowSystem cameraFollowSystem;
    private Entity player;
    private final CollectedPickupTracker collectedPickupTracker;

    /**
     * Constructs a new {@code LevelManager} with the necessary dependencies.
     *
     * @param engine                 the Ashley ECS engine
     * @param levelLoader            loader responsible for level assets and runtime construction
     * @param playerFactory          factory for creating the player entity
     * @param cameraFollowSystem     camera system updated with level map bounds
     * @param collectedPickupTracker tracker for keeping record of collected pickups across levels
     */
    public LevelManager(PooledEngine engine,
                        LevelLoader levelLoader,
                        PlayerFactory playerFactory,
                        CameraFollowSystem cameraFollowSystem,
                        CollectedPickupTracker collectedPickupTracker) {
        this.engine = engine;
        this.levelLoader = levelLoader;
        this.playerFactory = playerFactory;
        this.cameraFollowSystem = cameraFollowSystem;
        this.collectedPickupTracker = collectedPickupTracker;
    }

    /**
     * Loads the initial level when no level is currently active.
     *
     * @param definition the definition of the initial level to load
     * @throws IllegalStateException if a level is already active
     */
    public void loadInitialLevel(LevelDefinition definition) {
        if (currentRuntime != null) {
            throw new IllegalStateException(
                "A level is already active."
            );
        }

        requestTransition(definition);
    }

    /**
     * Requests a transition to a new level definition.
     *
     * @param definition the level definition to transition to
     * @throws IllegalArgumentException if definition is null
     * @throws IllegalStateException    if a transition is already in progress
     */
    public void requestTransition(LevelDefinition definition) {
        if (definition == null) {
            throw new IllegalArgumentException(
                "Level definition cannot be null."
            );
        }

        if (transitionRequested) {
            throw new IllegalStateException(
                "A level transition is already requested."
            );
        }

        levelLoader.requestLoad(definition);

        transitionRequested = true;
    }

    /**
     * Updates asynchronous level loading and performs the level transition once loading completes.
     * <p>Should be called frame-by-frame on the main update loop.</p>
     */
    public void update() {
        if (!transitionRequested) {
            return;
        }

        if (!levelLoader.update()) {
            return;
        }

        LevelRuntime newRuntime = levelLoader.build();

        switchTo(newRuntime);

        transitionRequested = false;
    }

    /**
     * Switches the active level runtime to the newly loaded runtime, updating camera bounds,
     * player position, and removing previously collected item pickups.
     *
     * @param newRuntime the newly constructed {@link LevelRuntime}
     */
    private void switchTo(LevelRuntime newRuntime) {
        LevelRuntime oldRuntime = currentRuntime;

        newRuntime.addTo(engine);
        currentRuntime = newRuntime;

        cameraFollowSystem.setMapBounds(newRuntime.getMapBounds());

        spawnPlayer(newRuntime);

        if (oldRuntime != null) {
            oldRuntime.removeFrom(engine);
            oldRuntime.dispose();
        }

        applyCollectedPickupState();
    }

    /**
     * Requests a reload transition for the currently active level.
     *
     * @throws IllegalStateException if no level is currently active
     */
    public void reloadCurrentLevel() {
        if (currentRuntime == null) {
            throw new IllegalStateException(
                "No active level to reload."
            );
        }

        requestTransition(currentRuntime.getDefinition());
    }

    /**
     * Spawns the player entity at the level spawn point if not already created,
     * or repositions the existing player entity.
     *
     * @param runtime the active level runtime containing player spawn coordinates
     */
    private void spawnPlayer(LevelRuntime runtime) {
        Vector2 spawn = runtime.getDefinition().getPlayerSpawn();

        if (player == null) {
            player = playerFactory.create(spawn.x, spawn.y);
            engine.addEntity(player);
        } else {
            setPlayerPosition(player, spawn.x, spawn.y);
        }
    }

    /**
     * Respawns the player at the current level's spawn location, restoring max health
     * and granting a brief damage cooldown period.
     */
    public void respawnPlayer() {
        if (player == null || currentRuntime == null) {
            return;
        }

        Vector2 spawn = currentRuntime
            .getDefinition()
            .getPlayerSpawn();

        setPlayerPosition(player, spawn.x, spawn.y);

        HealthComponent health =
            player.getComponent(HealthComponent.class);

        if (health != null) {
            health.hp = health.maxHp;
        }

        DamageCooldownComponent cooldown =
            player.getComponent(DamageCooldownComponent.class);

        if (cooldown != null) {
            cooldown.remaining = 2f;
        }

        System.out.println("Floki has been respawned");
    }

    /**
     * Sets the position coordinates on the player's {@link PositionComponent}.
     *
     * @param player the player entity
     * @param x      the target x-coordinate
     * @param y      the target y-coordinate
     */
    private void setPlayerPosition(Entity player, float x, float y) {
        PositionComponent position = player.getComponent(PositionComponent.class);

        position.x = x;
        position.y = y;
    }

    /**
     * Inspects item pickup entities in the current engine and removes any that have
     * already been collected according to the {@link CollectedPickupTracker}.
     */
    public void applyCollectedPickupState() {
        ImmutableArray<Entity> pickups =
            engine.getEntitiesFor(
                Family.all(
                    ItemPickupComponent.class
                ).get()
            );

        Array<Entity> toRemove = new Array<>();

        for (Entity entity : pickups) {
            ItemPickupComponent pickup =
                entity.getComponent(ItemPickupComponent.class);

            if (pickup != null
            && collectedPickupTracker.getCollectedIds()
                .contains(pickup.id, false)) {
                toRemove.add(entity);
            }
        }

        for (Entity entity : toRemove) {
            engine.removeEntity(entity);
        }
    }

    /**
     * Cancels an in-progress level transition request and cancels asset loading.
     */
    public void cancelTransition() {
        if (!transitionRequested) {
            return;
        }
        levelLoader.cancel();
        transitionRequested = false;
    }

    /**
     * Gets the currently active level runtime.
     *
     * @return the active {@link LevelRuntime}, or {@code null} if no level is loaded
     */
    public LevelRuntime getCurrentRuntime() {
        return currentRuntime;
    }

    /**
     * Gets the active player entity.
     *
     * @return the player {@link Entity}, or {@code null} if not yet spawned
     */
    public Entity getPlayer() {
        return player;
    }

    /**
     * Checks whether a level transition is currently in progress.
     *
     * @return {@code true} if transitioning, {@code false} otherwise
     */
    public boolean isTransitioning() {
        return transitionRequested;
    }

    /**
     * Gets the current level loading progress.
     *
     * @return loading progress as a float between 0.0 and 1.0 (returns 1.0 if not transitioning)
     */
    public float getLoadingProgress() {
        if (!transitionRequested) {
            return 1.0f;
        }
        return levelLoader.getProgress();
    }

    public Array<String> getOpenedDoorIds() {
        Array<String> openIds = new Array<>();

        ImmutableArray<Entity> doors = engine.getEntitiesFor(
            Family.all(
                DoorComponent.class
            ).get());

        for(Entity entity : doors){
            DoorComponent door = entity.getComponent(DoorComponent.class);

            if(door.opened && door.id != null && !door.id.isBlank()){
                openIds.add(door.id);
            }
        }

        return openIds;
    }

    public ImmutableArray<Entity> getEntitiesWithDoor(){
        return engine.getEntitiesFor(
            Family.all(DoorComponent.class).get()
        );
    }

}
