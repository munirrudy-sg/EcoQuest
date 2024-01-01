package com.mygdx.game.GameSection.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;

import static com.mygdx.game.Utils.Constants.*;

public class Farmer_NPC {

    private Entity<Object> myE;

    private int mass = 1000000;

    private float width, height, xPos, yPos;

    public Farmer_NPC(SpriteBatch batch, int width, int height, int xPos, int yPos) {
        String textureFolder = "sprites/Farmer/";

        myE = new EntityBuilder<>(width, height, xPos, yPos)
                // .addTexture(textureFolder + "farmer_sad.png", 0.2f)
                .addTexture(textureFolder + "farmer_sad.png", 0.2f)
                .addCollision()
                .addBody(POLYGON, mass, false)
                .build();

        // myE.getBody().setUserData(this);
        myE.setType(this);

        this.width = myE.getSize().x;

        this.height = myE.getSize().y;

        this.xPos = myE.getPosition().x;

        this.yPos = myE.getPosition().y;

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
}
