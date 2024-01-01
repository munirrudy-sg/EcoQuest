package com.mygdx.game.GameSection.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;
import com.mygdx.game.GameEngineSection.ObjectPoolings.RainPool;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.Entity.Collectibles.Collectible;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.Utils.Helper;

import static com.mygdx.game.Utils.Constants.*;

// Entity that takes in a T of (Player, Platform, Rain)
public class Entity<T> {

    private EntityBuilder<T> builder;

    private Body myBody; // The body that is used to control this entity
    private Fixture fixture; // The fixture, but prob like useless
    private FixtureDef fDef; // This needs to be kept in var cause we can use it to reactivate the collider
    // after destroying it

    private Texture myTexture;
    private boolean hasAnimation, isNPC;
    TextureRegion currentFrame, previousFrame;
    Animation<TextureRegion> upAnimation, downAnimation, rightAnimation, leftAnimation;
    float elapsedTime;
    public int KEY_PRESSED;
    private int prevDirection = 0;

    private Sprite mySprite;
    private float width, height;
    private float xPos, yPos;
    private boolean isCollidable;
    private boolean collided;
    private T obj;
    public T objectPooler;

    public Entity(EntityBuilder<T> builder) {

        this.builder = builder;
        this.myTexture = builder.getTexture();
        this.mySprite = builder.getSprite();
        this.myBody = builder.getBody();
        this.fixture = builder.getFixture();
        this.fDef = builder.getFixtureDef();
        this.width = builder.getWidth();
        this.height = builder.getHeight();
        this.xPos = builder.getPosition().x;
        this.yPos = builder.getPosition().y;
        this.isCollidable = builder.getIsCollidable();
        this.myBody.setUserData(this);

        this.hasAnimation = builder.hasAnimation();
        this.isNPC = builder.isNPC();
        this.upAnimation = builder.getAnimation("UP");
        this.downAnimation = builder.getAnimation("DOWN");
        this.leftAnimation = builder.getAnimation("LEFT");
        this.rightAnimation = builder.getAnimation("RIGHT");
        if (this.hasAnimation)
            previousFrame = this.downAnimation.getKeyFrame(0);
    }

    // Used to draw the sprite of current entity
    public void renderEntity(OrthographicCamera camera, float delta) {

        boolean isPaused = (delta == 0.0f) ? true : false;

        // If player collided and it is not active, dont draw it out
        if (GameScreen.batch == null || myTexture == null || collided)
            return;

        // Run Entity's update method if not paused
        if (!isPaused) {
            // Run each entity's update method
            if (objectPooler != null)
                if (getObject() instanceof Rain)
                    ((Rain) getObject()).update((RainPool) objectPooler);
        }

        // Regardless of pause state, draw entity on screen
        GameScreen.batch.begin();

        // float textureWidthScale = myTexture.getWidth() / 4;
        // float textureHeightScale = myTexture.getHeight() / 4;

        // If put width and height here, it will be drawn to fit the width, but will
        // look stretched out
        float curX = getPosition().x * PPM - (width / 2);
        float curY = getPosition().y * PPM - (height / 2);
        getSprite().setPosition(curX, curY);

        if (hasAnimation) {
            elapsedTime += delta;
            if (isPaused)
                elapsedTime = 0;
            if (isNPC) {
                currentFrame = downAnimation.getKeyFrame(elapsedTime, true);
            } else {
                currentFrame = getCurrentFrame();
            }
            previousFrame = currentFrame;

            GameScreen.batch.draw(currentFrame, curX, curY, width, height);
        } else
            GameScreen.batch.draw(getSprite(), curX, curY, width, height);

        GameScreen.batch.end();
    }

    public void hit(Entity<?> other) {

        // If rain hits anything, rain will definitely be removed
        if (this.obj instanceof Rain)
            ((Rain) this.obj).removeRain();

        else if (other.obj instanceof Rain)
            ((Rain) other.obj).removeRain();

        // If this entity object is other instances
        if (this.obj instanceof Player) {
            ((Player) this.obj).hit(other);

        } else if (this.obj instanceof Platform) {
            ((Platform) this.obj).hit(other);

        } else if (this.obj instanceof Wall) {
            ((Wall) this.obj).hit(other);

        } else if (this.obj instanceof Collectible) {
            ((Collectible) this.obj).hit(other);

        } else {
            System.out.println("[Ln 80, Entity.java | hit()]: " + this.obj.getClass().getSimpleName() + ", hitting "
                    + other.obj.getClass().getSimpleName()
                    + ", but hit behaviour not created.");
        }
        // System.out.println("i am a " + this.obj + ", hitting " + other.obj);

    }

    // Add to gameworld list of fixturestodestroy to remove after world.step,
    // otherwise will crash
    public void destroyFixture() {
        if (GameWorld.getInstance().myEntities.contains(this)) {
            setHasCollided(true);
            setCollidable(false);

            // Put into destroy queue
            GameWorld.getInstance().fixturesToDestroy.add(this);

            // Remove my entity
            GameWorld.getInstance().RemoveEntity(this);

            // If any object collided with Rain, rain will disappear
            if (getObject() instanceof Rain) {
                ((Rain) getObject()).removeRain();
            }
        }
    }

    // Add to gameworld list of fixturestocreate to create after world.step,
    // otherwise will crash
    public void createFixture() {
        setHasCollided(false);
        setCollidable(true);

        GameWorld.getInstance().fixturesToCreate.add(this);
        GameWorld.getInstance().myEntities.add(this);
    }

    // This will be set in Player or Platform itself to get their type
    public void setType(T typeOfObject) {
        obj = typeOfObject;
    }

    // SetPosition will move the object itself, different from setting its' velocity
    public void setPosition(float x, float y) {
        this.myBody.setTransform(new Vector2(x, y), 0);
    }

    // Used to get Entity's position in game unit
    public Vector3 getPositionInGameUnits() {
        // Get player in game pos
        Vector3 playerGamePos = new Vector3(this.myBody.getPosition().x * PPM,
                this.myBody.getPosition().y * PPM, 1.0f);

        return playerGamePos;
    }

    // Change texture on runtime, after creating this entity
    public void setTexture(String path) {

        if (path != "") {
            if (Gdx.files.internal(path).exists()) {
                this.myTexture = new Texture(path);
                this.mySprite = new Sprite(this.myTexture);
                return;
            }
        }
        System.err.println("Path Not Found: (" + path + ")");
    }

    // for animation with only one spritesheet
    public void setTexture(String string, float frame) {
        // Load the spritesheet
        if (!Helper.CheckPathExists(string)) {
            System.out.println("(EntityBuilder.java, Ln 101): File path not found");
        }

        Texture texture = new Texture(string);

        // Split the spritesheet into TextureRegion
        TextureRegion[][] frames = TextureRegion.split(texture, 32, 32);

        // Create the animation
        downAnimation = new Animation<TextureRegion>(frame, frames[0]);

        this.myTexture = texture;
        this.mySprite = new Sprite(this.myTexture);
    }

    // If this is true, then player will know who to collide against
    // Otherwise, player can only collide, but wont know who it is colliding with
    public void setCollidable(boolean isCollidable) {
        this.isCollidable = isCollidable;
    }

    public void setHasCollided(boolean hasCollided) {
        this.collided = hasCollided;
    }

    private TextureRegion getCurrentFrame() {
        // Determine which animation to play based on the movement direction

        if (KEY_PRESSED == KEY_UP) {
            prevDirection = 1;
            return upAnimation.getKeyFrame(elapsedTime, true);
        } else if (KEY_PRESSED == KEY_DOWN) {
            prevDirection = 2;
            return downAnimation.getKeyFrame(elapsedTime, true);
        } else if (KEY_PRESSED == KEY_RIGHT) {
            prevDirection = 3;
            return rightAnimation.getKeyFrame(elapsedTime, true);
        } else if (KEY_PRESSED == KEY_LEFT) {
            prevDirection = 4;
            return leftAnimation.getKeyFrame(elapsedTime, true);
        } else {

            // If no movement keys are pressed, return the default frame
            switch (prevDirection) {
                case 1:
                    return upAnimation.getKeyFrame(0);
                case 2:
                    return downAnimation.getKeyFrame(0);
                case 3:
                    return rightAnimation.getKeyFrame(0);
                case 4:
                    return leftAnimation.getKeyFrame(0);
                default:
                    return previousFrame;
            }
        }
    }

    // Returns the object (Player or Platform. so far)
    public T getObject() {

        return obj;
    }

    public Sprite getSprite() {
        return this.mySprite;
    }

    public Body getBody() {
        return this.myBody;
    }

    public Fixture getFixture() {
        return this.fixture;
    }

    public FixtureDef getFixtureDef() {
        return this.fDef;
    }

    public Vector2 getPosition() {
        return this.myBody.getPosition();
    }

    public Vector2 getSize() {
        return new Vector2(width, height);
    }

    public boolean getIsCollidable() {
        return this.isCollidable;
    }

    // If called this, means should go one level down and call
    // entity.getObject().hit()
    // public void hit() {

    // // System.err.println("Entity is not collidable. Kindly use getObject().hit()
    // // instead");

    // }

    public void dispose() {

        myTexture.dispose();

    }
}
