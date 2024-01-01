package com.mygdx.game.GameSection;

import java.util.HashMap;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameSection.Entity.Item;

/*
 * This class manages inventory and display items in it
 */
public class InventoryManager {

    private static InventoryManager instance;

    // Use Window
    private Window inventoryPanel;
    private boolean isShowingInventory, refreshInventoryUI;
    private HashMap<String, Item> items = new HashMap<String, Item>();

    // Singleton
    public static InventoryManager getInstance() {
        if (instance == null)
            instance = new InventoryManager();
        return instance;
    }

    // If click on item in inventory, will populate this var to get the Item that is
    // selected
    public Item selectedItem;

    // Updates inventory
    public void Update() {
        RefreshInventoryUI();
        ToggleInventoryPanel();
    }

    // "initialize" inventory's item
    public void SetItems(HashMap<String, Item> items) {
        this.items = items;
        refreshInventoryUI = true;
    }

    // Add an item into items Dictionary
    public void AddItem(Item item) {
        String itemName = item.GetItemName();

        if (this.items.get(itemName) != null) {
            // Exists, add to count
            // System.out.println("Existed, Qty: " + items.get(itemName).GetItemQty());
            this.items.get(itemName).AddItemQty(1);
            return;
        }

        this.items.put(itemName, item);
        System.out.println(itemName + "(" + item.GetItemQty() + ") added!");
        refreshInventoryUI = true;
    }

    public void RemoveItem(Item item) {
        String itemName = item.GetItemName();

        if (this.items.get(itemName) != null) {
            // Exists, remove from inventory
            this.items.remove(itemName);
            refreshInventoryUI = true;
            return;
        }

        System.out.println(itemName + "(" + item.GetItemName() + ") not found!");
    }

    // Get items Dictionary
    public HashMap<String, Item> GetItems() {
        return items;
    }

    // Refreshes Inventory UI
    private void RefreshInventoryUI() {
        if (refreshInventoryUI && inventoryPanel != null) {
            inventoryPanel.addAction(Actions.removeActor()); // Remove inventory UI from stage (stops rendering)
            inventoryPanel = null; // Sets to null
            refreshInventoryUI = false; // Dont allow it to loop again
        }
    }

    /**
     * Toggle visibility of inventory panel
     */
    public void ToggleInventoryPanel() {
        // Press I to toggle boolean
        if (Gdx.input.isKeyJustPressed(Input.Keys.I))
            isShowingInventory = !isShowingInventory;

        // If inventoryPanel has no parent (stage), add to stage
        if (!InventoryPanel().hasParent())
            GameWorld.getInstance().getStage().addActor(InventoryPanel());

        // Set visibility of inventoryPanel based on boolean
        InventoryPanel().setVisible(isShowingInventory);
    }

    /**
     * Prints out Player Inventory
     */
    private void PrintInventory() {
        System.out.println("--------------------\r\n");
        System.out.println("Items in Player Inventory");
        System.out.println("Name\tTexture\t\tQty");
        for (Entry<String, Item> set : items.entrySet()) {
            Item temp = set.getValue();
            System.out.println(temp.GetItemName() + "\t" + temp.GetItemTexture() + "\t\t" + temp.GetItemQty());
        }
        System.out.println("--------------------\r\n");
    }

    /**
     * Creates a inventoryPanel
     */
    private Window InventoryPanel() {

        float panelWidth = 400;
        float panelHeight = Gdx.graphics.getHeight();

        if (inventoryPanel == null) {
            inventoryPanel = new Window("", GameEngine.gameSkin);

            inventoryPanel.setSize(panelWidth, panelHeight);
            inventoryPanel.setPosition(Gdx.graphics.getWidth() - panelWidth, 0);
            // inventoryPanel.setDebug(true, true); // Draw the debug lines

            // create a new table
            Table table = new Table();

            // Set the position of the table inside the window
            table.setY(panelHeight - 200);
            table.setWidth(panelWidth);
            table.setHeight(panelHeight);

            // First row with labels
            table.add(new Label("Inventory",
                    GameEngine.gameSkin)).pad(10).colspan(3).height(100);
            table.row();

            // Number of items in user inventory
            int numOfItems = GetItems().size();

            // Shortfall amount to make it multiples of 3
            int shortfall = (numOfItems % 3) == 0 ? 0 : (3 - (numOfItems % 3));

            // Get total placeholder items needed
            int totalPlaceholder = numOfItems + shortfall;

            int index = 1;
            // PrintInventory();
            // Iterating HashMap through for loop
            for (Entry<String, Item> set : items.entrySet()) {
                final Item temp = set.getValue();
                Texture tempTexture = new Texture(temp.GetItemTexture());
                final Image tempImage = new Image(tempTexture);
                tempImage.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        System.out.println(temp.GetItemName() + " was clicked.");
                        System.out.println(x + ", " + y);
                        System.out.println("Click end");
                        selectedItem = temp;
                    }
                });

                // tempImage.addListener(new DragListener() {
                // public void drag(InputEvent event, float x, float y, int pointer) {
                // tempImage.moveBy(x - tempImage.getWidth() / 2, y - tempImage.getHeight() /
                // 2);
                // }
                // });
                if (totalPlaceholder - index < 3) {

                    // Add placeholder image
                    if (index > numOfItems) {
                        // table.add(new Image(new
                        // Texture(Gdx.files.internal("background.jpg")))).pad(10).size(64, 64)
                        // .expand()
                        // .top();
                    }
                    // Add item
                    else {
                        table.add(tempImage).pad(10).size(64, 64).expand().top();
                    }

                } else {
                    table.add(tempImage).pad(10).size(64, 64).expandX();
                }
                // System.out.println(tempImage.getX() + ", " + tempImage.getY());
                // Go to new row every 3 items
                if (index % 3 == 0) {
                    table.row();
                }
                index++;
            }
            // Adding items to inventory panel
            // for (int i = 1; i <= totalPlaceholder; i++) {
            // // Current item is

            // if (totalPlaceholder - i < 3) {

            // // Add placeholder image
            // if (i > numOfItems) {
            // // table.add(new Image(new
            // // Texture(Gdx.files.internal("background.jpg")))).pad(10).size(64, 64)
            // // .expand()
            // // .top();
            // }
            // // Add item
            // else {
            // table.add(new Image(item)).pad(10).size(64, 64)
            // .expand()
            // .top();
            // }

            // } else {
            // table.add(new Image(item)).pad(10).size(64, 64).expandX();
            // }

            // // Go to new row every 3 items
            // if (i % 3 == 0) {
            // table.row();
            // }
            // }

            // Add the table to the window
            inventoryPanel.add(table).expand().fillX().width(panelWidth - 40).height(panelHeight - 40);

        }
        return inventoryPanel;
    }
}
