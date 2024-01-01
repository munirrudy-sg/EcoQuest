package com.mygdx.game.GameSection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.GameEngineSection.GameEngine;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class QuestPanel {

    private Stage stage;
    private Table questsTable;
    private CheckBox quest1CheckBox;
    private CheckBox quest2CheckBox;
    private CheckBox quest3CheckBox;
    private CheckBox quest4CheckBox;
    private Label quest1Label;
    private Label quest2Label;
    private Label quest3Label;
    private Label quest4Label;

    public QuestPanel(Stage stage) {
        this.stage = stage;
        questsTable = createQuestsTable();
        stage.addActor(questsTable);

    }

    public void setVisible(boolean visible) {
        questsTable.setVisible(visible);
    }

    private Table createQuestsTable() {
        questsTable = new Table();
        questsTable.setFillParent(true);

        // Creates a Scene2D window with title and skin.
        Window questsWindow = new Window("Quests", GameEngine.gameSkin);

        // Makes sure the elements below title are positioned from top left.
        questsWindow.top().left();
        questsWindow.getTitleTable().padTop(48).padLeft(8);
        questsWindow.getTitleLabel().setFontScale(0.8f);

        // Prevents the window from being dragged.
        questsWindow.setMovable(false);

        // Example of 2 quests.
        String delim = "";
        if (System.getProperty("os.name").contains("Windows")) { // Windows
            delim = "\r\n";
        } else if (System.getProperty("os.name").contains("Mac OS X")) { // Mac OS X is Unix
            delim = "\n";
        } else { // This is old version of Mac
            delim = "\r";
        }

        Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));

        // Quest 1
        quest1CheckBox = new CheckBox("", skin);
        quest1CheckBox.setTouchable(Touchable.disabled);
        questsWindow.add(quest1CheckBox).align(Align.left).padLeft(8).padTop(36);
        questsWindow
                .add(quest1Label = createLabelForQuest(
                        "Talk to the crying farmer and" + delim + "find out why he is upset."))
                .align(Align.left).padLeft(8).padTop(36);
        questsWindow.row();

        // Quest 2
        quest2CheckBox = new CheckBox("", skin);
        quest2CheckBox.setTouchable(Touchable.disabled);
        questsWindow.add(quest2CheckBox).align(Align.left).padLeft(8).padTop(8);
        questsWindow
                .add(quest2Label = createLabelForQuest("Help keep the park clean! Sort" + delim
                        + "the rubbish into the right" + delim + "recycling bins."))
                .align(Align.left).padLeft(8).padTop(8);
        questsWindow.row();

        // Quest 3
        quest3CheckBox = new CheckBox("", skin);
        quest3CheckBox.setTouchable(Touchable.disabled);
        questsWindow.add(quest3CheckBox).align(Align.left).padLeft(8).padTop(8);
        questsWindow
                .add(quest3Label = createLabelForQuest(
                        "Save energy and reduce" + delim + "pollution! Turn off all lamps in" + delim + "the mansion."))
                .align(Align.left).padLeft(8).padTop(8);
        questsWindow.row();

        // Quest 4
        quest4CheckBox = new CheckBox("", skin);
        quest4CheckBox.setTouchable(Touchable.disabled);
        questsWindow.add(quest4CheckBox).align(Align.left).padLeft(8).padTop(8);
        questsWindow
                .add(quest4Label = createLabelForQuest(
                        "Check back with the farmer to " + delim + " see if he needs any more help."))
                .align(Align.left).padLeft(8).padTop(8);
        questsWindow.row();

        // Adds the Window to a table of width 400x300.
        questsTable.add(questsWindow).width(400).height(300);
        this.stage.addActor(questsTable);

        // Sets the window invisible on start.
        questsTable.setVisible(false);
        return questsTable;
    }

    public Label createLabelForQuest(String text) {
        Label label = new Label(text, GameEngine.gameSkin);
        label.setFontScale(0.6f);
        return label;
    }

    public void completeQuest(int questNumber) {
        GameWorld.getInstance().getPlayer().setCompletedQuest(questNumber);
        switch (questNumber) {
            case 1:
                quest1CheckBox.setChecked(true);
                break;
            case 2:
                quest2CheckBox.setChecked(true);
                break;
            case 3:
                quest3CheckBox.setChecked(true);
                break;
            case 4:
                quest4CheckBox.setChecked(true);
                break;
            default:
                throw new IllegalArgumentException("Invalid quest number: " + questNumber);
        }
    }

    public void setQuestVisible(int questNumber, boolean visible) {
        switch (questNumber) {
            case 1:
                quest1CheckBox.setVisible(visible);
                quest1Label.setVisible(visible);
                break;
            case 2:
                quest2CheckBox.setVisible(visible);
                quest2Label.setVisible(visible);
                break;
            case 3:
                quest3CheckBox.setVisible(visible);
                quest3Label.setVisible(visible);
                break;
            case 4:
                quest4CheckBox.setVisible(visible);
                quest4Label.setVisible(visible);
                break;
            default:
                throw new IllegalArgumentException("Invalid quest number: " + questNumber);
        }
    }

}