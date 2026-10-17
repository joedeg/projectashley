package com.jdegnan.projectashley.loot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class EnemyLootTable {

    private final Array<LootDrop> drops = new Array<>();

    public void addDrop(LootDrop drop) {
        if (drop == null) {
            throw new IllegalArgumentException(
                "drop cannot be null."
            );
        }
        drops.add(drop);
    }

    public LootDrop roll() {
        float totalWeight = 0f;

        for (LootDrop drop : drops) {
            totalWeight += drop.getWeight();
        }

        if (totalWeight <= 0f) {
            return null;
        }

        float roll = MathUtils.random() * totalWeight;

        for (LootDrop drop : drops) {
            roll -= drop.getWeight();

            if(roll < 0f){
                return drop;
            }
        }

        return null;
    }
}

