package com.mygdx.game.GameEngineSection.Screen;

import com.badlogic.gdx.Game;

// Set screen based on ScreenEnum passed in
public enum ScreenEnum {
    MAIN_MENU {
        public AbstractScreen setScreen(AbstractScreen screen, Game game) {
            game.setScreen(screen);
            return screen;
        }
    },
    GAME_SCREEN {
        public AbstractScreen setScreen(AbstractScreen screen, Game game) {
            game.setScreen(screen);
            return screen;
        }
    },
    SETTINGS_SCREEN {
        public AbstractScreen setScreen(AbstractScreen screen, Game game) {
            game.setScreen(screen);
            return screen;
        }
    },
    LOADING_SCREEN {
        public AbstractScreen setScreen(AbstractScreen screen, Game game) {
            game.setScreen(screen);
            return screen;
        }
    },
    CREDITS_SCREEN {
        public AbstractScreen setScreen(AbstractScreen screen, Game game) {
            game.setScreen(screen);
            return screen;
        }
    };

    // Returns AbstractScreen to set the current screen that is shown
    public abstract AbstractScreen setScreen(AbstractScreen screen, Game game);

}
