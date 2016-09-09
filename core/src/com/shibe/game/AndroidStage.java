package com.shibe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

        Image leftImage = new Image(leftButton);
        leftImage.setX(100);
        leftImage.setY(200);
        leftImage.setWidth(150);
        leftImage.setHeight(150);

        Image rightImage = new Image(rightButton);
        rightImage.setX(300);
        rightImage.setY(200);
        rightImage.setWidth(150);
        rightImage.setHeight(150);

        Image downImage = new Image(downButton);
        downImage.setX(200);
        downImage.setY(50);
        downImage.setWidth(150);
        downImage.setHeight(150);

        Image upImage = new Image(upButton);
        upImage.setX(1700);
        upImage.setY(50);
        upImage.setWidth(200);
        upImage.setHeight(200);

        TextButton ballButton = new TextButton("Ball", skin);
        ballButton.setPosition(0, 1000);
        ballButton.setWidth(300);
        ballButton.setHeight(50);

        TextButton missileButton = new TextButton("Missile", skin);
        missileButton.setPosition(350, 1000);
        missileButton.setWidth(300);
        missileButton.setHeight(50);

        TextButton ultraBallButton = new TextButton("Ultraball", skin);
        ultraBallButton.setPosition(700, 1000);
        ultraBallButton.setWidth(300);
        ultraBallButton.setHeight(50);

        Stage stage = new Stage(new ScreenViewport());

        stage.addActor(leftImage);
        stage.addActor(rightImage);
        stage.addActor(downImage);
        stage.addActor(upImage);
        stage.addActor(ballButton);
        stage.addActor(missileButton);
        stage.addActor(ultraBallButton);

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

        ballButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 1;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        missileButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 2;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        ultraBallButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 3;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        return stage;
    }
}
