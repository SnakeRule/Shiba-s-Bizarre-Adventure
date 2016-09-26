package com.shibe.game;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shibe.game.Components.PlayerComponent;
import com.shibe.game.Managers.ActionProcessor;
import com.shibe.game.Managers.Game;

import javax.print.attribute.standard.MediaSize;

/**
 * Created by Jere on 16.9.2016.
 */
public class HudStage extends EntitySystem
{
    private float resolutionScaleX = Game.ScreenCoordinates.x / 1920;
    private float resolutionScaleY = Game.ScreenCoordinates.y / 1080;

    public static Label Name;
    Label Money;
    Label health;
    Label Gmode;
    Label Missiles;
    Label LevelComplete;

    ComponentMapper<PlayerComponent> pm = ComponentMapper.getFor(PlayerComponent.class);
    PlayerComponent player;
    ImmutableArray<Entity> players;
    Entity e;

    public HudStage() {
        super();
    }

    public Stage CreateStage()
    {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
        skin.getFont("default-font").getData().setScale(resolutionScaleX, resolutionScaleY);

        Name = new Label("",skin);
        Name.setPosition(30, 900);

        health = new Label("",skin);
        health.setPosition(30, 800);

        Money = new Label("", skin);
        Money.setPosition(30,700);

        Gmode = new Label("",skin);
        Gmode.setPosition(30, 600);

        Missiles = new Label("",skin);
        Missiles.setPosition(30, 500);

        LevelComplete = new Label("Level Complete!",skin);
        LevelComplete.setPosition(1920/2 - LevelComplete.getWidth()/2,1080/2);
        LevelComplete.setVisible(false);

        Stage stage = new Stage(new ScreenViewport());

        stage.addActor(Name);
        stage.addActor(health);
        stage.addActor(Money);
        stage.addActor(Gmode);
        stage.addActor(Missiles);
        stage.addActor(LevelComplete);

        return stage;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        players = engine.getEntitiesFor(Family.all(PlayerComponent.class).get());
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        e = players.first();
        player = pm.get(e);

        Name.setText(MenuStage.nameArea.getText());

        health.setText("Health: " + player.hp + "/ 100" );

        Money.setText("Treats: "+ player.money);

        if(ActionProcessor.GodMode)
            Gmode.setText("Godmode: On");
        else
            Gmode.setText("Godmode: Off");

        Missiles.setText("Missiles: " + player.weapons.get("Missile"));

        if(Game.Goal)
        {
            LevelComplete.setVisible(true);
        }
        else
            LevelComplete.setVisible(false);
    }
}
