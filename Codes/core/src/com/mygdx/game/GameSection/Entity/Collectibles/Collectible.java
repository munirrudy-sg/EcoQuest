package com.mygdx.game.GameSection.Entity.Collectibles;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameEngineSection.BehaviourManagement.Behavior;
import com.mygdx.game.GameEngineSection.Entities.EntityBuilder;
import com.mygdx.game.GameSection.InventoryManager;
import com.mygdx.game.GameSection.Entity.Entity;
import com.mygdx.game.GameSection.Entity.Item;

import static com.mygdx.game.Utils.Constants.*;

public class Collectible implements Behavior, ICollectible {

    private Entity<Object> myE;
    private Item item;
    private CollectibleManager myManager;

    private int mass = 50000;
    private float width, height, xPos, yPos;

    private boolean takenByUser;

    // Original method of creating collectible item
    public Collectible(Object itemType, String texture, int width, int height, float xPos, float yPos) {
        myE = new EntityBuilder<>(width, height, xPos, yPos)
                .addTexture(texture)
                .addCollision()
                .addBody(POLYGON, 0, true)
                .build();

        myE.setType(this);

        this.width = myE.getSize().x;
        this.height = myE.getSize().y;
        this.xPos = myE.getPosition().x;
        this.yPos = myE.getPosition().y;

        item = new Item();
        item.SetItemName(texture.substring(0, texture.length() - 4));
        item.SetItemTexture(texture);
        item.SetItemQty(1);
        item.SetItemType(itemType);
    }

    // Overload constructor to pass in Item instead, for cloning purpose in
    // CollectibleManager.java
    public Collectible(Item item, int width, int height, float xPos, float yPos) {
        myE = new EntityBuilder<>(width, height, xPos, yPos)
                .addTexture(item.GetItemTexture())
                .addCollision()
                .addBody(POLYGON, 0, true)
                .build();

        myE.setType(this);

        this.width = myE.getSize().x;
        this.height = myE.getSize().y;
        this.xPos = myE.getPosition().x;
        this.yPos = myE.getPosition().y;
        this.item = item;
    }

    public Vector2 getPosition() {
        return myE.getPosition();
    }

    protected void setPosition(float x, float y) {
        myE.setPosition(x, y);
    }

    public void setCollectibleManager(CollectibleManager collectibleManager) {
        this.myManager = collectibleManager;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public Entity<?> getEntity() {
        return myE;
    }

    @Override
    public void hit(Entity<?> other) {
        // Add to player inventory
        InventoryManager.getInstance().AddItem(item);

        // Remove from collectible manager
        myManager.RemoveCollectible(this);

        // Destroy the collider and remove from entities so it stops showing in scene
        myE.destroyFixture();
        // System.out.println(other.getClass() + " is hitting me");
    }

    @Override
    public void moveLeft() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveLeft'");
    }

    @Override
    public void moveRight() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveRight'");
    }

    @Override
    public void moveUp() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveUp'");
    }

    @Override
    public void moveDown() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'moveDown'");
    }

}
