package com.mygdx.game.Utils;

import java.util.List;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import static com.mygdx.game.Utils.Constants.*;

public class Helper {
    /**
     * Returns a random int
     * 
     * @return inclusive of min, exclusive of max
     */
    public static int getRandomInt(int min, int max) {
        Random random = new Random();
        int randInt = min + random.nextInt(max - min);
        return randInt;
    }

    public static Vector2 getRandomVector2(float minX, float maxX, float minY, float maxY) {
        Random random = new Random();
        float x = minX + random.nextFloat() * (maxX - minX);
        float y = minY + random.nextFloat() * (maxY - minY);
        return new Vector2(x, y);
    }

    public static boolean isDuplicate(List<Vector2> inArray, Vector2 newVector2) {
        for (Vector2 inArr : inArray) {
            if (inArr.x == newVector2.x && inArr.y == newVector2.y)
                return true;
        }
        return false;
    }

    public static boolean isInRange(Vector2 inArr, Vector2 newVector2, float offsetX, float offsetY) {
        float minX = inArr.x - offsetX;
        float minY = inArr.y - offsetY;
        float maxX = inArr.x + offsetX;
        float maxY = inArr.y + offsetY;

        if (newVector2.x >= minX && newVector2.y >= minY && newVector2.x <= maxX && newVector2.y <= maxY)
            return true;
        return false;
    }

    /**
     * Returns path if true, else return empty string ""
     */
    public static String CheckPathExist(String path) {
        String temp = path;
        if (!EXTENSIONS.contains(temp.substring(temp.length() - 4))) {
            for (String e : EXTENSIONS) {
                if (Helper.CheckPathExists(temp + e)) {
                    temp = temp + e;
                    return temp;
                }
            }
        } else {
            if (Helper.CheckPathExists(temp)) {
                return temp;
            }
        }
        // Return empty string if even after adding extensions cannot find file
        return "";
    }

    /**
     * This method checks if a filepath exists
     * 
     * @param path Takes in a filepath
     * @return true or false boolean
     */
    public static boolean CheckPathExists(String path) {

        if (path != "") {

            if (Gdx.files.internal(path).exists()) {
                return true;
            }
        }
        System.err.println("(" + path + ") Texture not found.");
        return false;
    }

    /**
     * Checks for sprite overlapping
     * 
     * @param sprite1
     * @param sprite2
     * @return Returns true if sprites overlap, false otherwise
     */
    public static boolean CheckSpriteOverlap(Sprite sprite1, Sprite sprite2) {
        // Assuming you have two Sprite objects named sprite1 and sprite2
        Rectangle sprite1Bounds = sprite1.getBoundingRectangle();
        Rectangle sprite2Bounds = sprite2.getBoundingRectangle();
        return sprite1Bounds.overlaps(sprite2Bounds);
    }
}
