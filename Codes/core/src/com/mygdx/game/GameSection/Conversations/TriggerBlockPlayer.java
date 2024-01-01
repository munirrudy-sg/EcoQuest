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
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.TriggerEvent;

public class TriggerBlockPlayer extends TriggerEvent {

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
    public Label instructionLabel;
    private boolean pauseDelay = false;

    public TriggerBlockPlayer(Player player, String texture, int width, int height, float xPos, float yPos) {
        super(player, texture, width, height, xPos, yPos);

        instructionLabel = new Label("Check the quest panel (press Q)",
                GameEngine.gameSkin);
        instructionLabel.setFontScale(0.8f);
        instructionLabel.setColor(Color.WHITE);
        instructionLabel.setPosition(
                GameWorld.getInstance().getStage().getWidth() / 2 - instructionLabel.getPrefWidth() / 2,
                GameWorld.getInstance().getStage().getHeight() / 2 - instructionLabel.getPrefHeight() / 2);
        GameWorld.getInstance().getStage().addActor(instructionLabel);
        instructionLabel.setVisible(false);
    }

    @Override
    public void OnTriggerEnter() {
        if (!pauseDelay) {
            instructionLabel.setVisible(true);
            GameScreen.PauseGame();
        }

        if (this != null && Gdx.input.isKeyPressed(Input.Keys.Q)) {
            GameScreen.ResumeGame();
            instructionLabel.setVisible(false);
            pauseDelay = true;
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    pauseDelay = false;
                }
            }, 2f);
        }

    }

    @Override
    public void OnTriggerExit() {
        instructionLabel.setVisible(false);
    }

}
