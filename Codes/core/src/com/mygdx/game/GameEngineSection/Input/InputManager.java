package com.mygdx.game.GameEngineSection.Input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class InputManager implements InputProcessor {

    public Array<KeyState> keyStates = new Array<KeyState>();
    public Array<TouchState> touchStates = new Array<TouchState>();

    public InputManager() {
        // create the initial state of every key on the keyboard
        // There are 256 keys available which are all represented as integers.
        for (int i = 0; i < 256; i++) {
            keyStates.add(new KeyState(i));
        }
        // atleast on TouchState object due to Desktop users who utilize a mouse rather
        // than touch
        touchStates.add(new TouchState(0, 0, 0, 0));
    }

    public class InputState {
        public boolean pressed = false;
        public boolean down = false;
        public boolean released = false;
    }

    public class KeyState extends InputState {
        // keyboard key of this object represented as a integer
        public int key;

        public KeyState(int key) {
            this.key = key;
        }
    }

    public class TouchState extends InputState {
        // keep track of which finger this object belongs to
        public int pointer;
        // cordinates of this finger/mouse
        public Vector2 coordinates;
        // mouse button
        public int button;
        // track the displacement of this finger/mouse
        private Vector2 lastPosition;
        public Vector2 displacement;

        public TouchState(int coord_x, int coord_y, int pointer, int button) {
            this.pointer = pointer;
            this.coordinates = new Vector2(coord_x, coord_y);
            this.button = button;

            this.lastPosition = new Vector2(0, 0);
            this.displacement = new Vector2(lastPosition.x, lastPosition.y);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        // this function only gets called once when an event is fired.
        // even this key is being held down

        // to store the state of the key being held down as well as pressed
        keyStates.get(keycode).pressed = true;
        keyStates.get(keycode).down = true;

        // every overrriden method needs a return value.This wont be untilized but it
        // can be used for error handlingf
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // the key was released
        // need to set it down state to false and released state to true
        keyStates.get(keycode).down = false;
        keyStates.get(keycode).released = true;

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // There is always at least one touch event initialiazed(mouse)

        // Input manager will add touch events on the fly if more than one
        // finger is touching the screen

        // check for existing pointer (touch)
        boolean pointerFound = false;

        // get altered coordinates
        int coord_x = coordinateX(screenX);
        int coord_y = coordinateY(screenY);

        // set the state of all the touch state events
        for (int i = 0; i < touchStates.size; i++) {
            TouchState t = touchStates.get(i);
            if (t.pointer == pointer) {
                t.down = true;
                t.pressed = true;

                // store coordinates of this touch event
                t.coordinates.x = coord_x;
                t.coordinates.y = coord_y;
                t.button = button;

                // recording last position for displacement values
                t.lastPosition.x = coord_x;
                t.lastPosition.y = coord_y;

                // this pointer exists, dont add new one
                pointerFound = true;
            }
        }
        if (!pointerFound) {
            touchStates.add(new TouchState(coord_x, coord_y, pointer, button));
            TouchState t = touchStates.get(pointer);

            t.down = true;
            t.pressed = true;

            t.lastPosition.x = coord_x;
            t.lastPosition.y = coord_y;

        }

        return false;
    }

    private int coordinateX(int screenX) {
        return screenX - Gdx.graphics.getWidth() / 2;
    }

    private int coordinateY(int screenY) {
        return Gdx.graphics.getHeight() / 2 - screenY;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        TouchState t = touchStates.get(pointer);
        t.down = false;
        t.released = true;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // get altered coordinates
        int coord_x = coordinateX(screenX);
        int coord_y = coordinateY(screenY);

        TouchState t = touchStates.get(pointer);
        // set coordinates of this touchstate

        t.coordinates.x = coord_x;
        t.coordinates.y = coord_y;
        // calculate the displacement of this touchstate based on
        // information from the last frame's position
        t.displacement.x = coord_x - t.lastPosition.x;
        t.displacement.y = coord_y - t.lastPosition.y;
        // store the current position for the next frame
        t.lastPosition.x = coord_x;
        t.lastPosition.y = coord_y;

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    // check states of supplied key
    public boolean isKeyPressed(int key) {
        return keyStates.get(key).pressed;
    }

    public boolean isKeyDown(int key) {
        return keyStates.get(key).down;
    }

    public boolean isKeyReleased(int key) {
        return keyStates.get(key).released;
    }

    public void update() {
        // for every keyState, set pressed and released to false
        for (int i = 0; i < 256; i++) {
            KeyState k = keyStates.get(i);
            k.pressed = false;
            k.released = false;
        }

        for (int i = 0; i < touchStates.size; i++) {
            TouchState t = touchStates.get(i);

            t.pressed = false;
            t.released = false;

            t.displacement.x = 0;
            t.displacement.y = 0;
        }
    }

    // check states of supplied touch

    public boolean isTouchPressed(int pointer) {
        return touchStates.get(pointer).pressed;
    }

    public boolean isTouchDown(int pointer) {
        return touchStates.get(pointer).down;
    }

    public boolean isTouchReleased(int pointer) {
        return touchStates.get(pointer).released;
    }

    public Vector2 touchCoordinates(int pointer) {
        return touchStates.get(pointer).coordinates;
    }

    public Vector2 touchDisplacement(int pointer) {
        return touchStates.get(pointer).displacement;
    }
}
