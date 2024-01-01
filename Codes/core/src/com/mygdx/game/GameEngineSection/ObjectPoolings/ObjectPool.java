package com.mygdx.game.GameEngineSection.ObjectPoolings;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GameSection.GameWorld;

// This abstract class will be the parent for any pool classes created. (E.g. RainPool.java)
public abstract class ObjectPool<T> {
    private final Set<T> available = new HashSet<>();
    private final Set<T> inUse = new HashSet<>();

    GameWorld world;

    // This will be override in child class
    protected abstract T create(SpriteBatch batch, Texture texture, int width, int height, int xPos, int yPos);

    public int getAvailableSize() {
        return available.size();
    }

    // Return instance of pooled object
    // Used when i already created it before
    public synchronized T checkOut() {

        if (!available.isEmpty() || !inUse.isEmpty()) {

            // If available is used up, reuse whatever that is in available
            if (available.isEmpty()) {
                T prev = inUse.iterator().next();
                checkIn(prev);
                available.add(prev);
            }

            T instance = available.iterator().next();
            available.remove(instance);
            inUse.add(instance);
            return instance;

        }
        return null;
    }

    // Create and return instance for pooled object
    // Used for creating, when available is empty
    public synchronized T checkOut(SpriteBatch batch, Texture texture, int width, int height, int xPos, int yPos) {
        if (available.isEmpty())
            available.add(create(batch, texture, width, height, xPos, yPos));

        T instance = available.iterator().next();
        available.remove(instance);
        inUse.add(instance);

        return instance;
    }

    // Remove pooled object from scene. Add it back to available
    public synchronized void checkIn(T instance) {
        inUse.remove(instance);
        available.add(instance);
    }

}
