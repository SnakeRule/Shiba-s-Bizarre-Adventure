package com.shibe.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shibe.game.Managers.Game;

/**
 * Created by Jere on 27.8.2016.
 */
public class MenuStage
{
    private Skin skin;
    private Table table;
    public static TextButton startButton;
    public static TextButton quitButton;
    private Stage stage;

    public Stage CreateStage()
    {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

        table.setPosition(0, Gdx.graphics.getHeight());

        startButton = new TextButton("New Game", skin);
        quitButton = new TextButton("Quit Game", skin);

        table.padTop(30);
        table.add(startButton).padBottom(30);
        table.row();
        table.add(quitButton);

        stage.addActor(table);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Game.pause == false) {
                    Game.gameInit = true;
                    Game.Menu = false;
                }
            }
        });

        return stage;
    }
}
