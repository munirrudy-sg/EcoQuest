package com.mygdx.game.GameSection.Entity.Collectibles;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameSection.Entity.Item;
import com.mygdx.game.Utils.Helper;
import com.mygdx.game.Utils.Constants.BIN_TYPE;

/*
 * This class will handle the creation, update, deletion of all collectibles
 */
public class CollectibleManager {
    // To keep track of spawn pos, min and max is to reference array index
    private static final int MIN = 0;
    private static final int MAX = 1;

    private int numOfCollectibles = 0;
    private List<ICollectible> myCollectibles = new ArrayList<ICollectible>();

    // Initializes all collectibles for user
    public void InitCollectibles() {

        int width = 16;
        int height = 16;
        String fileName = "collectibles/collectibles.txt";
        if (Gdx.files.internal(fileName).exists()) {
            FileHandle file = Gdx.files.internal(fileName);
            String text = file.readString();

            // Handle Multiple OS file read
            String delim = "";
            if (System.getProperty("os.name").contains("Windows")) { // Windows
                delim = "\r\n";
            } else if (System.getProperty("os.name").contains("Mac OS X")) { // Mac OS X is Unix
                delim = "\n";
            } else { // This is old version of Mac
                delim = "\r";
            }

            String[] textures = text.split(delim);

            // Setting the spawn area for rubbish
            Vector2[][] spawnPos = new Vector2[4][2];
            spawnPos[0][MIN] = new Vector2(-343, -163);
            spawnPos[0][MAX] = new Vector2(-103, 55);

            spawnPos[1][MIN] = new Vector2(-71, -234);
            spawnPos[1][MAX] = new Vector2(307, -188);

            spawnPos[2][MIN] = new Vector2(239, -92);
            spawnPos[2][MAX] = new Vector2(387, 35);

            spawnPos[3][MIN] = new Vector2(58, -78);
            spawnPos[3][MAX] = new Vector2(82, 57);

            // Each spawn pos will have 4 items
            int spawnIndex = 0;

            // Sets the total amount of collectibles
            numOfCollectibles = textures.length;

            for (int i = 0; i < textures.length; i++) {
                // Change spawn pos every 4 items spawned
                if (i % 4 == 0 && i != 0)
                    spawnIndex++;

                // Get random position
                Vector2 myRandomPos = Helper.getRandomVector2(
                        spawnPos[spawnIndex][MIN].x, spawnPos[spawnIndex][MAX].x,
                        spawnPos[spawnIndex][MIN].y, spawnPos[spawnIndex][MAX].y);

                // Create collectible and assign their respective bin type
                String binType = textures[i].substring(0, textures[i].indexOf('_'));
                BIN_TYPE curBinType = null;

                // Set bin type
                if (binType.equalsIgnoreCase(BIN_TYPE.PLASTIC.toString())) {
                    curBinType = BIN_TYPE.PLASTIC;
                } else if (binType.equalsIgnoreCase(BIN_TYPE.METAL.toString())) {
                    curBinType = BIN_TYPE.METAL;
                } else if (binType.equalsIgnoreCase(BIN_TYPE.PAPER.toString())) {
                    curBinType = BIN_TYPE.PAPER;
                } else if (binType.equalsIgnoreCase(BIN_TYPE.GLASS.toString())) {
                    curBinType = BIN_TYPE.GLASS;
                }

                // Create collectible w random position
                if (curBinType != null) {
                    Collectible temp = null;

                    if (myCollectibles.isEmpty()) {
                        // Creates the first collectible normally
                        temp = new Collectible(curBinType, "collectibles/" + textures[i],
                                width, height,
                                myRandomPos.x,
                                myRandomPos.y);

                    } else {
                        try {
                            // Clone Item with base as first collectible's Item
                            Item tempItem = (Item) myCollectibles.get(0).getItem().clone();
                            String extension = ".png";

                            // Override value of new item
                            tempItem.SetItemName(textures[i].substring(0, textures[i].length() - extension.length()));
                            tempItem.SetItemTexture("collectibles/" + textures[i]);
                            tempItem.SetItemType(curBinType);

                            // Creates a new collectible but pass in Item object instead of type of object
                            temp = new Collectible(tempItem, width, height, myRandomPos.x, myRandomPos.y);

                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                    myCollectibles.add(temp);
                    temp.setCollectibleManager(this);
                } else {
                    System.err.println("CollectibleManager.java (InitCollectibles()) | binType not found.");
                }
            }
        }
    }

    // Returns original amount of collectibles (before collecting)
    public int getTotalNumOfCollectibles() {
        return this.numOfCollectibles;
    }

    // Returns current amount of collectibles (will decrease after collecting)
    public List<ICollectible> getMyCollectibles() {
        return this.myCollectibles;
    }

    public void RemoveCollectible(Collectible collectible) {
        myCollectibles.remove(collectible);
        // CheckCollectibleList();
    }

    public void CheckCollectibleList() {
        // Checks what collectible is available in map
        for (ICollectible collectible : myCollectibles) {
            String delim = myCollectibles.indexOf(collectible) == myCollectibles.size() - 1 ? "" : ", ";
            System.out.print(collectible.getItem().GetItemName() + delim);
        }
        System.out.println("");
    }
}