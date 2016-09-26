package com.shibe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 9.9.2016.
 */
public class AndroidStage
{
    Texture leftButton = new Texture("TouchButtonLeft.png");
    Texture rightButton = new Texture("TouchButtonRight.png");
    Texture downButton = new Texture("TouchButtonDown.png");
    Texture upButton = new Texture("TouchButtonUp.png");
    private float resolutionScaleX = Game.ScreenCoordinates.x / 1920;
    private float resolutionScaleY = Game.ScreenCoordinates.y / 1080;
    public static Image leftImage;
    public static Image rightImage;
    public static Image downImage;
    public static Image upImage;


    public Stage CreateStage()
    {

        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("default-font").getData().setScale(resolutionScaleX, resolutionScaleY);
        skin.getFont("default-font").getData().setScale(skin.getFont("default-font").getData().scaleX * (float) 0.7, skin.getFont("default-font").getData().scaleY * (float) 0.7);

        leftImage = new Image(leftButton);
        leftImage.setX(0 * resolutionScaleX);
        leftImage.setY(200 * resolutionScaleY);
        leftImage.setWidth(250 * resolutionScaleX);
        leftImage.setHeight(250 * resolutionScaleY);

        rightImage = new Image(rightButton);
        rightImage.setX(400 * resolutionScaleX);
        rightImage.setY(200 * resolutionScaleY);
        rightImage.setWidth(250  * resolutionScaleX);
        rightImage.setHeight(250 * resolutionScaleY);

        downImage = new Image(downButton);
        downImage.setX(225 * resolutionScaleX);
        downImage.setY(0 * resolutionScaleY);
        downImage.setWidth(200 * resolutionScaleX);
        downImage.setHeight(200 * resolutionScaleY);

        upImage = new Image(upButton);
        upImage.setX(1600 * resolutionScaleX);
        upImage.setY(0 * resolutionScaleY);
        upImage.setWidth(300 * resolutionScaleX);
        upImage.setHeight(300 * resolutionScaleY);

        TextButton ballButton = new TextButton("Ball", skin);
        ballButton.addAction(Actions.alpha(50));
        ballButton.setPosition(0  * resolutionScaleX, 950 * resolutionScaleY);
        ballButton.setWidth(150  * resolutionScaleX);
        ballButton.setHeight(130 * resolutionScaleY);

        TextButton borkButton = new TextButton("Bork", skin);
        borkButton.addAction(Actions.alpha(50));
        borkButton.setPosition(250  * resolutionScaleX, 950 * resolutionScaleY);
        borkButton.setWidth(150  * resolutionScaleX);
        borkButton.setHeight(130 * resolutionScaleY);

        TextButton missileButton = new TextButton("Missile", skin);
        missileButton.addAction(Actions.alpha(50));
        missileButton.setPosition(500  * resolutionScaleX, 950 * resolutionScaleY);
        missileButton.setWidth(150  * resolutionScaleX);
        missileButton.setHeight(130 * resolutionScaleY);

        TextButton ultraBallButton = new TextButton("Ultraball", skin);
        ultraBallButton.addAction(Actions.alpha(50));
        ultraBallButton.setPosition(750  * resolutionScaleX, 950 * resolutionScaleY);
        ultraBallButton.setWidth(150  * resolutionScaleX);
        ultraBallButton.setHeight(130 * resolutionScaleY);

        Stage stage = new Stage(new ScreenViewport());

        stage.addActor(leftImage);
        stage.addActor(rightImage);
        stage.addActor(downImage);
        stage.addActor(upImage);
        stage.addActor(ballButton);
        stage.addActor(borkButton);
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

        borkButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 2;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        missileButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 3;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        ultraBallButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                ActionProcessor.WeaponNmb = 4;
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        return stage;
    }
}
