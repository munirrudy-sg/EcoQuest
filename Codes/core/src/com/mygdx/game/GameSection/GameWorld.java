package com.mygdx.game.GameSection;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.GameEngineSection.CollisionManagement.CollisionManager;
import com.mygdx.game.GameEngineSection.ObjectPoolings.RainPool;
import com.mygdx.game.GameSection.Conversations.LastConversation;
import com.mygdx.game.GameSection.Entity.*;
import com.mygdx.game.GameSection.Entity.Collectibles.CollectibleManager;
import com.mygdx.game.GameSection.Entity.Collectibles.ICollectible;
import com.mygdx.game.GameSection.Conversations.*;
import com.mygdx.game.GameSection.MiniGame_Rubbish.RubbishBin;
import com.mygdx.game.GameSection.MiniGame_Rubbish.TriggerBinGame;
import com.mygdx.game.GameSection.MiniGame_TurnOff.*;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.Utils.Helper;
import static com.mygdx.game.Utils.Constants.*;

// This will handle the world of game with entities
public class GameWorld {

    private World world;
    private Stage stage;
    private Batch batch;
    private Game game;
    private OrthographicCamera camera;
    private static GameWorld instance;
    private CollisionManager collisionManager; // Hooks the contact listener for this world by setContactListener

    // Entities that will be created
    private Player player;
    private Farmer_NPC farmer;
    private RainPool rainPool;
    private CollectibleManager collectibleManager;

    // Mini game 2
    private Standing_Lamp standingLamp;

    private Table_Lamp tableLamp;

    private int lampStatus = 0;
    public static LampCounter LampCounter;

    // Trigger Area
    private TriggerBinGame triggerBin;
    private List<RubbishBin> eventRubbishGame = new ArrayList<RubbishBin>();

    private List<Standing_Lamp> eventStandingLamp = new ArrayList<Standing_Lamp>();

    private FirstConversation FirstConversation;
    public static TriggerBlockPlayer blockPlayer;
    private LastConversation LastConversation;
    private enterMansion enterMansion;
    private Teleporter backtoMainMap;
    private Teleporter Bedroom2LivingRoom;
    private Teleporter LivingRoom2Toilet;
    private Teleporter LivingRoom2Kitchen;
    private Teleporter LivingRoom2Bedroom;
    private Teleporter Toilet2LivingRoom;
    private Teleporter Kitchen2LivingRoom;

    // Textures
    private Texture textureMetalBin, texturePlasticBin, texturePaperBin, textureGlassBin;
    private Texture texturePlatform;
    private Texture textureRain;
    private Texture healthyCrops;

    private boolean isGame2Completed = false;
    private boolean hasCheckedQuest = false;
    private boolean showLastLabel = true;

    public List<Entity<?>> fixturesToDestroy = new ArrayList<Entity<?>>(); // This list will store fixtures that should
    // be destroyed
    public List<Entity<?>> fixturesToCreate = new ArrayList<Entity<?>>(); // This list will store fixtures that should
    // be created
    public List<Entity<?>> collidableEntities = new ArrayList<Entity<?>>(); // Entities that are collidable
    public List<Entity<?>> myEntities = new ArrayList<Entity<?>>(); // All entities created, regardless of collidable or

    public GameWorld(float gravityForce, boolean doSleep, SpriteBatch batch, Stage stage, Game game) {

        world = new World(new Vector2(0, gravityForce), false);
        this.stage = stage;
        this.game = game;
        if (instance == null)
            instance = this;

        collisionManager = new CollisionManager();
        world.setContactListener(collisionManager);

        // Assign textures
        textureRain = GameAssets.getInstance().textures.get(TEXTURE_RAIN);
        texturePlatform = GameAssets.getInstance().textures.get(TEXTURE_PLATFORM);
        textureGlassBin = GameAssets.getInstance().textures.get(TEXTURE_GLASSBIN);
        textureMetalBin = GameAssets.getInstance().textures.get(TEXTURE_METALBIN);
        texturePaperBin = GameAssets.getInstance().textures.get(TEXTURE_PAPERBIN);
        texturePlasticBin = GameAssets.getInstance().textures.get(TEXTURE_PLASTICBIN);
        this.batch = batch;

        // Create entities
        CreateEntities(batch);
    }

    public static GameWorld getInstance() {
        return instance;
    }

    // Create entities in this world
    public void CreateEntities(SpriteBatch batch) {

        CreateWall(batch);
        CreatePlayer(batch);
        CreateFarmer(batch);
        CreateCollectible(); // Creates the trash for Game1

        // First convo that player will have in game
        FirstConversation = new FirstConversation(player, EMPTY_IMAGE, 55, 55, 362, 432);
        blockPlayer = new TriggerBlockPlayer(player, EMPTY_IMAGE, 20, 100, 400, 275);

        // Last convo that player will have in game
        LastConversation = new LastConversation(game, player, EMPTY_IMAGE, 55, 55, 362, 432);

        // Game 1: Collecting Rubbish Game
        triggerBin = new TriggerBinGame(player, EMPTY_IMAGE, 32, 96, 395, -187);

        eventRubbishGame
                .add(new RubbishBin(BIN_TYPE.PLASTIC, player, texturePlasticBin, 32, 32, 359, 144));
        eventRubbishGame.add(new RubbishBin(BIN_TYPE.METAL, player, textureMetalBin, 32, 32, 295, 144));
        eventRubbishGame.add(new RubbishBin(BIN_TYPE.PAPER, player, texturePaperBin, 32, 32, 231, 144));
        eventRubbishGame.add(new RubbishBin(BIN_TYPE.GLASS, player, textureGlassBin, 32, 32, 167, 144));

        // Pass collectible manager into trgger bin to keep track number of trash left
        triggerBin.setCollectibleManager(collectibleManager);

        LampCounter = new LampCounter(stage);

        // Game 2: Turning Off Electrics
        enterMansion = new enterMansion(player, EMPTY_IMAGE, 32, 32, 584, 1645, 1860, 2456);
        backtoMainMap = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1860, 2456, 584, 1644);
        Bedroom2LivingRoom = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1381, 2370, 1757, 2376);
        LivingRoom2Bedroom = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1730, 2376, 1381, 2370);
        LivingRoom2Kitchen = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1998, 2377, 2306, 2404);
        LivingRoom2Toilet = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1861, 2240, 1930, 1815);
        Toilet2LivingRoom = new Teleporter(player, EMPTY_IMAGE, 32, 32, 1930, 1815, 1861, 2271);
        Kitchen2LivingRoom = new Teleporter(player, EMPTY_IMAGE, 32, 32, 2306, 2404, 1966, 2377);

        eventStandingLamp
                .add(new Standing_Lamp(player, "sprites/Lamp/Standing_Lamp_On.png", 16, 29, 1303, 2462, LampCounter));
        eventStandingLamp
                .add(new Standing_Lamp(player, "sprites/Lamp/Standing_Lamp_On.png", 16, 29, 1821, 2392, LampCounter));
        eventStandingLamp
                .add(new Standing_Lamp(player, "sprites/Lamp/Standing_Lamp_On.png", 16, 29, 2583, 2392, LampCounter));
        tableLamp = new Table_Lamp(player, "sprites/Lamp/Table_Lamp_On.png", 16, 16, 1231, 2421, LampCounter);

        healthyCrops = new Texture("map/healthycrops.png");

        collisionManager.UpdateCollidableEntities();
    }

    // Update entities in this world
    public void UpdateEntities(OrthographicCamera camera, float delta) {
        this.camera = camera;

        // True when delta is 0.0f
        boolean isPaused = (delta == 0.0f) ? true : false;

        triggerBin.Render();
        for (RubbishBin rubbish : eventRubbishGame) {
            rubbish.Render();
            triggerBin.checkGameComplete(rubbish.GetBinType(), rubbish.GetNumOfTrashInBin());
        }

        if (triggerBin.IsGame1Completed()) {
            // Update quest panel if game 1 is completed
            GameScreen.questPanel.completeQuest(2);
        }

        tableLamp.Render();
        // check for if all lights are switched off
        lampStatus = 0;
        for (Standing_Lamp sLamp : eventStandingLamp) {
            sLamp.Render();
            if (sLamp.checkOffLamps == 1) {
                lampStatus = lampStatus + sLamp.checkOffLamps;
            }
        }

        if (lampStatus == 3 && tableLamp.checkOffLamps == 1) {
            // mini game switch off light complete
            isGame2Completed = true;
            GameScreen.questPanel.completeQuest(3);
        }

        // Show instruction label once both games are done
        if (triggerBin.IsGame1Completed() && isGame2Completed) {
            GameScreen.questPanel.setQuestVisible(4, true);
            if (showLastLabel) {
                GameScreen.PauseGame();
                FirstConversation.instructionLabel.setVisible(true);
                if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
                    FirstConversation.instructionLabel.setVisible(false);
                    GameScreen.ResumeGame();
                    hasCheckedQuest = true;
                    showLastLabel = false;
                }
            }
        }

        // Once user has checked quest panel to see the last quest, render
        // lastConversation
        if (hasCheckedQuest || isAllQuestsCompleted()) {
            batch.begin();
            batch.draw(healthyCrops, 329 - 134, 492 - 109, 231 + 15 + 10 + 5, 172 + 10 + 5 + 3);
            batch.end();
            LastConversation.Render();
            farmer.getEntity().setTexture("sprites/Farmer/farmer_happy.png", 0.2f);
        } else {
            // Place outside so it will update. Updating needed cause it removes blockWall
            FirstConversation.update(delta);
            if (!player.getPlayerData("firstconversation")) {
                FirstConversation.Render();
                if (blockPlayer != null) {
                    blockPlayer.Render();
                }
            }
        }

        enterMansion.Render();
        backtoMainMap.Render();
        Bedroom2LivingRoom.Render();
        LivingRoom2Toilet.Render();
        LivingRoom2Kitchen.Render();
        LivingRoom2Bedroom.Render();
        Toilet2LivingRoom.Render();
        Kitchen2LivingRoom.Render();

        for (int i = 0; i < myEntities.size(); i++) {

            myEntities.get(i).renderEntity(camera, delta);
        }

        if (!isPaused) {

            // Handles player entity input
            player.inputUpdate();

            // Focuses on player when game is not paused
            cameraUpdate(camera, player.getPosition(), 0.1f, "GameWorld");
        }

        // Updates player inventory
        InventoryManager.getInstance().Update();

        collisionManager.UpdateCollidableEntities();
    }

    private boolean isAllQuestsCompleted() {
        if (getPlayer().getPlayerData("firstconversation")
                && getPlayer().getPlayerData("game1")
                && getPlayer().getPlayerData("game2")) {
            return true;
        }

        return false;
    }

    private void CreateCollectible() {
        if (collectibleManager == null)
            collectibleManager = new CollectibleManager();

        if (!GameWorld.getInstance().getPlayer().getPlayerData("game1")) {
            // Initializes collectibles in game
            collectibleManager.InitCollectibles();
        }
        // Add all collectibles to entities for rendering etc
        for (ICollectible collectible : collectibleManager.getMyCollectibles()) {
            myEntities.add(collectible.getEntity());
        }
    }

    private void CreatePlayer(SpriteBatch batch) {
        player = new Player(batch, 48, 48, -112, 275);

        // Set new movement keys
        player.SetMovementKeys(Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D);
        myEntities.add(player.getEntity());
    }

    private void CreateFarmer(SpriteBatch batch) {
        farmer = new Farmer_NPC(batch, 48, 48, 370, 464);
        myEntities.add(farmer.getEntity());
    }

    private void CreateWall(SpriteBatch batch) {
        // Initialize the array of numbers
        int[][] wall_coords = {
                { 827, 441, 87, 1444 }, // top left grass area
                { 672, 215, 162, 972 }, // 3 houses
                { 659, 1151, 1019, 836 }, // middle right side grass
                { 668, 217, 1015, 1556 }, // top right grass area
                { 368, 412, 877, -93 }, // bottom right grass
                { 1, 144, -164, 278 }, // behind player start pos
                { 445, 1, 58, 350 }, // start pos to fence door
                { 173, 1, 443, 350 }, // right of fence door to end of grass
                { 1, 359, 530, 529 }, // right side of fences
                { 384, 1, 338, 603 }, // top of fence
                { 1, 253, 146, 476 }, // left side of fences
                { 697, 1, 184, 206 }, // bottom of player start pos to end of grass
                { 1, 301, 533, 55 }, // right side of park fences
                { 1, 48, 533, -278 }, // right side of park fences after park entrance
                { 1, 148, 1061, 187 }, // 2 houses right side grass
                { 1, 515, -326, 966 }, // 3 houses left side grass
                { 856, 1, 102, 709 }, // 3 houses bottom side grass
                { 180, 1, 591, 1665 }, // tapwater game house
                { 160, 1, 613, -299 }, // outside of park bottom trees
                { 110, 4, 478, -256 }, // bottom side entrance of park
                { 566, 1, 140, -259 }, // bottom side fence park
                { 188, 74, -237, -222 }, // 3 trees at bottom left park
                { 1, 255, -360, -58 }, // left side of park fences
                { 188, 51, -237, 95 }, // top right park lake
                { 259, 31, -14, 147 }, // 3 benches
                { 313, 1, 272, 163 }, // top park fences
                { 77, 61, 467, 132 }, // top right park tree
                { 1, 197, 506, 3 }, // right side park fence
                { 100, 243, 180, -42 }, // middle lake right side
                { 112, 243, -15, -42 }, // middle lake left side
                { 94, 14, 486, -102 }, // top side entrance of park
                { 250, 377, 2138, 2426 }, // right side living room & left side kitchen
                { 345, 1, 2435, 2366 }, // bottom kitchen
                { 1, 249, 2614, 2490 }, // right side kitchen
                { 346, 1, 2442, 2615 }, // top side kitchen
                { 173, 1, 1824, 2474 }, // top side living room to wardrobe
                { 61, 1, 1964, 2460 }, // wardrobe
                { 109, 19, 1862, 2369 }, // 2 small sofa living room
                { 44, 19, 1869, 2407 }, // mid sofa living room
                { 26, 57, 1744, 2274 }, // left plant living room
                { 32, 59, 1971, 2273 }, // right plant living room
                { 58, 95, 2317, 2503 }, // counter left kitchen
                { 25, 25, 2371, 2458 }, // counter bottom left
                { 12, 1, 2365, 2519 }, // counter top left
                { 12, 1, 2429, 2519 }, // counter in btwn stove and sink
                { 102, 26, 2538, 2532 }, // right side counter
                { 44, 38, 2509, 2443 }, // kitchen table
                { 19, 20, 2515, 2414 }, // btm chair kitchen
                { 19, 19, 2515, 2460 }, // top chair kitchen
                { 19, 26, 2547, 2443 }, // right chair kitchen
                { 13, 19, 2473, 2439 }, // left chair kitchen
                { 1, 212, 1962, 1806 }, // toilet right side
                { 135, 1, 1894, 1700 }, // toilet btm side
                { 1, 141, 1827, 1770 }, // toilet left side
                { 63, 1, 1923, 1834 }, // toilet top side
                // {38, 64, 1853, 1745}, // bathtub toilet
                { 32, 13, 1850, 1785 }, // toilet in toilet
                { 19, 27, 1939, 1715 }, // towels toilet
                { 320, 313, 1559, 2394 }, // right side bed room & left side living room
                { 32, 1, 1357, 2474 }, // top bedroom right
                { 19, 43, 1363, 2445 }, // sofa bedroom
                { 51, 25, 1257, 2468 }, // top bedroom left
                { 51, 51, 1257, 2372 }, // bed bedroom
                { 1, 217, 1206, 2442 }, // left side bedroom
                { 180, 1, 1296, 2334 }, // bottom side bedroom
                { 236, 1, 1864, 2245 }, // btm side living room
        };

        for (int i = 0; i < wall_coords.length; i++) {
            int[] row = wall_coords[i];
            Wall start = new Wall(batch, row[0], row[1], row[2], row[3]);

            // start.getEntity().setTexture("sprites/ClassicRPG_Sheet_Sliced/img/img47.png");
            myEntities.add(start.getEntity());
        }
    }

    private void CreateRain(SpriteBatch batch) {
        rainPool = new RainPool();

        int rainToSpawn = 50;

        for (int i = 0; i < rainToSpawn; i++) {

            Vector2 rainPos = Helper.getRandomVector2(-50, 150, 100, 200);

            Rain temp = rainPool.checkOut(batch, textureRain, 8, 8, (int) rainPos.x, (int) rainPos.y);

            myEntities.add(temp.getEntity());

        }
    }

    public void RemoveEntity(Entity<?> e) {
        myEntities.remove(e);
    }

    // Makes it so that camera follows player / move camera to desired location (as
    // in MiniGame1)
    public void cameraUpdate(OrthographicCamera camera, Vector2 posToFollow, float lerpSpeed, String x) {

        Vector3 position = camera.position;

        position.x = position.x + (posToFollow.x * PPM - position.x) * lerpSpeed;
        position.y = position.y + (posToFollow.y * PPM - position.y) * lerpSpeed;

        camera.position.set(position);
        camera.update();
    }

    public Stage getStage() {
        return this.stage;
    }

    public Game getGame() {
        return this.game;
    }

    public World getWorld() {
        return this.world;
    }

    public Batch getBatch() {
        return this.batch;
    }

    public Player getPlayer() {
        return this.player;
    }

    public OrthographicCamera getCamera() {
        return this.camera;
    }

    public void UpdateGameWorld() {

        this.world.step(1 / 60f, 6, 2);

        // I used for loop instead of foreach loop because
        // for loop can remove its' own element while looping, foreach dont allow
        // removing
        for (int i = 0; i < fixturesToDestroy.size(); i++) {
            fixturesToDestroy.get(i).getBody().setActive(false);
            fixturesToDestroy.remove(fixturesToDestroy.get(i));
        }

        for (int i = 0; i < fixturesToCreate.size(); i++) {
            fixturesToCreate.get(i).getBody().setActive(true);
            fixturesToCreate.remove(fixturesToCreate.get(i));
        }
    }

    private void disposeEntities() {
        for (Entity<?> entity : myEntities)
            entity.dispose();
    }

    public void dispose() {
        this.world.dispose();
        disposeEntities();
    }

}