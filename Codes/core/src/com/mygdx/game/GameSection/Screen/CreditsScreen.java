package com.mygdx.game.GameSection.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameSection.PlayerPref.PlayerPref;
import com.mygdx.game.GameSection.SnakeGame.Snake;
import static com.mygdx.game.Utils.Constants.*;

public class CreditsScreen extends AbstractScreen {

    private Game game;
    private Stage stage;
    private String creditsText;
    private Snake snakeGame;
    private FitViewport snakeGameViewport;

    public CreditsScreen(Game aGame) {
        game = aGame;
        stage = new Stage();
        createCredits();

        // createWhiteBox();
        createSnakeGame();
        createBackToMainMenuLabel();
    }

    private void createSnakeGame() {
        snakeGame = new Snake();
        snakeGame.setSize(570, 360);
        float paddingLeft = Gdx.graphics.getWidth() * 3 / 8 + 30f + 20f; // Add extra 20f for a gap between the credits
                                                                         // label and the Snake game
//        snakeGame.setPosition(641, Gdx.graphics.getWidth() / 8);

        snakeGameViewport = new FitViewport(570, 360);
        snakeGameViewport.setScreenBounds(641, Gdx.graphics.getWidth() / 8, 570, 360);
        snakeGameViewport.apply();

        paddingLeft = 20f; // Add extra padding from the left side of the WhiteBox
        snakeGame.setPosition(641 + paddingLeft, Gdx.graphics.getWidth() / 8);

         stage.addActor(snakeGame);
    }

    private void createBackToMainMenuLabel() {
        Label label = new Label("Press ESC to exit Game", GameEngine.gameSkin);
        label.setFontScale(0.5f); // 50% smaller font
        label.setPosition(Gdx.graphics.getWidth() - label.getPrefWidth() - 20f, 10f); // adjust position as desired
        stage.addActor(label);
    }

    private Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        return new Texture(pixmap);
    }

    private void createWhiteBox() {
        Texture whiteTexture = createWhiteTexture();
        Image whiteBox = new Image(whiteTexture);
        whiteBox.setSize(570, 360);
        float paddingRight = Gdx.graphics.getWidth() - 640;
        whiteBox.setPosition(641, Gdx.graphics.getWidth() / 8);
        stage.addActor(whiteBox);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        snakeGameViewport.apply(); // Set the Snake game viewport's bounds
        snakeGame.draw();

        stage.getViewport().apply(); // Set the Credits viewport's bounds
        stage.act(delta);
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            // ScreenManager.getInstance().show(ScreenEnum.MAIN_MENU, game);
            // Reset player data since player completed the whole game
            PlayerPref.getInstance().ResetData(PLAYER);
            Gdx.app.exit();
        }
    }

    private void createCredits() {
        creditsText = "We hope you enjoyed playing our game! While the credits roll, feel free to play the Snake game on the side for some extra fun :)\n\n"
                + "This game was developed as part of the INF1009 Object Oriented Programming Module with the purpose of inspiring kids to take an interest in sustainable living.\n\n"
                + "Asset Packs:\n"
                + "Sprites: \"Cozy People\" by Shubibubi\n"
                + "Main Game Map: \"Cozy Farm\" by Shubibubi\n"
                + "Interior of House: \"Cozy Interior\" by Shubibubi\n"
                + "Recycling Bins: \"Modern Office\" by LimeZu\n\n"
                + "Sound:\n"
                + "Background Music: \"Song Name\" by Artist\n"
                + "Sound FX: Artist\n\n"
                + "Developed by a dedicated team of five students:\n"
                + "Aloysius Tay Hui Ming\n"
                + "Munir bin Rudy Herman\n"
                + "Muhammad Hidayah bin Mohd Latif\n"
                + "Tan Ying Xi\n"
                + "Sureshbabu Sujanraj\n\n"
                + "We would like to extend our heartfelt gratitude to Prof. Graham for your invaluable guidance throughout the development of this game.\n\n"
                + "Thank you for playing!\n\n"
                + "- The End -";

        Label creditsLabel = new Label(creditsText, GameEngine.gameSkin);
        creditsLabel.setFontScale(0.8f); // 20% smaller font
        creditsLabel.setAlignment(Align.center);
        creditsLabel.setWidth(Gdx.graphics.getWidth() * 3 / 8); // 3/8 of the screen width
        float paddingLeft = 80f; // Adjust paddingLeft value for desired padding.
        creditsLabel.setPosition(paddingLeft, -creditsLabel.getPrefHeight());
        creditsLabel.setWrap(true);

        float rollDuration = 50f; // 20% slower scrolling speed
        creditsLabel.addAction(Actions.forever(Actions.sequence(
                Actions.moveTo(paddingLeft, Gdx.graphics.getHeight(), 0),
                Actions.moveTo(paddingLeft, -creditsLabel.getPrefHeight(), 0),
                Actions.moveTo(paddingLeft, Gdx.graphics.getHeight(), rollDuration))));

        stage.addActor(creditsLabel);
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