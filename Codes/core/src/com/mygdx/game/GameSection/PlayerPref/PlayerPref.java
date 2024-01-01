package com.mygdx.game.GameSection.PlayerPref;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Null;

// This class will handle the saving and retrieving of data that are saved in 
// PlayerPref, making use of structures like PlayerData and SoundData
public class PlayerPref {
    private static PlayerPref instance;

    private Json json = new Json();

    // Singleton
    public static PlayerPref getInstance() {

        if (instance == null)
            instance = new PlayerPref();

        return instance;
    }

    // Return Object of Data
    public Object GetData(String key, @Null Class knownType) {
        Preferences pref = Gdx.app.getPreferences(key);

        // Converts it to Object
        Object myData = json.fromJson(knownType, pref.getString(key));
        return myData;
    }

    // Method will be called like
    // SetData("player", playerData, PlayerData.class)
    public void SetData(String key, Object item, @Null Class knownType) {

        // Convert class into json string
        String itemString = json.toJson(item, knownType);

        // Get the current pref
        Preferences pref = Gdx.app.getPreferences(key);

        // Put key and item into pref
        pref.putString(key, itemString);

        // Update pref so it will persist
        pref.flush();

    }

    public void ResetData(String key) {
        Preferences prefs = Gdx.app.getPreferences(key);

        // Clears the values
        prefs.clear();
    }
}