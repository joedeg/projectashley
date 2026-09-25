package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;


public interface LevelEntityFactory {
    Entity create(
        LevelObjectData object
    );
}
