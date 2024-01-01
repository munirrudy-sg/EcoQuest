package com.mygdx.game.GameEngineSection.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.Entity.Entity;
import com.mygdx.game.Utils.Helper;

import static com.mygdx.game.Utils.Constants.*;

public class EntityBuilder<T> {

    private float width, height, xPos, yPos;
    private Texture texture;
    Animation<TextureRegion> upAnimation, downAnimation, rightAnimation, leftAnimation;
    private Sprite mySprite;
    private Body myBody;
    private Fixture fixture;
    private FixtureDef fDef;
    private boolean isCollidable = false; // If this is true, when creating body, it will create the collider
    private boolean hasAnimation = false;
    private boolean isNPC = false;

    public EntityBuilder(float width, float height, float xPos, float yPos) {
        this.width = width;
        this.height = height;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    public EntityBuilder<T> addTexture(String path) {

        if (Helper.CheckPathExists(path)) {
            this.texture = new Texture(path);
            this.mySprite = new Sprite(texture);
            return this;
        }

        System.err.println("(" + path + ") Texture not found.");
        texture = null;
        return this;
    }

    public EntityBuilder<T> addTexture(Texture texture) {

        if (texture != null) {
            this.texture = texture;
            this.mySprite = new Sprite(texture);
            return this;
        }
        System.err.println("(" + texture + ") Texture not found.");
        return this;
    }

    // Used for animation
    public EntityBuilder<T> addTexture(String upString, String downString, String rightString, String leftString) {
        // Load the four spritesheets
        if (!Helper.CheckPathExists(upString) || !Helper.CheckPathExists(downString)
                || !Helper.CheckPathExists(rightString) || !Helper.CheckPathExists(leftString)) {
            System.out.println("(EntityBuilder.java, Ln 70): File paths not found");
            return this;
        }

        Texture upTexture = new Texture(upString);
        Texture downTexture = new Texture(downString);
        Texture rightTexture = new Texture(rightString);
        Texture leftTexture = new Texture(leftString);

        // Split the spritesheets into TextureRegions
        TextureRegion[][] upFrames = TextureRegion.split(upTexture, 32, 32);
        TextureRegion[][] downFrames = TextureRegion.split(downTexture, 32, 32);
        TextureRegion[][] rightFrames = TextureRegion.split(rightTexture, 32, 32);
        TextureRegion[][] leftFrames = TextureRegion.split(leftTexture, 32, 32);

        // Create the four animations
        upAnimation = new Animation<TextureRegion>(0.1f, upFrames[0]);
        downAnimation = new Animation<TextureRegion>(0.1f, downFrames[0]);
        rightAnimation = new Animation<TextureRegion>(0.1f, rightFrames[0]);
        leftAnimation = new Animation<TextureRegion>(0.1f, leftFrames[0]);

        this.texture = downTexture;
        this.mySprite = new Sprite(this.texture);
        hasAnimation = true;
        return this;
    }

    // for animation with only one spritesheet
    public EntityBuilder<T> addTexture(String string, float frame) {
        // Load the spritesheet
        if (!Helper.CheckPathExists(string)) {
            System.out.println("(EntityBuilder.java, Ln 101): File path not found");
            return this;
        }

        Texture texture = new Texture(string);

        // Split the spritesheet into TextureRegion
        TextureRegion[][] frames = TextureRegion.split(texture, 32, 32);

        // Create the animation
        downAnimation = new Animation<TextureRegion>(frame, frames[0]);

        this.texture = texture;
        this.mySprite = new Sprite(this.texture);
        hasAnimation = true;
        isNPC = true;
        return this;
    }

    public EntityBuilder<T> addCollision() {
        isCollidable = true;
        return this;
    }

    // This should be at the last when creating Entity
    public EntityBuilder<T> addBody(String shapeType, int mass, boolean isStatic) {

        Body pbody;

        // Create body definition
        BodyDef def = new BodyDef();

        // Set body type, static : not affected by force
        if (isStatic)
            def.type = BodyType.StaticBody;
        else
            def.type = BodyType.DynamicBody;

        def.position.set(xPos / PPM, yPos / PPM);

        def.fixedRotation = true;

        // Create a body in game world
        pbody = GameWorld.getInstance().getWorld().createBody(def);

        // Define a shape for the body, either polygon OR circle
        Shape shape = null;

        // Depends on what user wants to create, Polygon or Circle
        if (shapeType.equals(POLYGON)) {

            shape = new PolygonShape();
            ((PolygonShape) shape).setAsBox(width / 2 / PPM, height / 2 / PPM);

        } else if (shapeType.equals(CIRCLE)) {

            shape = new CircleShape();
            ((CircleShape) shape).setRadius(width / 2 / PPM);
        }

        // FixtureDef gives it its' shape and mass
        this.fDef = new FixtureDef();
        this.fDef.shape = shape;
        this.fDef.density = mass;
        this.fixture = pbody.createFixture(this.fDef);

        // Set body to not active means it is not collidable
        if (!isCollidable) {
            pbody.setActive(false);
        }

        this.myBody = pbody;

        return this;
    }

    // Build this object
    public Entity<T> build() {

        return new Entity<T>(this);
    }

    public Texture getTexture() {
        return this.texture;
    }

    public boolean isNPC() {
        return this.isNPC;
    }

    public boolean hasAnimation() {
        return this.hasAnimation;
    }

    public Animation<TextureRegion> getAnimation(String direction) {
        switch (direction) {
            case "UP":
                return this.upAnimation;
            case "DOWN":
                return this.downAnimation;
            case "RIGHT":
                return this.rightAnimation;
            case "LEFT":
                return this.leftAnimation;
            default:
                return null;
        }
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

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public Vector2 getPosition() {
        return new Vector2(xPos, yPos);
    }

    public boolean getIsCollidable() {
        return this.isCollidable;
    }

}
