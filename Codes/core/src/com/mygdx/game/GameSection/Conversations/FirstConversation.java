package com.mygdx.game.GameSection.Conversations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.Entity.Wall;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.TriggerEvent;

public class FirstConversation extends TriggerEvent {

    private Label textLabel;
    private Label arrowLabel;
    private Image background;
    private String[] dialogueLines = {
            "Farmer:\n(sighing) It's such a shame. \nAll of my crops are dead. The polluted air has \ntaken its toll on them. I don't know what to do.",
            "Player:\nI'm sorry to hear that. Is there \nanything I can do to help?",
            "Farmer:\nActually, there is. You see, the air around \nhere is very polluted and it's killing my crops. ",
            "Farmer:\nBut if we can clean the air, my crops will \nhave a chance to grow again.",
            "Player:\nI understand. What can I do to help clean the air?",
            "Farmer:\nWell, there are a few things. First, we need \nto clean up the park. ",
            "Farmer:\nThere's a lot of rubbish there that's polluting \nthe air. If you could head there and recycle the rubbish \nto the correct bins, that would help a lot.",
            "Player:\nOkay, I'll do that. What's the next task?",
            "Farmer:\nThe next task is to go to the mansion. \nThere are a lot of electrical appliances and \ntaps that are left on, even when no one is using them. ",
            "Farmer:\nIf you could turn them off, that would help \nsave energy and reduce pollution.",
            "Player:\nGot it. I'll head to the mansion and \nturn off any appliances and taps that are left on.",
            "Farmer:\nThank you so much. With your help, we can save \nthe Earth and my crops will have a chance to grow again."
    };
    private int currentLine = 0;
    private float animationTotalTime = 2.0f; // in seconds

    private float animTimer;
    private boolean isAnimationComplete = true;
    public Label instructionLabel;
    private Label skipCutsceneLabel;

    private Wall blockWall;

    private boolean pauseDelay = false;

    public FirstConversation(Player player, String texture, int width, int height, float xPos, float yPos) {
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

        skipCutsceneLabel = new Label("Press F to skip cutscene", GameEngine.gameSkin);
        skipCutsceneLabel.setFontScale(0.8f); // 50% smaller font
        skipCutsceneLabel.setPosition(Gdx.graphics.getWidth() - skipCutsceneLabel.getPrefWidth() - 20f, 10f);
        skipCutsceneLabel.setVisible(false);

        GameWorld.getInstance().getStage().addActor(skipCutsceneLabel);
        GameWorld.getInstance().getStage().addActor(background);
        GameWorld.getInstance().getStage().addActor(textLabel);
        GameWorld.getInstance().getStage().addActor(arrowLabel);

        instructionLabel = new Label("Quest panel has been updated. \nCheck the quest panel (press Q)",
                GameEngine.gameSkin);
        instructionLabel.setFontScale(0.8f);
        instructionLabel.setColor(Color.WHITE);
        instructionLabel.setPosition(
                GameWorld.getInstance().getStage().getWidth() / 2 - instructionLabel.getPrefWidth() / 2,
                GameWorld.getInstance().getStage().getHeight() / 2 - instructionLabel.getPrefHeight() / 2);
        GameWorld.getInstance().getStage().addActor(instructionLabel);
        instructionLabel.setVisible(false);

        blockWall = new Wall(GameScreen.batch, 20, 100, 450, 275);
        if (!GameWorld.getInstance().myEntities.contains(blockWall.getEntity()))
            GameWorld.getInstance().myEntities.add(blockWall.getEntity());
    }

    @Override
    public void OnTriggerEnter() {

        if (currentLine < dialogueLines.length - 1) {
            skipCutsceneLabel.setVisible(true);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.F)) {
            currentLine = dialogueLines.length - 1;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    SimulateClick();
                }
            }, 1f);
        }

        if (isAnimationComplete) {
            GameScreen.PauseGame();
            textLabel.setVisible(true);
            background.setVisible(true);
            animTimer += Gdx.graphics.getDeltaTime();
            if (animTimer > animationTotalTime) {
                isAnimationComplete = false;
                animTimer = animationTotalTime;
                arrowLabel.setVisible(true);
            }
            String actuallyDisplayedText = "";
            int charactersToDisplay = (int) ((animTimer / animationTotalTime) * dialogueLines[currentLine].length());
            for (int i = 0; i < charactersToDisplay; i++) {
                actuallyDisplayedText += dialogueLines[currentLine].charAt(i);
            }
            if (!actuallyDisplayedText.equals(textLabel.getText().toString())) {
                textLabel.setText(actuallyDisplayedText);

                // Resize background based on text length
                background.setBounds(textLabel.getX() - 10, textLabel.getY() - 50, textLabel.getPrefWidth() + 50,
                        textLabel.getPrefHeight() + 20);

                // Update arrow label position
                arrowLabel.setPosition(textLabel.getX() + textLabel.getPrefWidth() + 5, textLabel.getY() - 40);
            }
        } else if (Gdx.input.justTouched() | Gdx.input.isKeyJustPressed(Input.Keys.F)) {
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
                skipCutsceneLabel.setVisible(false);
                instructionLabel.setVisible(true);

                // Quest1 is completed
                GameScreen.questPanel.completeQuest(1);
                GameScreen.questPanel.setQuestVisible(2, true);
                GameScreen.questPanel.setQuestVisible(3, true);

            }
        }

    }

    @Override
    public void OnTriggerExit() {
        instructionLabel.setVisible(false);
        skipCutsceneLabel.setVisible(false);
        textLabel.setVisible(false);
        background.setVisible(false);
        arrowLabel.setVisible(false);
        blockWall.getEntity().destroyFixture();
        GameWorld.blockPlayer = null;
    }

    public void update(float delta) {
        if (GameWorld.getInstance().getPlayer().getPlayerData("firstconversation")) {
            blockWall.getEntity().destroyFixture();
        }
        if (instructionLabel != null && instructionLabel.isVisible() && Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            GameScreen.ResumeGame();
            instructionLabel.setVisible(false);
        }
    }

    private void SimulateClick() {
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
            skipCutsceneLabel.setVisible(false);
            instructionLabel.setVisible(true);

            // Quest1 is completed
            GameScreen.questPanel.completeQuest(1);
            GameScreen.questPanel.setQuestVisible(2, true);
            GameScreen.questPanel.setQuestVisible(3, true);
        }
    }
}
