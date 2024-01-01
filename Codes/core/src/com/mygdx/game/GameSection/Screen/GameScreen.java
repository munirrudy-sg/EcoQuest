package com.mygdx.game.GameSection.Screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.mygdx.game.GameEngineSection.Input.InputManager;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.QuestPanel;

import static com.mygdx.game.Utils.Constants.*;

public class GameScreen extends AbstractScreen {

    private Stage stage;
    private Game game;
    public static SpriteBatch batch;
    public static OrthographicCamera camera;
    private Texture img;

    private GameWorld world;
    private Box2DDebugRenderer b2dr;

    // Variables regarding pause
    private static Boolean paused = false;
    private final Dialog pauseDialog;
    private boolean isPauseDialogShown;
    private boolean isQuestsWindowVisible;

    // Quest panel
    public static QuestPanel questPanel;

    InputManager inputManager;
    private InputMultiplexer multiplexer;

    public GameScreen(Game aGame) {

        game = aGame;
        // System.out.println()
        stage = new Stage();

        inputManager = new InputManager();
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);
        multiplexer.addProcessor(inputManager);

        // Best is to have only 1 spritebatch in game
        batch = new SpriteBatch();

        // This img will be the background
        img = new Texture("map/MainMap.png");

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / SCALE, h / SCALE);

        // The gravity that will act on the gameworld
        float gravityForce = 0.0f;

        // The world object that entity will spawn in
        world = new GameWorld(gravityForce, false, batch, stage, game);

        // To draw the squarethingy like a hitbox but just for debugging
        b2dr = new Box2DDebugRenderer();

        pauseDialog = new Dialog("", GameEngine.gameSkin) {

            {
                text("Paused");
                button("Back to Main Menu", 1L);
                getButtonTable().row();
                button("Resume", 2L);
            }

            protected void result(Object object) {
                if (object.equals(1L)) {
                    paused = false;
                    // ScreenManager.getInstance().show(ScreenEnum.MAIN_MENU, game);
                    ScreenManager.getInstance().show(ScreenEnum.LOADING_SCREEN, ScreenEnum.MAIN_MENU, game);
                }
                if (object.equals(2L)) {
                    paused = false;
                }
            }
        };

        // Quests window. Toggling Q will open it.
        questPanel = new QuestPanel(stage);
        questPanel.setVisible(false);

        // Hide all quests except the first one at the beginning

        questPanel.setQuestVisible(2, false);
        questPanel.setQuestVisible(3, false);
        questPanel.setQuestVisible(4, false);

        if (GameWorld.getInstance().getPlayer().getPlayerData("firstconversation")) {
            questPanel.completeQuest(1);
            questPanel.setQuestVisible(2, true);
            questPanel.setQuestVisible(3, true);

        }
        if (GameWorld.getInstance().getPlayer().getPlayerData("game1")) {
            questPanel.completeQuest(2);

            if (GameWorld.getInstance().getPlayer().getPlayerData("game2")) {
                questPanel.setQuestVisible(4, true);
            }
        }
        if (GameWorld.getInstance().getPlayer().getPlayerData("game2")) {
            questPanel.completeQuest(3);

            if (GameWorld.getInstance().getPlayer().getPlayerData("game1")) {
                questPanel.setQuestVisible(4, true);
            }
        }
    }

    // Function to create a label.

    public void show() {
        Gdx.input.setInputProcessor(multiplexer);
    }

    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        renderBackground();

        // If user press Esc, game will toggle between pause and resume
        if (inputManager.isKeyPressed(Input.Keys.ESCAPE)) {

            paused = !paused;

            if (paused) {
                pauseDialog.show(stage);
                isPauseDialogShown = true;
            }
        }
        // Place outside isKeyJustPressed otherwise user will see pause dialog briefly
        // before continuing game
        if (!paused && isPauseDialogShown) {
            pauseDialog.act(100);
            pauseDialog.remove();
            isPauseDialogShown = false;
        }

        // Pressing Q will open quests window.
        if (inputManager.isKeyPressed(Input.Keys.Q)) {
            isQuestsWindowVisible = !isQuestsWindowVisible;
            questPanel.setVisible(isQuestsWindowVisible);
        }

        // deltaTime represents the time flow in game world
        float deltaTime = 0.0f;
        if (!paused)
            deltaTime = Gdx.graphics.getDeltaTime();

        GameWorld.getInstance().UpdateEntities(camera, deltaTime);// Update entities
        GameWorld.getInstance().UpdateGameWorld();// Update game world

        stage.act();
        stage.draw();

        // Set the scale of camera and sprite stuffs
        batch.setProjectionMatrix(camera.combined);
        // b2dr.render(GameWorld.getInstance().getWorld(), camera.combined.scl(PPM));

        inputManager.update();

    }

    // Not used right now, left it here cause we could use it in Part 2
    private void renderBackground() {
        // Draw background image
        batch.begin();
        batch.draw(img, 0 - img.getWidth() / 2, 0 - img.getHeight() / 2,
                img.getWidth() * 2, img.getHeight() * 2);
        batch.end();
    }

    public void resize(int width, int height) {
        camera.setToOrtho(false, width / SCALE, height / SCALE);
    }

    public static void PauseGame() {
        paused = true;
    }

    public static void ResumeGame() {
        paused = false;
    }

    public void pause() {
        // Stop player autosave thread.
        // This part will occur when player cross the game without exiting properly
        world.getPlayer().StopAutoSaveThread();
    }

    public void resume() {

    }

    public void hide() {

    }

    public void dispose() {
        // Dispose of entities in world
        GameWorld.getInstance().dispose();

        stage.dispose();
    }
}
