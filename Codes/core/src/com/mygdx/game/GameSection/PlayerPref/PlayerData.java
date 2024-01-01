package com.mygdx.game.GameSection.PlayerPref;

import java.util.HashMap;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameSection.Entity.Item;

public class PlayerData {

    // Player Position
    public Vector2 playerPos;
    public HashMap<String, Item> items;
    public boolean hasCompletedFirstConvo, hasCompletedGame1, hasCompletedGame2;
}
