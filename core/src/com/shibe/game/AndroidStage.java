package com.shibe.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shibe.game.Managers.ActionProcessor;

/**
 * Created by Jere on 9.9.2016.
 */
public class AndroidStage
{
    Texture leftButton = new Texture("TouchButtonLeft.png");
    Texture rightButton = new Texture("TouchButtonRight.png");
    Texture downButton = new Texture("TouchButtonDown.png");
    Texture upButton = new Texture("TouchButtonUp.png");


    public Stage CreateStage()
    {
        Image leftImage = new Image(leftButton);
        leftImage.setX(100);
        leftImage.setY(150);
        leftImage.setWidth(70);
        leftImage.setHeight(70);

        Image rightImage = new Image(rightButton);
        rightImage.setX(300);
        rightImage.setY(150);
        rightImage.setWidth(70);
        rightImage.setHeight(70);

        Image downImage = new Image(downButton);
        downImage.setX(200);
        downImage.setY(50);
        downImage.setWidth(70);
        downImage.setHeight(70);

        Image upImage = new Image(upButton);
        upImage.setX(1700);
        upImage.setY(100);
        upImage.setWidth(100);
        upImage.setHeight(100);

        Stage stage = new Stage(new ScreenViewport());

        stage.addActor(leftImage);
        stage.addActor(rightImage);
        stage.addActor(downImage);
        stage.addActor(upImage);

        leftImage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.LEFT_PRESSED = false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.LEFT_PRESSED = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        rightImage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.RIGHT_PRESSED = false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.RIGHT_PRESSED = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        downImage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.DOWN = false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.DOWN = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        upImage.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.JUMP = false;
                super.touchUp(event, x, y, pointer, button);
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.JUMP = true;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        return stage;
    }
}
