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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameEngineSection.SoundManager;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;
import com.mygdx.game.Utils.Constants.SoundEffects;

public class TitleScreen extends AbstractScreen {

    private Stage stage;
    private Game game;

    public TitleScreen(Game aGame) {

        game = aGame;
        stage = new Stage();

        Texture backgroundTexture = new Texture(Gdx.files.internal("background_title.jpg"));
        Image backgroundImage = new Image(backgroundTexture);
        stage.addActor(backgroundImage);

        // Logo
        Texture logoTexture = new Texture(Gdx.files.internal("logooOOP.png"));
        Image logoImage = new Image(logoTexture);
        logoImage.setSize(logoTexture.getWidth(), logoTexture.getHeight());
        logoImage.setPosition(
                (Gdx.graphics.getWidth() - logoImage.getWidth()) / 2,
                (Gdx.graphics.getHeight() * 2 / 3 - logoImage.getHeight() / 2) + 70);
        stage.addActor(logoImage);

        TextButton playButton = new TextButton("Play!", GameEngine.gameSkin);
        playButton.setWidth(Gdx.graphics.getWidth() / 2);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - playButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 2 - playButton.getHeight() / 2);

        playButton.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // ScreenManager.getInstance().show(ScreenEnum.GAME_SCREEN, game, stage);
                ScreenManager.getInstance().show(ScreenEnum.LOADING_SCREEN, ScreenEnum.GAME_SCREEN, game);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // When press button, play sound effect
                SoundManager.getInstance().PlayEffect(SoundEffects.BTN_CLICK);
                return true;
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (fromActor != null)
                    if (fromActor.toString().contains("TextButton"))
                        SoundManager.getInstance().PlayEffect(SoundEffects.BTN_CLICK);
            }
        });
        stage.addActor(playButton);

        TextButton settingsButton = new TextButton("Settings", GameEngine.gameSkin);
        settingsButton.setWidth(Gdx.graphics.getWidth() / 2);
        settingsButton.setPosition(Gdx.graphics.getWidth() / 2 - settingsButton.getWidth() / 2,
                Gdx.graphics.getHeight() / 4 - settingsButton.getHeight() / 2);

        settingsButton.addListener(new InputListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ScreenManager.getInstance().show(ScreenEnum.SETTINGS_SCREEN, game);
            }

            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // When press button, play sound effect
                SoundManager.getInstance().PlayEffect(SoundEffects.BTN_CLICK);
                return true;
            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (fromActor != null)
                    if (fromActor.toString().contains("TextButton"))
                        SoundManager.getInstance().PlayEffect(SoundEffects.BTN_CLICK);
            }
        });
        stage.addActor(settingsButton);

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
