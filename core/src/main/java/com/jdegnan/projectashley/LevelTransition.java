package com.jdegnan.projectashley;

public class LevelTransition {
    private final LevelDefinition target;

    public LevelTransition(LevelDefinition target){
        this.target = target;
    }

    public LevelDefinition getTarget(){
        return target;
    }
}
