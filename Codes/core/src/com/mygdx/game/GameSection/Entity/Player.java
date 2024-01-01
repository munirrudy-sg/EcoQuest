package com.mygdx.game.GameSection.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEngineSection.BehaviourManagement.Behavior;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;
import com.mygdx.game.GameSection.InventoryManager;
import com.mygdx.game.GameSection.PlayerPref.PlayerData;
import com.mygdx.game.GameSection.PlayerPref.PlayerPref;

import static com.mygdx.game.Utils.Constants.*;

import java.util.HashMap;

public class Player implements Behavior {

    static Entity<Object> myE;

    private Vector2 defaultStartingPos = new Vector2(-3.53f, 8.62f);
    private int LEFT, RIGHT, UP, DOWN;
    private int width, height;
    private float moveSpeed = 0.08f;
    private boolean controlsEnabled = true;

    // Data to be saved
    private PlayerData playerData;
    private boolean hasCompletedFirstConvo, hasCompletedGame1, hasCompletedGame2;

    private Thread autoSaveThread;

    public Player(SpriteBatch batch, int width, int height, int xPos, int yPos) {
        String textureFolder = "sprites/Main Character/";

        myE = new EntityBuilder<>(width, height, xPos, yPos)
                .addTexture(textureFolder + "main_character_up.png", textureFolder + "main_character_down.png",
                        textureFolder + "main_character_right.png",
                        textureFolder + "main_character_left.png")
                .addCollision()
                .addBody(CIRCLE, 10, false)
                .build();

        myE.setType(this);
        this.width = width;
        this.height = height;
        // Default movement keys will be LEFT RIGHT UP DOWN
        SetMovementKeys(Input.Keys.UP, Input.Keys.LEFT, Input.Keys.DOWN, Input.Keys.RIGHT);
        InitPlayer();
    }

    private void InitPlayer() {
        // PlayerPref.getInstance().ResetData(PLAYER);
        playerData = (PlayerData) PlayerPref.getInstance().GetData(PLAYER, PlayerData.class);

        // Check for null, means no previously saved data
        if (playerData == null)
            LoadDefaultPlayer();

        // Set player position to previously saved position
        // setPosition(-3.53f, 8.62f);
        setPosition(playerData.playerPos.x, playerData.playerPos.y);

        // Set sprite's bound for detection of overlapping
        myE.getSprite().setBounds(getPosition().x * PPM - this.width / 2, getPosition().y * PPM - this.height / 2,
                this.width, this.height);

        // Set inventory manager to playerData.items
        InventoryManager.getInstance().SetItems(playerData.items);
        hasCompletedFirstConvo = playerData.hasCompletedFirstConvo;
        hasCompletedGame1 = playerData.hasCompletedGame1;
        hasCompletedGame2 = playerData.hasCompletedGame2;

        // Begin thread of auto saving
        AutosaveProgress();
    }

    // This method will be called when player has no previously saved data
    private void LoadDefaultPlayer() {
        System.out
                .println("Player.java (LoadDefaultPlayer()) | Player is null, will create default values for Player ");
        playerData = new PlayerData(); // Create Data class

        // Set default starting position for player
        playerData.playerPos = defaultStartingPos; // Set playerPos

        // Initialize
        HashMap<String, Item> newItems = new HashMap<String, Item>();
        playerData.items = newItems;
        playerData.hasCompletedFirstConvo = hasCompletedFirstConvo;
        playerData.hasCompletedGame1 = hasCompletedGame1;
        playerData.hasCompletedGame2 = hasCompletedGame2;

        // Save player data
        PlayerPref.getInstance().SetData(PLAYER, playerData, PlayerData.class);
    }

    public boolean getPlayerData(String data) {
        if (data.equals("firstconversation")) {
            return hasCompletedFirstConvo;
        } else if (data.equals("game1")) {
            return hasCompletedGame1;
        } else if (data.equals("game2")) {
            return hasCompletedGame2;
        }
        return false;
    }

    public Entity<?> getEntity() {
        return myE;
    }

    public Vector2 getPosition() {
        return myE.getPosition();
    }

    public static void setPosition(float x, float y) {
        myE.setPosition(x, y);
    }

    public void setCompletedQuest(int questNumber) {

        switch (questNumber) {
            case 1:
                hasCompletedFirstConvo = true;
                break;
            case 2:
                hasCompletedGame1 = true;
                break;
            case 3:
                hasCompletedGame2 = true;
                break;
            default:
                throw new IllegalArgumentException("Invalid quest number: " + questNumber);
        }
    }

    // If user press alt-f4 to close game, stop thread as well.
    private void QuitThread() {
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) &&
                Gdx.input.isKeyPressed(Input.Keys.F4)) {

            // Save one last time before quitting thread
            SavePlayerData();
            // Stop auto save thread
            StopAutoSaveThread();
        }
    }

    // Auto save player's progress every 5seconds
    private void AutosaveProgress() {
        final int sleepPeriod = 5;
        autoSaveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // Continue running if thread is not interrupted (interrupt in QuitThread())
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        SavePlayerData();

                        // * 1000 to convert to ms
                        Thread.sleep(sleepPeriod * 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException("Thread " +
                                "interrupted");
                    }
                }
            }
        });
        autoSaveThread.start();
    }

    public void StopAutoSaveThread() {
        // Interrupt this thread to stop it
        this.autoSaveThread.interrupt();
    }

    private void SavePlayerData() {
        playerData.playerPos = getPosition(); // Get current player pos
        playerData.items = InventoryManager.getInstance().GetItems();// Get whatever that is in inventory
        playerData.hasCompletedFirstConvo = hasCompletedFirstConvo;
        playerData.hasCompletedGame1 = hasCompletedGame1;
        playerData.hasCompletedGame2 = hasCompletedGame2;
        PlayerPref.getInstance().SetData(PLAYER, playerData, PlayerData.class);
    }

    // Hit update (vera)
    // Destroy whatever player hit with (if other is collidable)
    @Override
    public void hit(Entity<?> other) {
        other.destroyFixture();
    }

    // Re-bind the keys used to move player
    public void SetMovementKeys(int up, int left, int down, int right) {
        UP = up;
        LEFT = left;
        DOWN = down;
        RIGHT = right;
    }

    // Input update (vera)
    // Make sure the gravity set for the world it will spawn in, to be 0f
    @Override
    public void moveLeft() {
        setPosition(getPosition().x - moveSpeed, getPosition().y);
    }

    @Override
    public void moveRight() {
        setPosition(getPosition().x + moveSpeed, getPosition().y);
    }

    @Override
    public void moveUp() {
        setPosition(getPosition().x, getPosition().y + moveSpeed);
    }

    @Override
    public void moveDown() {
        setPosition(getPosition().x, getPosition().y - moveSpeed);
    }

    public void inputUpdate() {

        if (!controlsEnabled) {
            return;
        } else {
            // Player controls keycode, to get from input/GameKeys (vera)
            // Handles player entity input via in put controls, keys can be changed if set
            // up (vera)
            myE.KEY_PRESSED = 0;

            if (Gdx.input.isKeyPressed(LEFT) || Gdx.input.isKeyPressed(RIGHT)) {
                if (Gdx.input.isKeyPressed(LEFT)) {
                    moveLeft();
                    myE.KEY_PRESSED = LEFT;
                }

                if (Gdx.input.isKeyPressed(RIGHT)) {
                    moveRight();
                    myE.KEY_PRESSED = RIGHT;
                }
            } else if (Gdx.input.isKeyPressed(UP) || Gdx.input.isKeyPressed(DOWN)) {
                if (Gdx.input.isKeyPressed(UP)) {
                    moveUp();
                    myE.KEY_PRESSED = UP;
                }

                if (Gdx.input.isKeyPressed(DOWN)) {
                    moveDown();
                    myE.KEY_PRESSED = DOWN;
                }
            }

            QuitThread();
        }
    }

    public void setControlsEnabled(boolean enabled) {
        this.controlsEnabled = enabled;
    }

    public boolean isControlsEnabled() {
        return controlsEnabled;
    }

}
