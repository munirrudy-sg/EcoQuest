package com.mygdx.game.GameSection.MiniGame_TurnOff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.TriggerEvent;

public class Teleporter extends TriggerEvent {
    private float nextXpos;
    private float nextYpos;
    private Image blackScreen;
    private Label instructionLabel;

    public Teleporter(Player player, String texture, int width, int height, float xPos, float yPos, float nextXpos, float nextYpos) {
        super(player, texture, width, height, xPos, yPos);
        this.nextXpos = nextXpos;
        this.nextYpos = nextYpos;

        blackScreen = new Image(new Texture(Gdx.files.internal("black_dialogue.jpg")));
        blackScreen.setColor(Color.BLACK);
        blackScreen.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        blackScreen.setVisible(false);
        GameWorld.getInstance().getStage().addActor(blackScreen);

        instructionLabel = new Label("Press E to enter", GameEngine.gameSkin);
        instructionLabel.setFontScale(0.8f); // 50% smaller font
        instructionLabel.setPosition(Gdx.graphics.getWidth() - instructionLabel.getPrefWidth() - 20f, 10f); // adjust position as desired
        instructionLabel.setVisible(false);
        GameWorld.getInstance().getStage().addActor(instructionLabel);
    }

    @Override
    public void OnTriggerEnter() {
        instructionLabel.setVisible(true);

        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            // Show the black screen
            blackScreen.setVisible(true);

            // Fade in the black screen
            blackScreen.addAction(Actions.sequence(
                    Actions.alpha(0),
                    Actions.fadeIn(0.5f),
                    Actions.delay(0.5f),
                    Actions.fadeOut(0.5f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            blackScreen.setColor(new Color(0, 0, 0, 0));
                        }
                    })
            ));

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    // Hide the black screen after 0.5 seconds
                    Player.setPosition(nextXpos/32, nextYpos/32);
                }
            }, 0.5f);

            GameWorld.LampCounter.showLampCounter(true);
        }
    }

    @Override
    public void OnTriggerExit() {
        GameWorld.LampCounter.showLampCounter(true);
        instructionLabel.setVisible(false);
    }
}
