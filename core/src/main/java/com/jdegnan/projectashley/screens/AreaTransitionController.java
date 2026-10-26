package com.jdegnan.projectashley.screens;

import com.jdegnan.projectashley.level.LevelDefinition;
import com.jdegnan.projectashley.level.LevelManager;

public class AreaTransitionController {

    private final TransitionController transitionController;
    private final LevelManager levelManager;

    private LevelDefinition destination;

    private float spawnX;
    private float spawnY;

    private boolean transitionPending;
    private boolean waitingForLevelLoad;

    public AreaTransitionController(
        TransitionController transitionController,
        LevelManager levelManager) {

        this.transitionController = transitionController;
        this.levelManager = levelManager;
    }

    public void requestTransition(
        LevelDefinition destination,
        float spawnX,
        float spawnY) {

        this.destination = destination;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        if(destination == null){
            throw new IllegalArgumentException(
                "Destination level cannot be null."
            );
        }

        if(transitionPending || waitingForLevelLoad){
            return;
        }

        if(levelManager.isTransitioning()){
            return;
        }

        this.destination = destination;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

        transitionPending = true;
        transitionController.fadeOut();
    }

    public void update(){
        if(transitionPending &&
           transitionController.isFullyFadedOut()){
            levelManager.requestTransition(
                destination,
                spawnX,
                spawnY);

            transitionPending = false;
            waitingForLevelLoad = true;
        }

        if(waitingForLevelLoad &&
        !levelManager.isTransitioning() &&
        levelManager.getCurrentRuntime() != null){
            waitingForLevelLoad = false;
            destination = null;

            transitionController.fadeIn();
        }
    }

    public boolean isTransitionInProgress(){
        return transitionPending || waitingForLevelLoad;
    }
}
