package com.mygdx.game.GameSection.SnakeGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

public class Snake extends Actor {

    private SpriteBatch batch;
    private Texture snakeHead;
    private Texture apple;
    private Texture pear;
    private Array<Vector2> snakeBody;
    private BitmapFont font;
    private Vector2 applePosition;
    private Vector2 pearPosition;
    private int snakeLength = 3;
    private boolean up = true;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private float moveTimer = 0.0f;
    private final float MOVE_TIME = 0.1f;
    private Random random = new Random();
    private int points = 0;
    private boolean isGameOver = false;
    private static final int MARGIN = 10;
    private int highScore = 0;
    private Array<Texture> goodItems;
    private Array<Texture> badItems;
    Texture whiteBackground = new Texture("white_background.png");
    private int Game_Width = 641 + 570;
    private int Game_Height = 360 + Gdx.graphics.getWidth() / 8;

    public Snake() {
        batch = new SpriteBatch();
        snakeHead = new Texture(Gdx.files.internal("Snake_Collectible/snake.png"));

        goodItems = new Array<>();
        goodItems.add(new Texture(Gdx.files.internal("Snake_Collectible/good/BulkResizePhotos.com/bicycle.png")));
        goodItems.add(new Texture(Gdx.files.internal("Snake_Collectible/good/BulkResizePhotos.com/tree.png")));
        goodItems.add(new Texture(Gdx.files.internal("Snake_Collectible/good/BulkResizePhotos.com/windturbine.png")));

        badItems = new Array<>();
        badItems.add(new Texture(Gdx.files.internal("Snake_Collectible/bad/BulkResizePhotos.com/plasticbag.png")));
        badItems.add(new Texture(Gdx.files.internal("Snake_Collectible/bad/BulkResizePhotos.com/waterbottle.png")));
        badItems.add(new Texture(Gdx.files.internal("Snake_Collectible/bad/BulkResizePhotos.com/car.png")));

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("DePixelHalbfett.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        font = generator.generateFont(parameter);
        parameter.size = 16;
        BitmapFont highScoreFont = generator.generateFont(parameter);
        generator.dispose();
        font.setColor(Color.BLACK);

        highScoreFont.setColor(Color.BLACK);
        snakeBody = new Array<Vector2>();
        for (int i = 0; i < snakeLength; i++) {
            snakeBody.add(new Vector2(50 + i * 10, 50));
        }
        applePosition = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        pearPosition = new Vector2(-1, -1);

        // Replace apple and pear textures with random good and bad items
        spawnApple();
        spawnPear();

        if (points > highScore) {
            highScore = points;
        }
    }

    public void draw() {
        if (isGameOver) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
                isGameOver = false;
                points = 0;
                snakeLength = 3;
                snakeBody.clear();
                for (int i = 0; i < snakeLength; i++) {
                    snakeBody.add(new Vector2(50 + i * 10, 50));
                }
                spawnPear();
            }
        } else {
            // Gdx.gl.glClearColor(0,0,0,0);
            // Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            act(Gdx.graphics.getDeltaTime());
        }

        batch.begin();
        batch.draw(whiteBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        font.getData().setScale(2, 2);
        GlyphLayout instructionLayout = new GlyphLayout(font,
                "Consume items that are good for the environment.\nAvoid items that are bad for the environment!",
                Color.BLACK, Gdx.graphics.getWidth(), Align.center, false);
        font.draw(batch, instructionLayout, 0, Gdx.graphics.getHeight() / 2 + instructionLayout.height / 2);
        if (points >= 10) {
            batch.draw(apple, applePosition.x, applePosition.y);
        }
        batch.draw(pear, pearPosition.x, pearPosition.y);
        for (Vector2 bodySegment : snakeBody) {
            batch.draw(snakeHead, bodySegment.x, bodySegment.y);
        }
        font.setColor(Color.BLACK);
        font.getData().setScale(2, 2);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        GlyphLayout pointsLayout = new GlyphLayout(font, "Points: " + points);
        font.draw(batch, pointsLayout, Gdx.graphics.getWidth() - pointsLayout.width - 10,
                Gdx.graphics.getHeight() - pointsLayout.height - 10);
        BitmapFont highScoreFont = font; // Use existing font for high score label
        highScoreFont.getData().setScale(2, 2); // Set scale of high score font
        highScoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        GlyphLayout highScoreLayout = new GlyphLayout(highScoreFont, "High Score: " + highScore); // Use high score font
                                                                                                  // for label
        highScoreFont.draw(batch, highScoreLayout, MARGIN,
                Gdx.graphics.getHeight() - pointsLayout.height - highScoreLayout.height);
        highScoreFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        batch.end();

        if (isGameOver) {
            batch.begin();
            batch.draw(whiteBackground, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();

            batch.begin();
            font.setColor(Color.RED);
            font.getData().setScale(2, 2);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            GlyphLayout gameOverLayout = new GlyphLayout(font, "Play Again?\n(Press Enter)");
            font.draw(batch, gameOverLayout, (Gdx.graphics.getWidth() - gameOverLayout.width) / 2,
                    (Gdx.graphics.getHeight() + gameOverLayout.height) / 2);
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            batch.end();
        }
        super.act(Gdx.graphics.getDeltaTime());

    }

    public void dispose() {
        batch.dispose();
        snakeHead.dispose();
        apple.dispose();
        pear.dispose();
        font.dispose();
        whiteBackground.dispose();

        for (Texture goodItem : goodItems) {
            goodItem.dispose();
        }

        for (Texture badItem : badItems) {
            badItem.dispose();
        }
    }

    public void act(float deltaTime) {
        handleInput();
        moveTimer += deltaTime;
        if (moveTimer >= MOVE_TIME) {
            moveSnake();
            checkCollision();
            moveTimer = 0;
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && !down) {
            up = true;
            left = false;
            right = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && !up) {
            down = true;
            left = false;
            right = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && !right) {
            left = true;
            up = false;
            down = false;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && !left) {
            right = true;
            up = false;
            down = false;
        }
    }

    private void moveSnake() {
        Vector2 headPosition = snakeBody.first().cpy();

        if (up) {
            headPosition.y += 10;
        }
        if (down) {
            headPosition.y -= 10;
        }
        if (left) {
            headPosition.x -= 10;
        }
        if (right) {
            headPosition.x += 10;
        }

        snakeBody.insert(0, headPosition);
        if (snakeBody.size > snakeLength) {
            snakeBody.removeIndex(snakeBody.size - 1);
        }
    }

    private void checkCollision() {
        Vector2 headPosition = snakeBody.first();

        if (headPosition.x < 0 || headPosition.x >= Gdx.graphics.getWidth() || headPosition.y < 0
                || headPosition.y >= Gdx.graphics.getHeight()) {
            isGameOver = true;
        }

        for (int i = 1; i < snakeBody.size; i++) {
            if (headPosition.equals(snakeBody.get(i))) {
                isGameOver = true;
                break;
            }
        }

        // Increase collision area by 20%
        float collisionAreaIncrease = 1.2f;

        if (headPosition.dst(applePosition) < snakeHead.getWidth() * collisionAreaIncrease && points >= 10) {
            points -= 10;
            if (snakeBody.size > 3) {
                snakeBody.removeIndex(snakeBody.size - 1);
            }
            spawnApple();
        }

        if (headPosition.dst(pearPosition) < snakeHead.getWidth() * collisionAreaIncrease) {
            points += 10;
            snakeLength++;
            snakeBody.insert(snakeBody.size - 1, snakeBody.get(snakeBody.size - 1).cpy());
            spawnPear();

            if (points >= 10) {
                spawnApple();
            }

            if (points > highScore) {
                highScore = points;
            }
        }
    }

    private void spawnApple() {
        if (points >= 10) {
            int x = random.nextInt((Gdx.graphics.getWidth() - snakeHead.getWidth() - MARGIN * 2) / 10) * 10 + MARGIN;
            int y = random.nextInt((Gdx.graphics.getHeight() - snakeHead.getHeight() - MARGIN * 2) / 10) * 10 + MARGIN;
            applePosition.set(x, y);
            apple = getRandomBadItem();
        } else {
            applePosition.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    private void spawnPear() {
        int x = random.nextInt((Gdx.graphics.getWidth() - snakeHead.getWidth() - MARGIN * 2) / 10) * 10 + MARGIN;
        int y = random.nextInt((Gdx.graphics.getHeight() - snakeHead.getHeight() - MARGIN * 2) / 10) * 10 + MARGIN;
        pearPosition.set(x, y);
        pear = getRandomGoodItem();
    }

    private Texture getRandomGoodItem() {
        return goodItems.random();
    }

    private Texture getRandomBadItem() {
        return badItems.random();
    }

}