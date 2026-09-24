package com.jdegnan.projectashley.level;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.maps.MapObject;

public interface LevelEntityFactory {
    Entity create(
        MapObject object
    );
}
