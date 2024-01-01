package com.mygdx.game.GameEngineSection;

import com.badlogic.gdx.Game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.mygdx.game.GameEngineSection.Screen.ScreenEnum;
import com.mygdx.game.GameEngineSection.Screen.ScreenManager;

public class GameEngine extends Game {

	static public Skin gameSkin;

	@Override
	public void create() {

		gameSkin = new Skin(Gdx.files.internal("skin/craftacular-ui.json"));

		ScreenManager.getInstance().initialize(this);

		// Show Main menu
		ScreenManager.getInstance().show(ScreenEnum.LOADING_SCREEN, ScreenEnum.MAIN_MENU, this);
	}

	@Override
	public void render() {

		super.render();

	}

	@Override
	public void resize(int width, int height) {
		// camera.setToOrtho(false, width / SCALE, height / SCALE);
	}

	@Override
	public void dispose() {

	}

}
