package com.jdegnan.projectashley.level;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ObjectMap;

public class LevelObjectData {
    private final String name;
    private final String type;
    private final Vector2 position;
    private final Vector2 size;
    private final ObjectMap<String, Object> properties;

    public LevelObjectData(
        String name,
        String type,
        Vector2 position,
        Vector2 size,
        ObjectMap<String, Object> properties) {

        this.name = name;
        this.type = type;
        this.position = new Vector2(position);
        this.size = new Vector2(size);
        this.properties = properties;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return size;
    }

    public ObjectMap<String, Object> getProperties() {
        return properties;
    }

    public String getStringProperty(
        String name,
        String defaultValue){

        String value = (String) properties.get(name, String.class);

        return value != null ? value : defaultValue;
    }

    public float getFloatProperty(
        String name,
        float defaultValue){

        Float value = (Float) properties.get(name, Float.class);

        return value != null ? value : defaultValue;
    }

    public boolean getBooleanProperty(
        String name,
        boolean defaultValue){

        Boolean value = (Boolean) properties.get(name, Boolean.class);

        return value != null ? value : defaultValue;
    }
}
