package com.jdegnan.projectashley.save;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class SaveGameService {

    private final SaveGameSerializer serializer;
    private final FileHandle saveFile;

    public SaveGameService(){
        serializer = new SaveGameSerializer();
        saveFile = Gdx.files.local("saves/savegame.json");
    }

    public void save(SaveGameData data){
        saveFile.parent().mkdirs();
        String json = serializer.serialize(data);
        saveFile.writeString(json, false);
    }

    public SaveGameData load(){
        if(!saveFile.exists()){
            return null;
        }

        String json = saveFile.readString();
        return serializer.deserialize(json);
    }

    public boolean hasSave(){
        return saveFile.exists();
    }
}
