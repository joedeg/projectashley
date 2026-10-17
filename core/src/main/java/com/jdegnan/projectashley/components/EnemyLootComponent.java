package com.jdegnan.projectashley.components;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import com.jdegnan.projectashley.loot.EnemyLootTable;

public class EnemyLootComponent implements Component, Pool.Poolable{

    public EnemyLootTable lootTable;

    public float dropChance;
    @Override
    public void reset() {
        lootTable = null;
    }
}
