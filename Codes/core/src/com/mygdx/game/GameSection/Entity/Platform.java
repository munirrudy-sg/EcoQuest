package com.mygdx.game.GameSection.Entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;

import static com.mygdx.game.Utils.Constants.*;

public class Platform {

    private Entity<Object> myE;

    private int mass = 50000;

    private float width, height, xPos, yPos;

    public Platform(SpriteBatch batch, Texture texture, int width, int height, int xPos, int yPos) {

        myE = new EntityBuilder<>(width, height, xPos, yPos)
                .addTexture(texture)
                .addBody(POLYGON, 0, true)
                .build();

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
