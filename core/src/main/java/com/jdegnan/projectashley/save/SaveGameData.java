package com.jdegnan.projectashley.save;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;

public class SaveGameData {

    public String levelId;
    public float playerX;
    public float playerY;
    public int playerHealth;
    public int playerMaxHealth;

    public ObjectMap<String, Integer> inventory = new ObjectMap<>();

    public Array<String> collectedPickupIds = new Array<>();

}
