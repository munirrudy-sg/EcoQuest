package com.mygdx.game.GameSection.Entity;

import java.util.ArrayList;
import java.util.List;

import com.mygdx.game.Utils.Helper;
import static com.mygdx.game.Utils.Constants.*;

// This class handles the structure of an Item that Player can store in Inventory
// Serializable
public class Item implements Cloneable {
    /*
     * Item have
     * 1. name
     * 2. texture path
     * 3. quantity
     */
    private String itemName = "";
    private String itemTexturePath = "";
    private int itemQty = 0;
    private Object itemType;

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // #region Getter / Setter methods
    public void SetItemName(String itemName) {
        this.itemName = itemName;
    }

    public void SetItemTexture(String texturePath) {
        texturePath = Helper.CheckPathExist(texturePath);
        if (texturePath != "") {
            itemTexturePath = texturePath;
            return;
        }
        System.err.println(itemTexturePath + " do not exist");
    }

    public void SetItemType(Object type) {
        this.itemType = type;
    }

    public Object GetItemType() {
        return this.itemType;
    }

    public void SetItemQty(int itemQty) {
        this.itemQty = itemQty;
    }

    public void AddItemQty(int itemQty) {
        this.itemQty += itemQty;
    }

    public String GetItemName() {
        return this.itemName;
    }

    public String GetItemTexture() {
        return this.itemTexturePath;
    }

    public int GetItemQty() {
        return this.itemQty;
    }
    // #endregion
}
