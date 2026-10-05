package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class BrazierGateComponent implements Component, Pool.Poolable {

    public String puzzleId;
    public boolean opened;
    public int requiredBraziers = 1;

    public float feedbackTimer;
    public boolean feedbackActive;

    @Override
    public void reset() {
        puzzleId = null;
        opened = false;
        requiredBraziers = 1;
        feedbackTimer = 0;
        feedbackActive = false;
    }
}
