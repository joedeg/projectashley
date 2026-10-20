package com.jdegnan.projectashley.save;

import com.badlogic.gdx.utils.Array;

public class CollectedPickupTracker {

    private final Array<String> collectedIds = new Array<>();

    public void record(String id){
        if(id == null || id.isBlank()) {
            return;
        }

        if(!collectedIds.contains(id, false)){
            collectedIds.add(id);
        }
    }

    public Array<String> getCollectedIds(){
        return collectedIds;
    }

    public void restore(Array<String> ids){
        collectedIds.clear();

        if(ids == null){
            return;
        }

        for(String id : ids){
            record(id);
        }
    }
}
