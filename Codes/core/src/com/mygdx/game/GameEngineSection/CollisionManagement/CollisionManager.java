package com.mygdx.game.GameEngineSection.CollisionManagement;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.GameSection.GameWorld;
import com.mygdx.game.GameSection.Entity.Entity;

// This class handles the collision contact between two objects
public class CollisionManager implements ContactListener {

    // A list of collidable entities
    public List<Entity<?>> collidableEntities = new ArrayList<Entity<?>>();

    // Updates collidable entities
    public void UpdateCollidableEntities() {
        for (Entity<?> myEntity : GameWorld.getInstance().myEntities) {
            if (myEntity.getIsCollidable())
                collidableEntities.add(myEntity);
        }
    }

    // Fixture by default provides collider, but this gives me what i want is player
    // able to collide against other objects, but only able to be affected by those
    // in collidableEntities
    private boolean FixtureIsCollidable(Fixture fix) {

        // Loop through my collidable entities and check if they are collidable
        if (fix.getBody().getUserData() instanceof Entity) {

            Entity<?> eFix = (Entity<?>) fix.getBody().getUserData();

            if (eFix.getIsCollidable())
                return true;
        }
        return false;
    }

    // This method will be called when 2 Entity comes in contact with each other
    @Override
    public void beginContact(Contact contact) {

        // Fixture is the collider
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (FixtureIsCollidable(fixA) && FixtureIsCollidable(fixB)) {

            Entity<?> entityA = (Entity<?>) fixA.getBody().getUserData();
            Entity<?> entityB = (Entity<?>) fixB.getBody().getUserData();

            // entityA so far, is Player because player is the one initiating the collision
            entityA.hit(entityB);
        }
    }

    // This method will be called when 2 Entity exits contact from each other
    @Override
    public void endContact(Contact contact) {
        // TODO Auto-generated method stub
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        // TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

        // TODO Auto-generated method stub

    }
}
