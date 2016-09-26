package com.shibe.game.Managers;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import com.shibe.game.AndroidStage;
import com.shibe.game.Components.RenderComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.HudStage;
import com.shibe.game.MenuStage;
import com.shibe.game.Systems.*;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

    public static final float WORLD_TO_BOX = 0.010f;
    public static final float BOX_TO_WORLD = 100f;

    public static boolean AprocessorSet; // Used when switching actionprocessor from menu to game
    private Timer goalTimer;
    public static boolean androidDebug = false;
    public static boolean Android;
    public static boolean Menu;
    public static boolean Save;
    public static boolean Load;
    public static boolean Quit;
    private Stage stage;
    private Stage buttonStage;
    public static boolean gameOn = false;
    public static boolean gameStart = false;
    private BitmapFont font;
    public static ArrayList<Body> destroyList = new ArrayList<Body>();
    public static ArrayList<System> systems = new ArrayList<System>();
    private ArrayList<EnemyManager> enemies = new ArrayList<EnemyManager>();
    private ArrayList<InteractableObjectManager> interactableObjects = new ArrayList<InteractableObjectManager>();
    public static ArrayList<Object> objects;
    private World world;
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    public static PooledEngine engine = new PooledEngine();
    public static boolean pause = false;
    private FPSLogger fpsLogger = new FPSLogger();
    public static Vector2 ScreenCoordinates;

    private RenderComponent renderComponent = new RenderComponent();
    private WorldComponent worldComponent = new WorldComponent();

    MovementSystem movementSystem;
    PlayerSystem playerSystem;
    ObjectSystem objectSystem;
    DestroySystem destroySystem;
    EnemySystem enemySystem;
    WeaponSystem weaponSystem;
    SpawnSystem spawnSystem;
    TreasureSystem treasureSystem;
    RenderSystem renderSystem;
    SaveSystem saveSystem;

    CollisionFilterManager filterManager = new CollisionFilterManager();
    CameraManager cameraManager;
    CursorManager cursorManager;
    private WorldManager worldManager;
    Level level;

    private Entity renderEntity = new Entity();
    private Entity cameraEntity = new Entity();
    private Entity worldEntity = new Entity();
    private Entity collisionEntity = new Entity();

    public static boolean Goal;

    MenuStage menu;
    AndroidStage androidStage;
    HudStage hudStage;
    Stage hud;

    public static InputMultiplexer inputMultiplexer = new InputMultiplexer();
    SpriteBatch batch;

    @Override
    public void create() {
        ScreenCoordinates = new Vector2(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        Gdx.graphics.setVSync(true);
        Gdx.input.setInputProcessor(stage);

        Music bgMusic = Gdx.audio.newMusic(Gdx.files.internal("BgMusic.mp3"));

        switch (Gdx.app.getType()) {
            case Android:
            {
                Android = true;
                androidStage = new AndroidStage();
                Gdx.input.setCatchBackKey(true);
            }

            // android specific code
            case Desktop: {
                if(androidDebug)
                    androidStage = new AndroidStage();
            }
            case WebGL:
                /// HTML5 specific code
        }

        //Loop and playback of music
        bgMusic.setLooping(true);
        bgMusic.play();

        Menu = true;

        level = new Level();

        movementSystem = new MovementSystem();
        playerSystem = new PlayerSystem();
        objectSystem = new ObjectSystem();
        destroySystem = new DestroySystem();
        enemySystem = new EnemySystem();
        weaponSystem = new WeaponSystem();
        spawnSystem = new SpawnSystem();
        treasureSystem = new TreasureSystem();
        renderSystem = new RenderSystem();

        hudStage = new HudStage();
        worldManager = new WorldManager();
        hud = hudStage.CreateStage();
        cameraManager = new CameraManager();
        cursorManager = new CursorManager();
        saveSystem = new SaveSystem();

        menu = new MenuStage();
        stage = menu.CreateStage();
    }

    private void initGame() {

        Gdx.input.setInputProcessor(new ActionProcessor());

        if(androidStage != null || androidDebug)
            buttonStage = androidStage.CreateStage();


        worldManager.initWorld();
        cameraManager.initCamera();
        cursorManager.initCursor();

        new CollisionManager(engine);

        TiledMap map = level.BuildLevel(world);
        level.MapBodyBuilder(map, engine);
        batch = new SpriteBatch();
        renderComponent.setBatch(batch);
        OrthogonalTiledMapRenderer renderer;
        renderComponent.setRenderer(renderer = new OrthogonalTiledMapRenderer(map, WORLD_TO_BOX));
        renderEntity.add(renderComponent);
        engine.addEntity(renderEntity);

        engine.addSystem(objectSystem);
        engine.addSystem(spawnSystem);
        engine.addSystem(treasureSystem);
        engine.addSystem(playerSystem);
        engine.addSystem(enemySystem);
        engine.addSystem(weaponSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(hudStage);
        engine.addSystem(saveSystem);
        engine.addSystem(renderSystem);
        engine.addSystem(destroySystem);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f / 255, 0f / 255, 0f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(gameStart == true)
        {
            initGame();
            gameOn = true;
            gameStart = false;
            pause = false;
        }
        if(gameOn && pause == false)
        {
            if(!AprocessorSet) {
                if (androidStage != null)
                    inputMultiplexer.addProcessor(buttonStage);
                inputMultiplexer.addProcessor(new ActionProcessor());
                Gdx.input.setInputProcessor(inputMultiplexer);
                AprocessorSet = true;
            }
            fpsLogger.log();
            engine.update(Gdx.graphics.getDeltaTime());
            if(androidStage != null || androidDebug) {
                buttonStage.act(Gdx.graphics.getDeltaTime());
                buttonStage.draw();
            }
            hud.act(Gdx.graphics.getDeltaTime());
            hud.draw();
        }

        if(Menu == true && gameOn == false) {
            //engine.update(Gdx.graphics.getDeltaTime());
            menu.GameOff();
            menu.quitButton.setText("Quit Game");
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
            Gdx.input.setInputProcessor(stage);
            AprocessorSet = false;
        }

        if(Menu == true && gameOn == true) {
            engine.update(Gdx.graphics.getDeltaTime());
            menu.Paused();
            menu.quitButton.setText("Back to Main Menu");
            hud.act(Gdx.graphics.getDeltaTime());
            hud.draw();
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
            Gdx.input.setInputProcessor(stage);
            AprocessorSet = false;
        }

        if(Save == true)
        {
            menu.Save();
        }
        if(Load == true)
        {
            menu.LoadData();
        }
    }

    @Override
    public void dispose() {
    }

}
