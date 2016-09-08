package com.shibe.game.Managers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.shibe.game.Components.RenderComponent;
import com.shibe.game.Components.WorldComponent;
import com.shibe.game.MenuStage;
import com.shibe.game.PhoneOverlay;
import com.shibe.game.Systems.*;

import java.util.ArrayList;

public class Game extends ApplicationAdapter {

    public static final float WORLD_TO_BOX = 0.01f;
    public static final float BOX_TO_WORLD = 100f;

    public static boolean Menu;
    private Stage stage;
    private static boolean gameOn = false;
    public static boolean gameInit = false;
    private TiledMap map;
    private SpriteBatch hudbatch;
    private Level level;
    private BitmapFont font;
    private OrthogonalTiledMapRenderer renderer;
    public static ArrayList<Body> destroyList = new ArrayList<Body>();
    public static ArrayList<System> systems = new ArrayList<System>();
    private ArrayList<EnemyManager> enemies = new ArrayList<EnemyManager>();
    private ArrayList<InteractableObjectManager> interactableObjects = new ArrayList<InteractableObjectManager>();
    public static ArrayList<Object> objects;
    private Music rainMusic;
    private SpriteBatch batch;
    private Vector3 screenCoordinates;
    private World world;
    public static ArrayList<Entity> entities = new ArrayList<Entity>();
    private PhoneOverlay phoneOverlay;
    public static PooledEngine engine = new PooledEngine();
    public static boolean pause = false;
    private FPSLogger fpsLogger = new FPSLogger();

    private MovementSystem movementSystem = new MovementSystem();
    private DrawSystem drawSystem = new DrawSystem();
    private RenderSystem renderSystem;
    private PlayerSystem playerSystem = new PlayerSystem();
    private ObjectSystem objectSystem = new ObjectSystem();
    private DestroySystem destroySystem = new DestroySystem();
    private EnemySystem enemySystem = new EnemySystem();
    private WeaponSystem weaponSystem = new WeaponSystem();
    private SpawnSystem spawnSystem = new SpawnSystem();

    private RenderComponent renderComponent = new RenderComponent();
    private WorldComponent worldComponent = new WorldComponent();

    private Entity renderEntity = new Entity();
    private Entity cameraEntity = new Entity();
    private Entity worldEntity = new Entity();
    private Entity collisionEntity = new Entity();

    @Override
    public void create() {
        MenuStage menu = new MenuStage();
        stage = menu.CreateStage();

        Gdx.input.setInputProcessor(stage);

        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("BgMusic.mp3"));

        switch (Gdx.app.getType()) {
            case Android: {
                phoneOverlay = new PhoneOverlay();
                hudbatch = new SpriteBatch();
            }
            // android specific code
            case Desktop: {
                phoneOverlay = new PhoneOverlay();
                hudbatch = new SpriteBatch();
            }
            case WebGL:
                /// HTML5 specific code
        }

        //Loop and playback of music
        rainMusic.setLooping(true);
        rainMusic.play();

        Menu = true;
    }

    private void initGame() {
        screenCoordinates = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);
        Gdx.input.setInputProcessor(new ActionProcessor());

        CollisionFilterManager filterManager = new CollisionFilterManager();

        new WorldManager(engine);

        level = new Level();
        //level.LoadLevel(1);
        map = level.BuildLevel(world);
        level.MapBodyBuilder(map, engine);
        renderComponent.setBatch(batch = new SpriteBatch());
        renderComponent.setRenderer(renderer = new OrthogonalTiledMapRenderer(map, WORLD_TO_BOX));
        renderEntity.add(renderComponent);
        engine.addEntity(renderEntity);

        new CameraManager(engine);

        new CursorManager(engine);

        new CollisionManager(engine);

        engine.addSystem(objectSystem);
        engine.addSystem(spawnSystem);
        engine.addSystem(playerSystem);
        engine.addSystem(enemySystem);
        engine.addSystem(weaponSystem);
        engine.addSystem(movementSystem);
        engine.addSystem(drawSystem);
        renderSystem = new RenderSystem();
        engine.addSystem(renderSystem);
        engine.addSystem(destroySystem);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f / 255, 0f / 255, 0f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Menu == true) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        }
        if(gameInit == true)
        {
            initGame();
            gameOn = true;
            gameInit = false;
        }
        if(gameOn)
        {
            fpsLogger.log();
            engine.update(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
    }
}
