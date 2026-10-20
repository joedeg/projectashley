package com.jdegnan.projectashley.save;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;

public class SaveGameSerializer {

    private final Json json;

    public SaveGameSerializer() {
        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
    }

    public String serialize(SaveGameData data) {
        return json.toJson(data);
    }

    public SaveGameData deserialize(String jsonData) {
        return json.fromJson(SaveGameData.class, jsonData);
    }
}
