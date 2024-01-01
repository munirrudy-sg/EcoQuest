package com.mygdx.game.GameSection.Screen;

import java.util.Random;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.GameEngineSection.Screen.AbstractScreen;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;
import com.mygdx.game.GameSection.GameAssets;
import static com.mygdx.game.Utils.Constants.*;

public class LoadingScreen extends AbstractScreen {

    private ShapeRenderer shapeRenderer;
    private SpriteBatch batch;
    private Game game;
    private Stage stage;

    // Screen to load
    private ScreenEnum screenToLoad;

    // Loading progress
    float progress = 0.0f;

    // Background
    private Texture bgImg;

    // Font
    private BitmapFont font;

    // Facts
    private String fileName = "facts.txt";
    private String factsToShow = "";
    private String[] facts;
    private int numberOfFacts;

    public LoadingScreen(Game aGame, ScreenEnum screenEnum) {
        screenToLoad = screenEnum;
        game = aGame;
        stage = new Stage();
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        String texturesToLoad[] = {
                TEXTURE_PLATFORM, TEXTURE_RAIN,
                TEXTURE_GLASSBIN, TEXTURE_METALBIN,
                TEXTURE_PAPERBIN, TEXTURE_PLASTICBIN };

        // Load texture according to which screen to load
        GameAssets.getInstance().LoadTextures(screenToLoad, texturesToLoad);

        // Create background image for loading screen
        bgImg = new Texture("background_title.jpg");

        // Create fonts and get a random fact
        createFonts();
        facts = readFileInList(fileName);
        if (numberOfFacts > 0)
            factsToShow = facts[getRandomFact()];
    }

    // Get random fact
    private int getRandomFact() {
        Random rand = new Random();
        return rand.nextInt(numberOfFacts);
    }

    // Read facts from file
    private String[] readFileInList(String fileName) {

        String[] lines = {};

        // Set cutoff line at index 75 (max should be 90 ish)
        int cutoff = 75;

        if (Gdx.files.internal(fileName).exists()) {
            FileHandle file = Gdx.files.internal(fileName);
            String text = file.readString();

            // System.out.println(System.getProperty("os.name"));

            // Handle Multiple OS file read
            String delim = "";
            if (System.getProperty("os.name").contains("Windows")) { // Windows
                delim = "\r\n";
            } else if (System.getProperty("os.name").contains("Mac OS X")) { // Mac OS X is Unix
                delim = "\n";
            } else { // This is old version of Mac
                delim = "\r";
            }

            String[] t = text.split(delim);

            // Add a return carriage if too long (so that text wont go out of scene)
            for (int i = 0; i < t.length; i++) {
                int tempCutoff = cutoff;

                // If length of current line will overflow to out of screen
                if (t[i].length() > cutoff) {
                    // Make sure not to cut off words halfway
                    // Thus, cutoff at the index where it is a space
                    while (t[i].toCharArray()[tempCutoff] != ' ') {

                        // Add the cutoff point until it is a space ' '
                        tempCutoff++;

                        // If length of current line is less than tempCutoff int, break
                        if (t[i].length() <= tempCutoff)
                            break;

                    }
                    // If length of current line is more than tempCutoff
                    if (t[i].length() > tempCutoff)
                        // Add a carriage return at the tempCutoff index
                        t[i] = t[i].substring(0, tempCutoff) + delim + t[i].substring(tempCutoff);
                }
            }
            lines = t;
        }

        numberOfFacts = lines.length - 1;
        return lines;
    }

    // Create font
    private void createFonts() {
        String fontPath = "Matiz.ttf";
        if (Gdx.files.internal(fontPath).exists()) {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontPath));
            FreeTypeFontParameter parameter = new FreeTypeFontParameter();
            parameter.size = 12;
            font = generator.generateFont(parameter); // font size 12 pixels
            font.getData().setScale(2.0f);
            // font.setColor(Color.BLACK);

            generator.dispose(); // don't forget to dispose to avoid memory leaks!
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw background
        batch.begin();
        batch.draw(bgImg, 0, 0);
        font.draw(batch, factsToShow, 20,
                Gdx.graphics.getHeight() / 3);
        batch.end();

        // Update the asset loading progress
        progress += 0.01f;

        // display the progress to the user
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Holder
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), 20);

        // Progress bar
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.rect(0, 0, progress * Gdx.graphics.getWidth(), 20);

        shapeRenderer.end();

        // If loading completed, go to game screen
        if (progress >= 1.0f) {
            ScreenManager.getInstance().show(screenToLoad, game);
        }

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
    }

    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        stage.dispose();

    }

    // other necessary methods
}