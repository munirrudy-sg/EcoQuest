package com.mygdx.game.GameEngineSection.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameEngineSection.SoundManager;
import com.mygdx.game.GameSection.Screen.*;

public class ScreenManager extends Thread {

    private static ScreenManager instance;
    private Game game;
    private ScreenFactory screenFactory;

    // Store global instances of screens, so progress persist throughout game scenes
    private TitleScreen titleScreen;
    private GameScreen gameScreen;
    private SettingsScreen settingsScreen;
    private CreditsScreen creditsScreen;
    private AbstractScreen curScreen;

    // singleton
    private ScreenManager() {
        // super();
        screenFactory = ScreenFactory.getInstance();
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }
        return instance;
    }

    public void initialize(Game game) {
        this.game = game;
    }

    public void show(ScreenEnum screenEnum, Game aGame) {
        // Show loading screen first
        // Then set screen
        curGame = aGame;
        curScreen = screenFactory.setScreen(screenEnum, aGame);
        SoundManager.getInstance().PlayBackgroundMusic(curScreen);
    }

    ScreenEnum enumChosen;
    Game curGame;
    Stage myStage;

    // public void show(ScreenEnum screenEnum, Game aGame, Stage stage) {
    // // Show loading screen first
    // // Then set screen
    // enumChosen = screenEnum;
    // curGame = aGame;
    // myStage = stage;

    // curScreen = enumChosen.setScreen(getScreen(enumChosen), curGame);
    // SoundManager.getInstance().PlayBackgroundMusic(curScreen);
    // }

    // Loading Screen
    public void show(ScreenEnum loadingScreen, ScreenEnum screenToLoad, Game aGame) {
        // Show loading screen first
        // Then set screen
        enumChosen = screenToLoad;
        curGame = aGame;
        // myStage = stage;

        curScreen = enumChosen.setScreen(getScreen(loadingScreen), curGame);
        SoundManager.getInstance().PlayBackgroundMusic(curScreen);
    }

    Dialog pauseDialog;

    private Dialog PauseDialog() {

        if (pauseDialog == null) {
            pauseDialog = new Dialog("", GameEngine.gameSkin) {

                {
                    text("Paused");
                    button("Back to Main Menu", 1L);
                    getButtonTable().row();
                    button("Resume", 2L);
                }

                protected void result(Object object) {
                    if (object.equals(1L)) {
                        ScreenManager.getInstance().show(ScreenEnum.MAIN_MENU, game);
                    }
                    if (object.equals(2L)) {
                    }
                }
            };

        }
        return pauseDialog;
    }

    // Returns an AbstractScreen based on ScreenEnum
    public AbstractScreen getScreen(ScreenEnum screenEnum) {
        switch (screenEnum) {
            case MAIN_MENU:
                if (titleScreen == null) {
                    titleScreen = new TitleScreen(game);
                }
                return titleScreen;

            case GAME_SCREEN:

                if (gameScreen == null) {
                    gameScreen = new GameScreen(game);
                }

                return gameScreen;
            case SETTINGS_SCREEN:
                if (settingsScreen == null) {
                    settingsScreen = new SettingsScreen(game);
                }
                return settingsScreen;
            case CREDITS_SCREEN:
                if (creditsScreen == null) {
                    creditsScreen = new CreditsScreen(game);
                }
                return creditsScreen;
            case LOADING_SCREEN:
                LoadingScreen loadingScreen = new LoadingScreen(game, enumChosen);
                return loadingScreen;
            default:
                return null;
        }
    }
}
