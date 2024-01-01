package com.mygdx.game.GameSection.MiniGame_TurnOff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.Screen.GameScreen;
import com.mygdx.game.GameSection.TriggerEvent;

public class Table_Lamp extends TriggerEvent {

    protected boolean lampSwitch = true;
    private LampCounter lampCounter;
    Sprite offSprite;

    public Table_Lamp(Player player, String texture, int width, int height, float xPos, float yPos,
            LampCounter lampCounter) {
        super(player, texture, width, height, xPos, yPos);

        offSprite = new Sprite(new Texture("sprites/Lamp/Table_Lamp_Off.png"));
        offSprite.setBounds(xPos - width / 2, yPos - height / 2, width, height);
        this.lampCounter = lampCounter;
    }

    @Override
    public void OnTriggerEnter() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.E)) {
            lampSwitch = !lampSwitch;
            lampCounter.updateCounter(lampSwitch);
            if (lampSwitch == false)
                checkOffLamps++;
            else
                checkOffLamps--;
        }

    }

    @Override
    public void OnTriggerExit() {

    }

    @Override
    public void Render() {
        GameScreen.batch.begin();

        if (lampSwitch) {
            GameScreen.batch.draw(triggerSprite, triggerSprite.getX(),
                    triggerSprite.getY(), 32, 32);
        } else {
            GameScreen.batch.draw(offSprite, offSprite.getX(),
                    offSprite.getY(), 32, 32);
        }
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
}
