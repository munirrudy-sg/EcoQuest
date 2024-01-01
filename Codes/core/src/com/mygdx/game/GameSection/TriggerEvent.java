package com.mygdx.game.GameSection;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.Utils.Helper;

// to be fair, we dont need Platform to be an entity. like the grass player walk on, can just be a sprite. need change bah - aloy
public abstract class TriggerEvent implements TriggerBehaviour {

    protected Sprite triggerSprite;

    Player player;
    protected boolean playerInRange;
    protected boolean canExit = false;
    protected int checkOffLamps = 0;

    // Creates a sprite from string texture path
    public TriggerEvent(Player player, String texture, int width, int height, float xPos, float yPos) {
        triggerSprite = new Sprite(new Texture(texture));
        triggerSprite.setBounds(xPos - width / 2, yPos - height / 2, width, height);
        this.player = player;
    }

    // Creates a sprite from Texture that is already loaded
    public TriggerEvent(Player player, Texture texture, int width, int height, float xPos, float yPos) {
        triggerSprite = new Sprite(texture);
        triggerSprite.setBounds(xPos - width / 2, yPos - height / 2, width, height);
        this.player = player;
    }

    public void Render() {

        GameScreen.batch.begin();

        GameScreen.batch.draw(triggerSprite, triggerSprite.getX(),
                triggerSprite.getY(), 32, 32);
        GameScreen.batch.end();

        SetPlayerInRange();

        if (playerInRange) {
            OnTriggerEnter();
            canExit = true;
        } else {
            // So that trigger exit code will run once
            if (canExit) {
                OnTriggerExit();
                canExit = false;
            }

        }
    }

    public void SetTexture(Texture newTexture) {
        triggerSprite.setTexture(newTexture);
    }

    public void SetPlayerInRange() {
        playerInRange = Helper.CheckSpriteOverlap(triggerSprite, player.getEntity().getSprite());
    }

}
