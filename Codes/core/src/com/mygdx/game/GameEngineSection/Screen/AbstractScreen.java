package com.mygdx.game.GameEngineSection.Screen;

import com.badlogic.gdx.Screen;

public abstract class AbstractScreen implements Screen {

    public abstract void show();

    public abstract void render(float delta);

    public abstract void resize(int width, int height);

    public abstract void pause();

    public abstract void resume();

    public abstract void hide();

    public abstract void dispose();
}
