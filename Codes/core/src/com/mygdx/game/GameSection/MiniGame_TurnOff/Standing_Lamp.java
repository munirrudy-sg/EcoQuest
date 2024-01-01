package com.mygdx.game.GameSection.MiniGame_TurnOff;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.game.GameSection.Entity.Player;
import com.mygdx.game.GameSection.TriggerEvent;

public class Standing_Lamp extends TriggerEvent {
    private LampCounter lampCounter;
    // Switch On and Off
    protected boolean lampSwitch = true;

    Sprite offSprite;
    Sprite onSprite;

    public Standing_Lamp(Player player, String texture, int width, int height, float xPos, float yPos,
            LampCounter lampCounter) {
        super(player, texture, width, height, xPos, yPos);
        onSprite = new Sprite(triggerSprite);
        offSprite = new Sprite(new Texture("sprites/Lamp/Standing_Lamp_Off.png"));
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

        if (lampSwitch) {
            SetTexture(onSprite.getTexture());
        } else {
            SetTexture(offSprite.getTexture());
        }
    }

    @Override
    public void OnTriggerExit() {

    }
}
