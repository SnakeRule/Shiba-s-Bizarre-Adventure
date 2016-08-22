package com.shibe.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.ArrayList;
import java.util.Collections;

public class Game extends ApplicationAdapter implements InputProcessor {

    public static final float WORLD_TO_BOX = 0.01f;
    public static final float BOX_TO_WORLD = 100f;

    private Stage stage;
    private Skin skin;
    private int weaponNmb = 1;
    private boolean gameOn = false;
    private boolean keyPressed;
    private TiledMap map;
    private Body bodyA;
    private Body bodyB;
    private Vector2 mousepos;
    private boolean leftPressed;
    private boolean rightPressed;
    protected Player player;
    private Level level;
    private BitmapFont font;
    private OrthogonalTiledMapRenderer renderer;
    private Weapon weapon;
    private Object object = new Object();
    private ArrayList<Body> destroyList = new ArrayList<Body>();
    private ArrayList<Weapon> weaponDestroyList = new ArrayList<Weapon>();
    private ArrayList<Weapon> weaponTmp = new ArrayList<Weapon>();
    private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    private ArrayList<InteractableObject> interactableObjects = new ArrayList<InteractableObject>();
    public static ArrayList<Object> objects;
    private Music rainMusic;
    private ContactListener contactListener;
    private OrthographicCamera camera;
    Vector3 mousePosition;
    private SpriteBatch batch;
    Vector3 screenCoordinates;
    private Box2DDebugRenderer debugRenderer;
    private World world;
    private PhoneOverlay phoneOverlay;
    private SpriteBatch hudbatch;
    private Table table;
    private TextButton startButton;
    private TextButton quitButton;
    Array<Body> bodies = new Array<Body>();
    private boolean pause = false;


    @Override
    public void create() {

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

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (pause == false)
                    initGame();
                gameOn = true;
            }
        });
        //initGame();
        //gameOn = true;
    }

    private void initGame() {
        batch = new SpriteBatch();

        //Box2D world creation
        world = new World(new Vector2(0, -10), true);

        screenCoordinates = new Vector3(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0);

        //Box2D renderer
        debugRenderer = new Box2DDebugRenderer();

        level = new Level();
        //level.LoadLevel(1);
        map = level.BuildLevel(world, player);
        level.MapBodyBuilder(world, map);
        enemies = level.enemies;
        interactableObjects = level.interactableObjects;
        renderer = new OrthogonalTiledMapRenderer(map, WORLD_TO_BOX);
        player = level.player;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 16f, 9f);
        contactListener = new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();
                bodyA = a.getBody();
                bodyB = b.getBody();


                for (InteractableObject interactableObject : interactableObjects)
                    for (InteractableObject interactableObject1 : interactableObjects) {
                        {
                            if (b.getBody().getUserData() == interactableObject.buttonSprite) {
                                if (interactableObject1.objectname.equals("Door") && interactableObject1.Nmb.equals(b.getUserData())) {
                                    interactableObject1.DoorMove(interactableObject1.doorBody);
                                    for (Weapon weapon : player.weapons) {
                                        if (a.getBody().getUserData() == weapon.weaponSprite) {
                                            destroyList.add(a.getBody());
                                            weapon.destroy = true;
                                            break;
                                        }
                                    }
                                }
                            } else if (a.getBody().getUserData() == interactableObject.buttonSprite) {
                                if (interactableObject1.objectname.equals("Door") && interactableObject1.Nmb.equals(a.getUserData())) {
                                    interactableObject1.DoorMove(interactableObject1.doorBody);
                                    for (Weapon weapon : player.weapons) {
                                        if (b.getBody().getUserData() == weapon.weaponSprite) {
                                            destroyList.add(b.getBody());
                                            weapon.destroy = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }


                if (a.getBody().getUserData() == player.dogeSprite || b.getBody().getUserData() == player.dogeSprite) {
                    if (a.getBody().getUserData() == player.dogeSprite && b.getBody().getUserData() == "Goal") {
                        camera.setToOrtho(false, 32f, 18f);
                        return;
                    }
                    for (Enemy enemy : enemies) {
                        if (a.getBody().getUserData() == player.dogeSprite && b.getBody().getUserData() == enemy.dogeSprite) {
                            enemies.remove(enemy);
                            destroyList.add(b.getBody());
                            break;
                        }
                    }
                }
            }

            @Override
            public void endContact(Contact contact) {
                Fixture a = contact.getFixtureA();
                Fixture b = contact.getFixtureB();

                if (a.getBody().getUserData() == player.dogeSprite && b.getBody().getUserData() == "Goal") {
                    camera.setToOrtho(false, 32f / 2, 18f / 2);
                }
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {

            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {

            }
        };

        mousePosition = new Vector3();
        world.setContactListener(contactListener);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f / 255, 0f / 255, 0f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (gameOn == false) {
            stage.act(Gdx.graphics.getDeltaTime());
            stage.draw();
        } else {
            Gdx.input.setInputProcessor(this);
            camera.position.set(player.dogeSprite.getX(), (float) 4.5, 0);
            camera.update();
            mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            renderer.setView(camera);
            renderer.render();
            world.getBodies(bodies);
            Sprite sprite;
            batch.begin();
            batch.setProjectionMatrix(camera.combined);
            for (Body body : bodies) {
                if (body.getUserData() instanceof Sprite) {
                    sprite = (Sprite) body.getUserData();
                    // update location and angle of sprite
                    sprite.setPosition(body.getPosition().x - sprite.getWidth() / 2, body.getPosition().y - sprite.getHeight() / 2);
                    sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
                    sprite.draw(batch); // spriteBatch must be defined elsewhere
                    if (body.getUserData() == player.dogeSprite) {
                        if (body.getPosition().y < 0) {
                            body.setTransform(level.SpawnX * WORLD_TO_BOX, level.SpawnY * WORLD_TO_BOX, 0);
                        }
                        if ((body.getLinearVelocity().x > 0 || body.getLinearVelocity().x < 0) && keyPressed == true) {
                            player.Animate(body, rightPressed, leftPressed);
                        }
                    }
                }
            }
            if (phoneOverlay != null) {
            }
            camera.unproject(mousePosition);
            player.AimReticle.setPosition(mousePosition.x - player.AimReticle.getWidth() / 2, mousePosition.y - player.AimReticle.getHeight() / 2);
            player.AimReticle.draw(batch);
            batch.end();
            //Matrix4 cameraCopy = camera.combined.cpy();
            debugRenderer.render(world, camera.combined);
            world.step(1 / 60f, 6, 2);
            destroyList();
        }
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean keyDown(int keycode) {
        for (Body body : bodies) {
            if (body.getUserData() == player.dogeSprite) {
                if (keycode == Input.Keys.D) {
                    body.setLinearVelocity(5, body.getLinearVelocity().y);
                    keyPressed = true;
                    rightPressed = true;
                } else if (keycode == Input.Keys.A) {
                    body.setLinearVelocity(-5, body.getLinearVelocity().y);
                    keyPressed = true;
                    leftPressed = true;
                } else if (keycode == Input.Keys.W && body.getLinearVelocity().y > -0.1 && body.getLinearVelocity().y < 0.1) {
                    body.applyForceToCenter(0, 350, true);
                }
            }

        }
        if (keycode == Input.Keys.NUM_1) {
            weaponNmb = 1;
        } else if (keycode == Input.Keys.NUM_2) {
            weaponNmb = 2;
        }
        if (keycode == Input.Keys.ESCAPE) {
            Gdx.input.setInputProcessor(stage);
            startButton.setText("Resume");
            pause = true;
            gameOn = false;
        }
        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == Input.Keys.D) {
            for (Body body : bodies) {
                if (body.getUserData() == player.dogeSprite) {
                    body.setLinearVelocity(0, body.getLinearVelocity().y);
                    keyPressed = false;
                    rightPressed = false;
                }
            }
        }
        if (keycode == Input.Keys.A) {
            for (Body body : bodies) {
                if (body.getUserData() == player.dogeSprite) {
                    body.setLinearVelocity(0, body.getLinearVelocity().y);
                    keyPressed = false;
                    leftPressed = false;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        int i = 0;
        if (gameOn)
            player.Shoot(world, player.dogeSprite.getX(), player.dogeSprite.getY(), (player.AimReticle.getX() + player.AimReticle.getWidth() / 2), player.AimReticle.getY() + player.AimReticle.getHeight() / 2, player.dogeSprite.isFlipX(), player.dogeSprite.getWidth(), player.dogeSprite.getHeight(), weaponNmb);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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

    private void destroyList() {
        if (destroyList.isEmpty() == false) {
            for (Body body : destroyList) {
                world.destroyBody(body);
            }
        }
        for (Weapon weapon : player.weapons) {
            if (weapon.destroy) {
                player.weapons.remove(weapon);
                break;
            }
        }
        destroyList.clear();
    }
}
