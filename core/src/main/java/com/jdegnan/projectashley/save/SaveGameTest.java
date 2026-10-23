package com.jdegnan.projectashley.save;

public class SaveGameTest {

    public static void run(){
        SaveGameSerializer serializer = new SaveGameSerializer();

        SaveGameData original = new SaveGameData();
        original.levelId = "adventure";
        original.playerX = 128.0f;
        original.playerY = 96.0f;
        original.playerHealth = 2;
        original.playerMaxHealth = 3;
        original.inventory.put("FOREST_KEY", 1);
        original.inventory.put("HEART", 2);

        String json = serializer.serialize(original);
        System.out.println("Saved JSON");
        System.out.println(json);

        SaveGameData restored = serializer.deserialize(json);

        boolean passed =
            "adventure".equals(restored.levelId) &&
                restored.playerX == 128.0f &&
                restored.playerY == 96.0f &&
                restored.playerHealth == 2 &&
                restored.playerMaxHealth == 3 &&
                restored.inventory.get("FOREST_KEY") == 1 &&
                restored.inventory.get("HEART") == 2;

        SaveGameService saveService = new SaveGameService();

        SaveGameData fileData = new SaveGameData();
        fileData.levelId = "adventure";
        fileData.playerX = 200f;
        fileData.playerY = 150f;
        fileData.playerHealth = 3;
        fileData.playerMaxHealth = 3;
        fileData.inventory.put("FOREST_KEY", 1);

        saveService.save(fileData);

        SaveGameData loadedData = saveService.load();


    }
}
