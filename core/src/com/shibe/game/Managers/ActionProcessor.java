package com.shibe.game.Managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Jere on 26.8.2016.
 */
public class ActionProcessor implements InputProcessor
{
    public static boolean LEFT_PRESSED;
    public static boolean RIGHT_PRESSED;
    public static boolean JUMP;
    public static boolean SHOOT;
    public static boolean DOWN;
    public static int WeaponNmb = 1;

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A)
            LEFT_PRESSED = true;

        if(keycode == Input.Keys.D)
            RIGHT_PRESSED = true;

        if(keycode == Input.Keys.W)
            JUMP = true;

        if(keycode == Input.Keys.S)
            DOWN = true;

        if(keycode == Input.Keys.NUM_1) {
            WeaponNmb = 1;
        }
        if(keycode == Input.Keys.NUM_2) {
            WeaponNmb = 2;
        }
        if(keycode == Input.Keys.NUM_3) {
            WeaponNmb = 3;
        }
        if(keycode == Input.Keys.ESCAPE)
            Game.Menu = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A)
            LEFT_PRESSED = false;

        if(keycode == Input.Keys.D)
            RIGHT_PRESSED = false;

        if(keycode == Input.Keys.W)
            JUMP = false;

        if(keycode == Input.Keys.S)
            DOWN = false;

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        SHOOT = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        SHOOT = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
