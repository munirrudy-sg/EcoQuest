package com.mygdx.game.GameEngineSection.Screen;

import com.badlogic.gdx.Game;

public class ScreenFactory {

    private static ScreenFactory instance;

    private ScreenFactory() {
    }

    public static ScreenFactory getInstance() {
        if (instance == null) {
            instance = new ScreenFactory();
        }
        return instance;
    }

    AbstractScreen screen;

    public AbstractScreen setScreen(ScreenEnum screenEnum, Game game) {

        screen = ScreenManager.getInstance().getScreen(screenEnum);
        // switch (screenEnum) {
        // case MAIN_MENU:
        // // screen = new TitleScreen(game);
        // screen= ScreenManager.getInstance().getScreen(screenEnum);
        // break;
        // case GAME_SCREEN:
        // // screen = new GameScreen(game);
        // screen= ScreenManager.getInstance().getScreen(screenEnum);
        // break;
        // case SETTINGS_SCREEN:
        // screen = new SettingsScreen(game);
        // break;
        // case LOADING_SCREEN:
        // screen = new LoadingScreen(game, screenEnum);
        // break;
        // case CREDITS_SCREEN:
        // screen = new CreditsScreen(game);
        // break;
        // default:
        // throw new IllegalArgumentException("Unknown screen type: " + screenEnum);
        // }

        game.setScreen(screen);
        return screen;
    }
}