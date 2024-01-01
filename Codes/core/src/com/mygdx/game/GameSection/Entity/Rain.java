package com.mygdx.game.GameSection.Entity;

import java.util.concurrent.atomic.AtomicInteger;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;
import com.mygdx.game.GameEngineSection.ObjectPoolings.RainPool;
import com.mygdx.game.GameSection.GameWorld;

import static com.mygdx.game.Utils.Constants.*;

public class Rain {

    private static final AtomicInteger counter = new AtomicInteger(0);

    public final int id;

    private Entity<Object> myE;

    private int mass = 0;

    private float width, height, xPos, yPos;

    private RainPool rainPool;

    private float rainSpeed = 0.05f;

    public Rain(RainPool rainPool, SpriteBatch batch, Texture texture, int width, int height,
            int xPos, int yPos) {

        myE = new EntityBuilder<>(width, height, xPos, yPos)
                .addTexture(texture)
                .addCollision()
                .addBody(CIRCLE, mass, false)
                .build();

        myE.objectPooler = rainPool;

        myE.setType(this);

        this.width = myE.getSize().x;

        this.height = myE.getSize().y;

        this.xPos = myE.getPosition().x;

        this.yPos = myE.getPosition().y;

        id = counter.incrementAndGet();

    }

    // This line must be called after rainPool.checkOut();, otherwise wont show in
    // game
    public void awake() {

        getEntity().createFixture();
        myE.setPosition(xPos, yPos);
    }

    public void update(RainPool rainPool) {

        myE.setPosition(myE.getPosition().x, myE.getPosition().y - rainSpeed);

        if (myE.getPosition().y <= -5.0f) {

            // Remove if reach certain threshold
            removeRain();

        }

    }

    // Will be called from Entity, destroyFixture()
    public void removeRain() {

        GameWorld.getInstance().myEntities.remove(this.myE);

        ((RainPool) myE.objectPooler).checkIn(this);

    }

    public void hit(Entity<?> other) {

    }

    public Entity<?> getEntity() {

        return myE;

    }

    public Vector2 getPosition() {

        return myE.getPosition();

    }

    protected void setPosition(float x, float y) {

        myE.setPosition(x, y);

    }

    public int getId() {

        return id;

    }

}
