package com.mygdx.game.GameSection.MiniGame_Rubbish;

import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.InventoryManager;
import com.mygdx.game.GameSection.TriggerEvent;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.Utils.Constants.BIN_TYPE;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;

/*
 * This class handles the behavior of triggering enter and exit of an this event
 */
public class RubbishBin extends TriggerEvent {

    // so far, this class will be created 4 times for the 4 different recycling bins
    private BIN_TYPE curBinType;
    Thread displayText;
    private int numOfTrashInBin = 0;
    private Label instructionLabel;

    public RubbishBin(BIN_TYPE binType, Player player, Texture texture, int width, int height, float xPos,
            float yPos) {
        super(player, texture, width, height, xPos, yPos);
        this.curBinType = binType;

        instructionLabel = new Label("Press I to show Inventory", GameEngine.gameSkin);
        instructionLabel.setFontScale(0.8f); // 50% smaller font
        instructionLabel.setPosition(Gdx.graphics.getWidth() - instructionLabel.getPrefWidth() - 20f, 10f); // adjust
                                                                                                            // position
                                                                                                            // as
                                                                                                            // desired
        instructionLabel.setVisible(false);
        GameWorld.getInstance().getStage().addActor(instructionLabel);
    }

    public BIN_TYPE GetBinType() {
        return this.curBinType;
    }

    public int GetNumOfTrashInBin() {
        return this.numOfTrashInBin;
    }

    private void HandleCorrectBinType() {

        String textToDisplay = "That's right!";
        // System.out.println(textToDisplay);

        Thread_DisplayText(textToDisplay);

        // Remove from inventory
        InventoryManager.getInstance().RemoveItem(InventoryManager.getInstance().selectedItem);

        // Increment num of trash in this bin
        numOfTrashInBin++;
    }

    private void HandleWrongBinType() {
        String textToDisplay = "You tried to put\n"
                + InventoryManager.getInstance().selectedItem.GetItemType() + " into " + curBinType + " bin!";
        // System.out.println(textToDisplay);

        Thread_DisplayText(textToDisplay);
    }

    private Window DisplayText(String textToDisplay) {

        Table questsTable = new Table();
        questsTable.setFillParent(true);

        // Creates a Scene2D window with title and skin.
        Window questsWindow = new Window("", GameEngine.gameSkin);

        // Makes sure the elements below title are positioned from top left.
        questsWindow.top().left();
        questsWindow.getTitleTable().padTop(48).padLeft(8);
        questsWindow.getTitleLabel().setFontScale(0.8f);

        // Prevents the window from being dragged.
        questsWindow.setMovable(false);

        // Example of 2 quests.
        questsWindow.add(createLabelForQuest(textToDisplay)).align(Align.left).padLeft(8).padTop(36);
        questsWindow.row();

        // questsWindow.add(createLabelForQuest("Pick up some
        // carrots")).align(Align.left).padLeft(8).padTop(8);

        // Adds the Window to a table of width 400x300.
        questsTable.add(questsWindow).width(400).height(300);
        GameWorld.getInstance().getStage().addActor(questsTable);

        // Sets the window invisible on start.
        questsWindow.setVisible(false);
        return questsWindow;
    }

    // Function to create a label.
    private Label createLabelForQuest(String text) {
        Label label = new Label(text, GameEngine.gameSkin);
        label.setFontScale(0.6f);
        return label;
    }

    // When called, this method will create a new window using DisplayText() method
    // and remove it after 1 second
    private void Thread_DisplayText(final String textToDisplay) {
        displayText = new Thread(new Runnable() {
            @Override
            public void run() {
                // Continue running if thread is not interrupted (interrupt in QuitThread())
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        Window currentWindow = DisplayText(textToDisplay);
                        currentWindow.setVisible(true);
                        // * 1000 to convert to ms
                        Thread.sleep(1 * 1000);
                        Thread.currentThread().interrupt();
                        currentWindow.addAction(Actions.removeActor());

                    } catch (InterruptedException e) {
                        throw new RuntimeException("Thread " +
                                "interrupted");
                    }
                }
            }
        });
        displayText.start();
    }

    @Override
    public void OnTriggerEnter() {
        instructionLabel.setVisible(true);

        if (InventoryManager.getInstance().selectedItem != null) {

            if (InventoryManager.getInstance().selectedItem.GetItemType().equals(curBinType)) {
                HandleCorrectBinType();
            } else {
                HandleWrongBinType();
            }

            InventoryManager.getInstance().selectedItem = null;
        }
    }

    @Override
    public void OnTriggerExit() {
        instructionLabel.setVisible(false);
    }
}
