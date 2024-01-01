package com.mygdx.game.GameSection;

import java.util.Dictionary;
import java.util.Hashtable;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;

// This class will be instantiated in LoadingScreen to load assets needed for GameWorld
public class GameAssets {

    public Dictionary<String, Texture> textures = new Hashtable<String, Texture>();
    private static GameAssets instance;
    private boolean loadingCompleted = false;

    public static GameAssets getInstance() {
        if (instance == null)
            instance = new GameAssets();

        return instance;
    }

    // Load texture according screen loaded
    public void LoadTextures(ScreenEnum screenEnum, String textureToLoad[]) {
        for (String texture : textureToLoad) {
            switch (screenEnum) {
                case MAIN_MENU:
                    break;
                case GAME_SCREEN:
                    AddTexture(texture);
                    break;
                case SETTINGS_SCREEN:
                    break;
                default:
                    break;
            }
        }
        // Texture loading completed
        loadingCompleted = true;
    }

    private void AddTexture(String textureName) {
        textures.put(textureName, new Texture(textureName));
    }

    public boolean hasCompleted() {
        return this.loadingCompleted;
    }
}
