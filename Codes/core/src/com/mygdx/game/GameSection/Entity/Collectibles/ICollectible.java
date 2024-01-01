package com.mygdx.game.GameSection.Entity.Collectibles;

import com.mygdx.game.GameSection.Entity.Entity;
import com.mygdx.game.GameSection.Entity.Item;

/**
 * ICollectible will be used in Collectible.java to improve code reusability
 */
public interface ICollectible {

    public Item getItem(); // Return the item that is binded to this collectible

    public Entity<?> getEntity(); // Return the entity that is binded to this collectible

    public void setCollectibleManager(CollectibleManager manager); // Set collectible manager to that collectible can
                                                                   // make use of collectibleManager's variable

}
