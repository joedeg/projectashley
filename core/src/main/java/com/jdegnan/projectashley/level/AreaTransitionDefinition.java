package com.jdegnan.projectashley.level;

public class AreaTransitionDefinition {

    private final String destinationLevelId;
    private final float spawnX;
    private final float spawnY;

    public AreaTransitionDefinition(
        String destinationLevelId,
        float spawnX,
        float spawnY
    ){
        if(destinationLevelId == null || destinationLevelId.isBlank()){
            throw new IllegalArgumentException(
                "Destination level ID cannot be null or blank."
            );
        }

        this.destinationLevelId = destinationLevelId;
        this.spawnX = spawnX;
        this.spawnY = spawnY;

    }

    public String getDestinationLevelId(){
        return destinationLevelId;
    }

    public float getSpawnX(){
        return spawnX;
    }

    public float getSpawnY(){
        return spawnY;
    }

}

