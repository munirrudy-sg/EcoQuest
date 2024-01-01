package com.mygdx.game.GameSection.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameEngineSection.SoundManager;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;
import com.mygdx.game.Utils.Constants.SoundEffects;

// Setting screen to click in Main Menu
public class SettingsScreen extends AbstractScreen {

    private Stage stage;
    private Game game;

    public SettingsScreen(Game aGame) {

        game = aGame;
        stage = new Stage();
        CreateSettingsUI();
    }

    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void CreateSettingsUI() {
        Texture backgroundTexture = new Texture(Gdx.files.internal("background_title.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        Label title = new Label("Settings Screen", GameEngine.gameSkin);
        title.setAlignment(Align.center);
        title.setY(Gdx.graphics.getHeight() * 2 / 3);
        title.setWidth(Gdx.graphics.getWidth());
        stage.addActor(title);

        Label lblBGVolumeSlider = new Label("BGM", GameEngine.gameSkin);
        lblBGVolumeSlider.setAlignment(Align.left);
        lblBGVolumeSlider.setX(Gdx.graphics.getWidth() / 2 - lblBGVolumeSlider.getWidth() / 2 - 250);
        lblBGVolumeSlider.setY(Gdx.graphics.getHeight() / 1.75f);
        lblBGVolumeSlider.setWidth(Gdx.graphics.getWidth());
        stage.addActor(lblBGVolumeSlider);

        Slider BGVolumeSlider = new Slider(0, 1, 0.1f, false, GameEngine.gameSkin);
        BGVolumeSlider.setWidth(Gdx.graphics.getWidth() / 8);
        BGVolumeSlider.setHeight(Gdx.graphics.getHeight() * 2);
        BGVolumeSlider.setPosition(Gdx.graphics.getWidth() / 2 - BGVolumeSlider.getWidth() / 2 - 200,
                Gdx.graphics.getHeight() / 2 - BGVolumeSlider.getHeight() / 2);
        // Set default value for slider
        BGVolumeSlider.setValue(SoundManager.getInstance().getBGVolume());
        BGVolumeSlider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getInstance().setBGVolume(((Slider) actor).getValue());
            }
        });
        stage.addActor(BGVolumeSlider);

        Label lblSFXVolumeSlider = new Label("SFX", GameEngine.gameSkin);
        lblSFXVolumeSlider.setAlignment(Align.left);
        lblSFXVolumeSlider.setX((Gdx.graphics.getWidth() / 2) - lblSFXVolumeSlider.getWidth() / 2 + 210
                - lblSFXVolumeSlider.getWidth());
        lblSFXVolumeSlider.setY(Gdx.graphics.getHeight() / 1.75f);
        lblSFXVolumeSlider.setWidth(Gdx.graphics.getWidth());
        stage.addActor(lblSFXVolumeSlider);

        Slider SFXVolumeSlider = new Slider(0, 1, 0.1f, false, GameEngine.gameSkin);
        SFXVolumeSlider.setWidth(Gdx.graphics.getWidth() / 8);
        SFXVolumeSlider.setHeight(Gdx.graphics.getHeight() * 2);
        SFXVolumeSlider.setPosition(Gdx.graphics.getWidth() / 2 - SFXVolumeSlider.getWidth() / 2 + 200,
                Gdx.graphics.getHeight() / 2 - SFXVolumeSlider.getHeight() / 2);
        // Set default value for slider
        SFXVolumeSlider.setValue(SoundManager.getInstance().getSFXVolume());
        SFXVolumeSlider.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SoundManager.getInstance().setSfxVolume(((Slider) actor).getValue());
            }
        });
        stage.addActor(SFXVolumeSlider);

        TextButton returnButton = new TextButton("Main Menu", GameEngine.gameSkin);
        returnButton.setWidth(Gdx.graphics.getWidth() / 4);
        returnButton.setPosition(Gdx.graphics.getWidth() / 2 - returnButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 4 - returnButton.getHeight() / 4);
        returnButton.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().show(ScreenEnum.MAIN_MENU, game);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (fromActor != null)
                    if (fromActor.toString().contains("TextButton"))
                        SoundManager.getInstance().PlayEffect(SoundEffects.BTN_CLICK);
            }
        });
        stage.addActor(returnButton);
    }

    public void resize(int width, int height) {

    }

    public void pause() {

    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        stage.dispose();
    }
}
