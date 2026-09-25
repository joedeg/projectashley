package com.jdegnan.projectashley.level;

public class LevelTransition {
    private final LevelDefinition target;

    public LevelTransition(LevelDefinition target){
        this.target = target;
    }

    public LevelDefinition getTarget(){
        return target;
    }
}
