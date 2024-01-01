package com.mygdx.game.GameEngineSection.ObjectPoolings;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameSection.Entity.Rain;

// This class will handle the creation of the pool of rain
public class RainPool extends ObjectPool<Rain> {

    @Override
    protected Rain create(SpriteBatch batch, Texture texture, int width, int height, int xPos, int yPos) {
        return new Rain(this, batch, texture, width, height, xPos, yPos);
    }
}
