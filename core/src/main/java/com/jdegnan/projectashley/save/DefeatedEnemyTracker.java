package com.jdegnan.projectashley.save;

import com.badlogic.gdx.utils.Array;

public class DefeatedEnemyTracker {

    private final Array<String> defeatedEnemyIds = new Array<>();

    public void markDefeated(String enemyId){
        if(enemyId == null || enemyId.isBlank()){
            return;
        }

        if(!defeatedEnemyIds.contains(enemyId, false)){
            defeatedEnemyIds.add(enemyId);
        }

    }

    public Array<String> getDefeatedEnemyIds() {
        return new Array<>(defeatedEnemyIds);
    }

    public void restore(Array<String> savedIds){
        defeatedEnemyIds.clear();

        if(savedIds != null){
            for(String id : savedIds){
                markDefeated(id);
            }
        }
    }

    public boolean isDefeated(String enemyId){
        return enemyId != null
            && defeatedEnemyIds.contains(enemyId, false);
    }
}
