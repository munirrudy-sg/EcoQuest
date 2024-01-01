package com.mygdx.game.GameSection.MiniGame_Rubbish;

import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.TriggerEvent;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.Entity.Collectibles.CollectibleManager;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.Utils.Constants.BIN_TYPE;

public class TriggerBinGame extends TriggerEvent {

    private CollectibleManager collectibleManager;
    private boolean hasTriggered, game1Complete = false;
    private HashMap<BIN_TYPE, Integer> allTrash = new HashMap<BIN_TYPE, Integer>();

    // The position to start triggering Game1
    private Vector2 rubbishRegion = new Vector2(2, -1);
    private Vector2 recyclingBins = new Vector2(7, 5);
    private boolean showDialogue;

    // Text with animation
    private Label textLabel;
    private Label arrowLabel, instructionLabel;
    private Image background;
    private String[] dialogueLines = {
            "Farmer:\nThis was a beautiful place..  \nNow it is ruined with garbage and I can\'t do this alone.",
            "Farmer:\nWill you help me pick up all the garbage around the area?",
            "You:\nOf course I will!",
            "Farmer:\nAfter you pick up the garbage,\nopen up your Inventory and sort them into the bins",
    };
    private int currentLine = 0;
    private float animationTotalTime = 2.0f; // in seconds
    private float animTimer;
    private boolean isAnimationComplete = true;

    public TriggerBinGame(Player player, String texture, int width, int height, float xPos, float yPos) {
        super(player, texture, width, height, xPos, yPos);
        textLabel = new Label("", GameEngine.gameSkin);
        textLabel.setPosition(86, 88);
        textLabel.setFontScale(0.8f);
        textLabel.setColor(Color.WHITE);

        arrowLabel = new Label(">", GameEngine.gameSkin);
        arrowLabel.setFontScale(0.8f);
        arrowLabel.setColor(Color.WHITE);
        arrowLabel.setVisible(false);

        background = new Image(
                new NinePatchDrawable(new NinePatch(new Texture("black_dialogue.9.png"), 10, 10, 10, 10)));
        background.setVisible(false);

        GameWorld.getInstance().getStage().addActor(background);
        GameWorld.getInstance().getStage().addActor(textLabel);
        GameWorld.getInstance().getStage().addActor(arrowLabel);

        instructionLabel = new Label(
                "Pick the garbage and\nsort them in the recycling corner.\nCheck the Inventory (press I)\n",
                GameEngine.gameSkin);
        instructionLabel.setFontScale(0.8f);
        instructionLabel.setColor(Color.WHITE);
        instructionLabel.setPosition(
                GameWorld.getInstance().getStage().getWidth() / 2 -
                        instructionLabel.getPrefWidth() / 2,
                GameWorld.getInstance().getStage().getHeight() / 2 -
                        instructionLabel.getPrefHeight() / 2);
        GameWorld.getInstance().getStage().addActor(instructionLabel);
        instructionLabel.setVisible(false);
    }

    public void setCollectibleManager(CollectibleManager collectibleManager) {
        this.collectibleManager = collectibleManager;
    }

    // Checks if game completed or not
    public void checkGameComplete(BIN_TYPE binType, int numOfRubbish) {
        // Add all rubbish to list of bintype, then sum it up to check if it equals to

        if (numOfRubbish != 0)
            allTrash.put(binType, numOfRubbish);

        int totalNumOfTrashCollected = 0;
        for (Map.Entry<BIN_TYPE, Integer> set : allTrash.entrySet())
            totalNumOfTrashCollected += set.getValue();

        // If total trash collected == 16, means game1 completed. total 16 collectibles
        if (totalNumOfTrashCollected == collectibleManager.getTotalNumOfCollectibles())
            game1Complete = true;

    }

    // Returns the boolean on whether game1 is completed or not
    public boolean IsGame1Completed() {
        return game1Complete;
    }

    // Send camera to location with a lerp
    private void Thread_SendCameraTo(final Vector2 location) {

        Thread thread_SendCamTo = new Thread(new Runnable() {
            @Override
            public void run() {

                // Continue running if thread is not interrupted (interrupt in QuitThread())
                while (!Thread.currentThread().isInterrupted()) {
                    try {

                        // * 100 to convert to ms
                        Thread.sleep(1 * 10);

                        // Send camera to location with lerp
                        GameWorld.getInstance().cameraUpdate(GameWorld.getInstance().getCamera(), location,
                                0.01f,
                                "?");

                        // Get current camera distance to location
                        float camDistToLocationX = Math
                                .abs(GameWorld.getInstance().getCamera().position.x - (location.x * 32));
                        float camDistToLocationY = Math
                                .abs(GameWorld.getInstance().getCamera().position.y - (location.y * 32));

                        // If camera reaching location, exit this thread and move on
                        if (camDistToLocationX <= 5.0f && camDistToLocationY <= 5.0f) {
                            showDialogue = true;
                            Thread.currentThread().interrupt();
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException("Thread " +
                                "interrupted");
                    }
                }
            }
        });
        thread_SendCamTo.start();
    }

    @Override
    public void OnTriggerEnter() {
        // 1. Pause player
        // 2. Send camera to bin
        // 3. Show some dialogue
        // 4. Resume player
        if (!GameWorld.getInstance().getPlayer().getPlayerData("game1")) {
            if (!hasTriggered) {
                hasTriggered = true;
                GameScreen.PauseGame();
                // Send camera to rubbish region first before setting showDialogue to true
                Thread_SendCameraTo(rubbishRegion);
            }
        }
        if (showDialogue) {
            if (isAnimationComplete) {
                if (currentLine == dialogueLines.length - 1) {
                    Thread_SendCameraTo(recyclingBins);
                }
                textLabel.setVisible(true);
                background.setVisible(true);
                animTimer += Gdx.graphics.getDeltaTime();
                if (animTimer > animationTotalTime) {
                    isAnimationComplete = false;
                    animTimer = animationTotalTime;
                    arrowLabel.setVisible(true);
                }
                String actuallyDisplayedText = "";
                int charactersToDisplay = (int) ((animTimer / animationTotalTime)
                        * dialogueLines[currentLine].length());
                for (int i = 0; i < charactersToDisplay; i++) {
                    actuallyDisplayedText += dialogueLines[currentLine].charAt(i);
                }
                if (!actuallyDisplayedText.equals(textLabel.getText().toString())) {
                    textLabel.setText(actuallyDisplayedText);

                    // Resize background based on text length
                    background.setBounds(textLabel.getX() - 10, textLabel.getY() - 50,
                            textLabel.getPrefWidth() + 50, textLabel.getPrefHeight() + 20);

                    // Update arrow label position
                    arrowLabel.setPosition(textLabel.getX() + textLabel.getPrefWidth() + 5,
                            textLabel.getY() - 40);
                }
            } else if (Gdx.input.justTouched()) {
                if (currentLine < dialogueLines.length - 1) {
                    currentLine++;
                    animTimer = 0;
                    isAnimationComplete = true;
                    arrowLabel.setVisible(false);
                } else {
                    // End of dialogue, you can resume the game and hide the textLabel, background,
                    // and arrowLabel here
                    textLabel.setVisible(false);
                    background.setVisible(false);
                    arrowLabel.setVisible(false);
                    instructionLabel.setVisible(true);
                    showDialogue = false;
                    GameScreen.ResumeGame();
                }
            }
        }

    }

    @Override
    public void OnTriggerExit() {
        instructionLabel.setVisible(false);
    }
}
