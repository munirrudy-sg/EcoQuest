package com.mygdx.game.GameSection.MiniGame_TurnOff;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.mygdx.game.GameEngineSection.GameEngine;

public class LampCounter {

    private static Label lampCounterLabel;
    private int offLampsCount;

    public LampCounter(Stage stage) {
        offLampsCount = 0;
        lampCounterLabel = createLampCounterLabel();
        stage.addActor(lampCounterLabel);
        showLampCounter(false);
    }

    public static void showLampCounter(boolean bool){
        lampCounterLabel.setVisible(bool);
    }

    public Label createLampCounterLabel() {
        Label label = new Label("Lamps switched off: 0", GameEngine.gameSkin);
        label.setFontScale(1.0f);
        label.setPosition(10, 10); // Set the position of the label on the screen
        return label;
    }

    public void updateCounter(boolean lampSwitch) {
        if (lampSwitch) {
            offLampsCount--;
        } else {
            offLampsCount++;
        }
        lampCounterLabel.setText("Lamps switched off: " + offLampsCount + " out of 4");
    }
}
