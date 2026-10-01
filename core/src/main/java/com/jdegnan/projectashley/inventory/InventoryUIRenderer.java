package com.jdegnan.projectashley.inventory;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ObjectMap;
import com.jdegnan.projectashley.assets.Assets;
import com.jdegnan.projectashley.assets.ItemAssetRegistry;
import com.jdegnan.projectashley.components.InventoryComponent;
import com.jdegnan.projectashley.components.InventoryUIComponent;

public class InventoryUIRenderer {

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final ShapeRenderer shapeRenderer;
    private final BitmapFont font;
    private final Assets assets;
    private final ItemAssetRegistry itemAssetRegistry;


    public InventoryUIRenderer(
        SpriteBatch batch,
        Assets assets,
        ItemAssetRegistry itemAssetRegistry) {

        this.batch = batch;
        this.assets = assets;
        this.itemAssetRegistry = itemAssetRegistry;

        this.camera = new OrthographicCamera();
        this.shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    public void render(Entity player) {

        if (player == null) {
            return;
        }

        InventoryUIComponent ui =
            player.getComponent(InventoryUIComponent.class);

        if (ui == null || !ui.visible) {
            return;
        }

        InventoryComponent inventory =
            player.getComponent(InventoryComponent.class);

        if (inventory == null) {
            return;
        }

        drawPanel(inventory, ui);

        ensureValidSelection(ui, inventory);
    }

    private void drawPanel(
        InventoryComponent inventory,
        InventoryUIComponent ui) {

        // Panel background
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.8f);
        shapeRenderer.rect(40, 40, 300, 240);
        shapeRenderer.end();

        // Panel border
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 1, 1);
        shapeRenderer.rect(40, 40, 300, 240);
        shapeRenderer.end();

        // Item icon and text
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, "Inventory", 60, 275);

        int slotIndex = 0;
        float slotSpacingX = 85f;
        float slotSpacingY = 70f;
        float startX = 70f;
        float startY = 200f;

        for (ItemType item : ItemType.values()) {
            int count = inventory.getCount(item);

            if (count <= 0) {
                continue;
            }

            int column = slotIndex % 3;
            int row = slotIndex / 3;

            float itemX = startX + column * slotSpacingX;
            float itemY = startY - row * slotSpacingY;


            ItemDefinition definition =
                itemAssetRegistry.get(item);

            Texture texture = assets.get(definition.getAsset());

            TextureRegion region =
                new TextureRegion(texture);

            batch.draw(region, itemX, itemY, 32, 32);

            font.draw(
                batch,
                definition.getDisplayName(),
                itemX,
                itemY -10);

            font.draw(
                batch,
                "x" + count,
                itemX + 35,
                itemY + 10
            );

            slotIndex++;
        }
        batch.end();

        // Selection highlight
        drawSelectionHighlight(inventory, ui);

        batch.setProjectionMatrix(camera.combined);

        // Selection Info
        drawSelectedItemInfo(inventory, ui);
    }

    private void drawSelectionHighlight(
        InventoryComponent inventory,
        InventoryUIComponent ui) {

        if (ui.selectedItem == null) {
            return;
        }

        int slotIndex = 0;

        float startX = 70f;
        float startY = 200f;

        float slotSpacingX = 85f;
        float slotSpacingY = 70f;

        for (ItemType item : ItemType.values()) {

            int count = inventory.getCount(item);

            if (count < 0) {
                continue;
            }

            int column = slotIndex % 3;
            int row = slotIndex / 3;

            float itemX = startX + column * slotSpacingX;
            float itemY = startY - row * slotSpacingY;

            if (item == ui.selectedItem) {
                shapeRenderer.setProjectionMatrix(camera.combined);

                shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

                shapeRenderer.setColor(1, 1, 0, 1);

                shapeRenderer.rect(
                    itemX - 4,
                    itemY - 9,
                    72, 48);

                shapeRenderer.end();

                return;

            }
            slotIndex++;
        }
    }

    private void ensureValidSelection(
        InventoryUIComponent ui,
        InventoryComponent inventoryComponent
    ) {
        if (ui.selectedItem != null &&
            inventoryComponent.has(ui.selectedItem)) {
            return;
        }

        ui.selectedItem = null;

        for (ObjectMap.Entry<ItemType, Integer> entry :
            inventoryComponent.items.entries()) {
            if (entry.value > 0) {
                ui.selectedItem = entry.key;
                break;
            }
        }
    }

    private void drawSelectedItemInfo(
        InventoryComponent inventory,
        InventoryUIComponent ui
    ) {
        if (ui.selectedItem == null) {
            return;
        }

        if (inventory.getCount(ui.selectedItem) <= 0) {
            return;
        }

        ItemDefinition definition =
            itemAssetRegistry.get(ui.selectedItem);

        batch.begin();

        font.draw(
            batch,
            definition.getDisplayName(),
            60,
            100
        );

        font.draw(
            batch,
            definition.getDescription(),
            60,
            75
        );

        batch.end();
    }

    public void dispose() {
        font.dispose();
        shapeRenderer.dispose();
    }
}
