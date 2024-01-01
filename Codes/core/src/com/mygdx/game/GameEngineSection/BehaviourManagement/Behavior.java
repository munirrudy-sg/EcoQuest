package com.mygdx.game.GameEngineSection.BehaviourManagement;

import com.mygdx.game.GameSection.Entity.Entity;

// This interface will define functionality to whichever class (Entity) that implements it. Abstraction.
public interface Behavior {

    void moveLeft();

    void moveRight();

    void moveUp();

    void moveDown();

    void hit(Entity<?> other);
}
