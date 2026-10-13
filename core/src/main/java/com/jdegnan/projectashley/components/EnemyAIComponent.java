package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class EnemyAIComponent implements Component, Pool.Poolable{

    public enum State{
        WANDERING,
        CHASING
    }

    public State state = State.WANDERING;

    public float detectionRange = 120f;

    public float loseInterestRange = 180f;

    public float wanderTimer = 0f;

    public float wanderInterval = 0f;

    public boolean avoidingObstacle = false;

    public float avoidanceTimer = 0f;

    public float avoidanceDirectionX = 0f;
    public float avoidanceDirectionY = 0f;

    public float avoidanceStartX = 0;
    public float avoidanceStartY = 0;

    public boolean preferLeft = true;

    @Override
    public void reset() {
        state = State.WANDERING;
        detectionRange = 120f;
        loseInterestRange = 180f;
        wanderTimer = 0f;
        wanderInterval = 0f;
        avoidingObstacle = false;
        avoidanceTimer = 0f;
        avoidanceDirectionX = 0f;
        avoidanceDirectionY = 0f;
        avoidanceStartX = 0;
        avoidanceStartY = 0;
        preferLeft = true;
    }
}
