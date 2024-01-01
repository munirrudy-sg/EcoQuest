package com.mygdx.game.Utils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Input;

public final class Constants {
    public static final float PPM = 32f; // Pixel Per Metric for screen
    public static final float SCALE = 2.0f; // Scale for windows size

    // Used for player movement input, need to change if we setting to dynamic key
    // binding
    public static final int KEY_UP = Input.Keys.W;
    public static final int KEY_DOWN = Input.Keys.S;
    public static final int KEY_LEFT = Input.Keys.A;
    public static final int KEY_RIGHT = Input.Keys.D;

    // The shapes that Entities can be
    public static final String POLYGON = "PolygonShape";
    public static final String CIRCLE = "CircleShape";

    // The type of sound effects available to play
    public static enum SoundEffects {
        BTN_CLICK,
        COLLIDE, WALK,
    };

    public static final String TEXTURE_PLATFORM = "grass.png";
    public static final String TEXTURE_METALBIN = "sprites/RubbishBins/METAL.png";
    public static final String TEXTURE_PLASTICBIN = "sprites/RubbishBins/PLASTIC.png";
    public static final String TEXTURE_PAPERBIN = "sprites/RubbishBins/PAPER.png";
    public static final String TEXTURE_GLASSBIN = "sprites/RubbishBins/GLASS.png";
    public static final String EMPTY_IMAGE = "empty.png";
    public static final String TEXTURE_RAIN = "raindrop.png";
    public static final String TEXTURE_WALLSIDE = "img46.png";

    // File extensions for checking if asset exists
    public static final List<String> EXTENSIONS = new ArrayList<String>() {
        {
            add(".png");
            add(".jpg");
        }
    };

    // Used for saving and getting player saved files
    public static final String PLAYER = "player";
    public static final String SOUND = "sound";

    // Bin types
    public static enum BIN_TYPE {
        GLASS, PAPER, PLASTIC, METAL
    };
}
