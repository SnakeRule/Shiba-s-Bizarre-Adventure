package com.shibe.game;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.shibe.game.Managers.Game;
import com.shibe.game.Managers.Level;
import com.shibe.game.Systems.SaveSystem;

import java.io.*;
import java.nio.file.Files;


/**
 * Created by Jere on 27.8.2016.
 */
public class MenuStage extends EntitySystem implements java.io.Serializable {
    Timer buttonTimer;
    Array<Button> levels = new Array<Button>();
    Array<Button> levels_temp = new Array<Button>();
    private Dialog loadDialog;
    public static TextButton startButton;
    public static TextField nameArea;
    public static TextButton deleteButton;
    public static TextButton quitButton;
    Texture bgTexture = new Texture("MenuBG.jpg");
    Image bg;
    Label instructions;
    Label nameDoge;
    SelectBox<Button> levelSelect;
    Button level1;
    Button level2;
    Button level3;
    Stage stage;
    Skin skin;
    public static Preferences prefs;

    public Stage CreateStage() {
        TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("uiskin.atlas"));
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setWidth(stage.getWidth());
        table.align(Align.center | Align.top);

        table.setPosition(0, Gdx.graphics.getHeight());

        startButton = new TextButton("Start Game", skin);
        startButton.setHeight(60);
        quitButton = new TextButton("Quit Game", skin);
        quitButton.setHeight(60);
        deleteButton = new TextButton("Delete Save", skin);
        deleteButton.setHeight(60);
        instructions = new Label("1.Ball\n2.Bork \n3.Missile \n4.Ultra Balls (Slows the web browser version)", skin);
        instructions.setColor(Color.WHITE);
        Pixmap labelColor = new Pixmap((int) instructions.getWidth(), (int) instructions.getHeight(), Pixmap.Format.RGBA8888);
        labelColor.setColor(0, 0, 0, (float) 0.6);
        labelColor.fill();
        instructions.getStyle().background = new Image(new Texture(labelColor)).getDrawable();
        nameDoge = new Label("Name your doge", skin);
        nameArea = new TextField("Doge", skin);

        table.padTop(100);
        table.add(nameDoge).padBottom(20);
        table.row();
        table.add(nameArea).padBottom(50).width(600);
        table.row();
        table.add(startButton).padBottom(50);
        table.row();
        table.add(quitButton).padBottom(50);
        table.row();
        table.add(deleteButton).padBottom(50);
        table.row();
        table.add(instructions).padBottom(50);
        table.row();

        levelSelect = new SelectBox<Button>(skin);
        levelSelect.setHeight(60);
        level1 = new Button(skin);
        level1.setName("Level 1");
        levels.add(level1);
        level2 = new Button(skin);
        level2.setName("Level 2");
        levels.add(level2);
        level3 = new Button(skin);
        level3.setName("Level 3");
        levels.add(level3);


        levelSelect.setItems(level1);
        levelSelect.setSelected(level1);

        table.add(levelSelect);

        bg = new Image(bgTexture);

        stage.addActor(bg);
        stage.addActor(table);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Game.gameOn == false) {
                    Game.gameStart = true;
                    Game.Menu = false;
                } else if (Game.pause == true && Game.gameOn == true) {
                    Game.Menu = false;
                    Game.pause = false;
                }
            }
        });
        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!Game.gameOn)
                    Gdx.app.exit();
                else
                    Game.Quit = true;
            }
        });

        levelSelect.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                switch (levelSelect.getSelectedIndex()) {
                    case 0: {
                        Level.levelNmb = levelSelect.getSelectedIndex() + 1;
                        break;
                    }
                    case 1: {
                        Level.levelNmb = levelSelect.getSelectedIndex() + 1;
                        break;
                    }
                }
            }
        });

        deleteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                DeleteData();
            }
        });

        nameArea.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (nameArea.getText().equals("Doge"))
                    nameArea.setText("");
            }
        });

        LoadData();

        return stage;
    }

    public void Load(PlayerData data) {
        nameArea.setText(data.name);
        levelSelect.clearItems();
        for (int i = 0; i < data.levelsCompleted + 1; i++) {
            levels_temp.add(levels.get(i));
        }
        levelSelect.setItems(levels_temp);
        levels_temp.clear();
    }

    public void Paused() {
        bg.setVisible(false);
        levelSelect.setVisible(false);
        nameArea.setVisible(false);
        deleteButton.setVisible(false);
    }

    public void GameOff() {
        bg.setVisible(true);
        levelSelect.setVisible(true);
        nameArea.setVisible(true);
        deleteButton.setVisible(true);
    }

    public void Save() {
        if (Game.Android) {
            prefs = Gdx.app.getPreferences("My Preferences");
            prefs.putString("name", SaveSystem.playerData.name);
            prefs.putInteger("maxHp", SaveSystem.playerData.maxhp);
            prefs.putInteger("money", SaveSystem.playerData.money);
            prefs.putInteger("levelsCompleted", SaveSystem.playerData.levelsCompleted);
            prefs.flush();
            System.out.println("Saved");
        } else {
            try {
                FileOutputStream fileOut = new FileOutputStream("save.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(SaveSystem.playerData);
                out.close();
            } catch (IOException i) {
                i.printStackTrace();
            }
        }
        Game.Save = false;
    }


    public void LoadData() {
        if (Game.Android) {
            prefs = Gdx.app.getPreferences("My Preferences");
            SaveSystem.playerData.name = prefs.getString("name");
            SaveSystem.playerData.maxhp = prefs.getInteger("maxHp");
            SaveSystem.playerData.money = prefs.getInteger("money");
            SaveSystem.playerData.levelsCompleted = prefs.getInteger("levelsCompleted");
            prefs.flush();
            System.out.println("Loaded");
        } else {
            try {
                FileInputStream fileIn = new FileInputStream("save.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
                SaveSystem.playerData = (PlayerData) in.readObject();
                in.close();
                fileIn.close();

            } catch (IOException i) {
                i.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                System.out.println("class not found");
                c.printStackTrace();
                return;
            }
        }
        Game.Load = false;
        Load(SaveSystem.playerData);
    }

    private void DeleteData() {
        if (Game.Android) {
            prefs = Gdx.app.getPreferences("My Preferences");
            prefs.clear();
            prefs.flush();
        } else {
            Gdx.files.local("save.ser").delete();
        }
        SaveSystem.playerData.Reset();
        Load(SaveSystem.playerData);
    }
}
