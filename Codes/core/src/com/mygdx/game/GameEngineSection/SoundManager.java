package com.mygdx.game.GameEngineSection;

import java.util.Hashtable;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameSection.PlayerPref.PlayerPref;
import com.mygdx.game.GameSection.PlayerPref.SoundData;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.GameSection.Screen.SettingsScreen;
import com.mygdx.game.GameSection.Screen.TitleScreen;
import com.mygdx.game.Utils.Constants.SoundEffects;
// import com.mygdx.game.Utils.Constants.*;
import static com.mygdx.game.Utils.Constants.SOUND;

// This class will handle all Sound related events including BGM and SFX
public class SoundManager {

    private static SoundManager instance;

    // Sound object for background
    private Sound bgSound;

    // Hashtable of <SoundEffects, Sound> to get and play Sound object
    private Hashtable<SoundEffects, Sound> sfxSounds = new Hashtable<SoundEffects, Sound>();

    private long bgMusicID = -1;

    // Controls the volume for background and effects respectively
    private float bgVolume;
    private float sfxVolume;

    // Sound data to be played (Contains volume for BGM and SFX)
    private SoundData soundData;

    // Singleton
    public static SoundManager getInstance() {
        if (instance == null)
            instance = new SoundManager();

        return instance;
    }

    public SoundManager() {
        Init();
    }

    private void Init() {
        soundData = (SoundData) PlayerPref.getInstance().GetData(SOUND, SoundData.class);

        // Check for null
        if (soundData == null) {
            soundData = new SoundData();
            soundData.BGMVolume = 1.0f;
            soundData.SFXVolume = 1.0f;
            PlayerPref.getInstance().SetData(SOUND, soundData, SoundData.class);
        }

        // Init volume
        soundData = (SoundData) PlayerPref.getInstance().GetData(SOUND, SoundData.class);
        bgVolume = soundData.BGMVolume;
        sfxVolume = soundData.SFXVolume;

        // Populate effectSounds
        sfxSounds.put(SoundEffects.BTN_CLICK,
                Gdx.audio.newSound(Gdx.files.internal("sound/btn_click.mp3")));

        sfxSounds.put(SoundEffects.COLLIDE,
                Gdx.audio.newSound(Gdx.files.internal("sound/btn_click.mp3")));

        sfxSounds.put(SoundEffects.WALK,
                Gdx.audio.newSound(Gdx.files.internal("sound/walk.mp3")));
    }

    // Play different background music based on screen
    public void PlayBackgroundMusic(AbstractScreen currentScreen) {

        // If bgMusicID is not default value, means it was already assigned, thus stop
        // playing
        if (bgMusicID != -1)
            bgSound.stop(bgMusicID);

        // Different background music for different screen
        if (currentScreen instanceof TitleScreen || currentScreen instanceof SettingsScreen) {

            bgSound = Gdx.audio.newSound(Gdx.files.internal("sound/bgmusic_title.mp3"));
            bgMusicID = bgSound.play(bgVolume);

        } else if (currentScreen instanceof GameScreen) {

            bgSound = Gdx.audio.newSound(Gdx.files.internal("sound/bgmusic_game.mp3"));
            bgMusicID = bgSound.play(bgVolume);
        } else {
            return;
        }
        // Background music should be looped
        bgSound.setLooping(bgMusicID, true);
    }

    // Pass in SoundEffects from Util.Constants.java
    public void PlayEffect(SoundEffects effect) {
        sfxSounds.get(effect).play(sfxVolume);
    }

    // Setting and Getting Background Volume values
    public void setBGVolume(float newVolume) {
        this.bgVolume = newVolume;
        bgSound.setVolume(bgMusicID, this.bgVolume);
        SaveSoundSetting();
    }

    public float getBGVolume() {
        return this.bgVolume;
    }

    // Setting and Getting SFX Volume values
    public void setSfxVolume(float newVolume) {
        this.sfxVolume = newVolume;
        SaveSoundSetting();
    }

    public float getSFXVolume() {
        return this.sfxVolume;
    }

    public void SaveSoundSetting() {
        soundData.BGMVolume = this.bgVolume;
        soundData.SFXVolume = this.sfxVolume;

        PlayerPref.getInstance().SetData(SOUND, soundData, SoundData.class);
    }
}
