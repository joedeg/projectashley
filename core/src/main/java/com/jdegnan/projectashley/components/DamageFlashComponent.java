package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class DamageFlashComponent  implements Component, Pool.Poolable{

    public float remainingTime;
    public boolean flashing;
    @Override
    public void reset() {
        remainingTime = 0f;
        flashing = false;

    }
}
