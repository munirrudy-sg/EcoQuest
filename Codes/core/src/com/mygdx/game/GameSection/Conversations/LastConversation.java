package com.mygdx.game.GameSection.Conversations;

import com.badlogic.gdx.Game;
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
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.TriggerEvent;

public class LastConversation extends TriggerEvent {

    private Label textLabel;
    private Label arrowLabel;
    private Image background;
    private Game game;
    private String[] dialogueLines = {
            "Farmer:\nI can't believe my eyes!\nMy crops are finally growing\nand thriving!",
            "Player:\nI'm glad to see that your\ncrops are doing well. It seems that our efforts to\nreduce pollution have paid off!",
            "Farmer:\nYes, indeed! I can't thank\nyou enough for all your help. You've cleaned up\nthe park and turned off those unnecessary\nlamps at the mansion.",
            "Farmer:\nYour actions have truly made\na difference, not only to my crops, but also to\nthe entire community. The air is cleaner,\nand everyone is feeling the benefits.",
            "Player:\nI'm happy that I could help.\nIt was a team effort, and I've learned a lot\nabout the importance of taking care\nof our environment.",
            "Farmer:\nI'm so grateful for your dedication\nand hard work. Your actions have shown me\nthat there's hope for the future, and\nthat we can turn things around if\nwe all work together.",
            "Player:\nI'm glad that I could help make a difference.\nLet's continue to\nwork together to protect our environment and\nensure a healthy future for everyone.",
            "Farmer:\nAbsolutely! With people like you around,\nI have faith that we can create a better world\n for ourselves and future generations.\n Thank you once again, and take care!"
    };
    private int currentLine = 0;
    private float animationTotalTime = 2.0f; // in seconds

    private float animTimer;
    private boolean isAnimationComplete = true;
    private Label skipCutsceneLabel;

    public LastConversation(Game game, Player player, String texture, int width, int height, float xPos, float yPos) {
        super(player, texture, width, height, xPos, yPos);

        this.game = game;

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
                background.setBounds(textLabel.getX() - 10, textLabel.getY() - 60, textLabel.getPrefWidth() + 50,
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
                ScreenManager.getInstance().show(ScreenEnum.CREDITS_SCREEN, game);

            }
        }
    }

    @Override
    public void OnTriggerExit() {

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
        }
    }

}
