package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;

public class InteractionMessageComponent  implements Component, Pool.Poolable{

    public String message;
    public float remainingTime;

    public void show(String message, float duration)
    {
        this.message = message;
        remainingTime = duration;
    }

    public void clear(){
        message = null;
        remainingTime = 0;
    }

    @Override
    public void reset() {
        clear();
    }
}
