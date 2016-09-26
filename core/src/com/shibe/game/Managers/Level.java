package com.shibe.game.Managers;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shibe.game.Components.SpawnComponent;
import com.shibe.game.Components.WorldComponent;

import java.util.ArrayList;

/**
 * Created by Jere on 4.6.2016.
 */
public class Level
{
    private Texture bgTexture;
    public static Sprite BgSprite;
    private float rectWidth;
    private float rectHeight;
    private float angle;
    public static int levelNmb = 1;
    private float ppt = Game.WORLD_TO_BOX;
    private Array<Shape> shapes;
    public ArrayList<Object> objects;
    private Object object;
    private ArrayList<EnemyManager> enemies = new ArrayList<EnemyManager>();
    private ArrayList<InteractableObjectManager> interactableObjects = new ArrayList<InteractableObjectManager>();
    private InteractableObjectManager interactableObjectManager;
    private EnemyManager enemyManager;
    private PlayerManager playerManager;
    private ItemManager itemManager;
    private TreasureManager treasureManager;
    String LevelData;
    TiledMap map;
    private Shape shape;
    private CircleShape circleShape;
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);


    public Level()
    {
        itemManager = new ItemManager();
        interactableObjectManager = new InteractableObjectManager();
        enemyManager = new EnemyManager();
        playerManager = new PlayerManager();
        treasureManager = new TreasureManager();
    }

    public TiledMap BuildLevel(World world)
    {
        if(map != null)
            map.dispose();
        map = new TmxMapLoader().load("Level"+levelNmb+".tmx");
        bgTexture = new Texture("BackGround"+levelNmb+".jpg");
        BgSprite = new Sprite(bgTexture);


        return map;
    }

    public void MapBodyBuilder(TiledMap map, Engine engine)
    {
        MapObjects objects = new MapObjects();
        objects = map.getLayers().get("Obstacles").getObjects();
        ImmutableArray<Entity> worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        Entity e1 = worlds.get(0);
        WorldComponent world = wm.get(e1);

        for (MapObject object:objects)
        {

            if (object instanceof TextureMapObject) {
                continue;
            }

            if (object instanceof RectangleMapObject)
            {
                shape = getRectangle((RectangleMapObject)object);
            }
            else if (object instanceof PolygonMapObject) {
                shape = getPolygon((PolygonMapObject)object);
            }
            else if (object instanceof PolylineMapObject)
            {
                shape = getPolyline((PolylineMapObject)object);
            }
            else if (object instanceof EllipseMapObject)
            {
                circleShape = getCircle((EllipseMapObject) object);
                circleShape.setPosition(new Vector2(circleShape.getRadius(), circleShape.getRadius()));
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                bd.position.set(((EllipseMapObject) object).getEllipse().x * Game.WORLD_TO_BOX,((EllipseMapObject) object).getEllipse().y * Game.WORLD_TO_BOX);
                Body body = world.world.createBody(bd);
                body.setAwake(false);
                body.createFixture(circleShape,1);
                body.setUserData("Obstacle");
                circleShape.dispose();
                continue;
            }

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.world.createBody(bd);
            body.setAwake(false);
            body.createFixture(shape,1);
            body.setUserData("Obstacle");

            shape.dispose();
        }



        objects = map.getLayers().get("Objects").getObjects();
        for(MapObject object:objects)
        {
            String objectName = object.getName();

            if(object.getName().equals("Treasure"))
            {
                shape = getRectangle((RectangleMapObject)object);
                treasureManager.CreateTreasure(world.world, shape, object);
            }

            if(object.getName().equals("StartPoint"))
            {
                shape = getRectangle((RectangleMapObject)object);
                playerManager.CreatePlayer(world.world, ((RectangleMapObject) object).getRectangle().x, ((RectangleMapObject) object).getRectangle().y);

                shape.dispose();
            }
            else if(object.getName().equals("Goal"))
            {
                shape = getRectangle((RectangleMapObject)object);
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                Body body = world.world.createBody(bd);
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.isSensor = true;
                fixtureDef.shape = shape;
                fixtureDef.density = 1;
                Fixture fixture = body.createFixture(fixtureDef);

                body.setUserData("Goal");

                shape.dispose();
            }
            else if(object.getName().equals("EnemySpawn"))
            {
                shape = getRectangle((RectangleMapObject)object);
                int spawnLink =Integer.parseInt(object.getProperties().get("SpawnTriggerLink").toString());
                int spawnType =Integer.parseInt(object.getProperties().get("EnemyType").toString());
                if(spawnLink == 0) {
                    enemyManager.createEnemy(world.world, ((RectangleMapObject) object).getRectangle().x, ((RectangleMapObject) object).getRectangle().y, spawnType);
                }
                else
                {
                    SpawnComponent spawnComponent = new SpawnComponent();
                    spawnComponent.setSpawn(spawnType, ((RectangleMapObject) object).getRectangle().x, ((RectangleMapObject) object).getRectangle().y, spawnLink);
                    Entity e = new Entity();
                    e.add(spawnComponent);
                    engine.addEntity(e);
                }

                shape.dispose();
            }
            if(object.getName().equals("EnemySpawnTrigger"))
            {
                shape = getRectangle((RectangleMapObject)object);
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                Body body = world.world.createBody(bd);
                body.setAwake(false);
                Fixture fixture = body.createFixture(shape, 1);
                fixture.setSensor(true);
                fixture.setUserData(Integer.parseInt(object.getProperties().get("SpawnTriggerLink").toString()));
                Filter filter = new Filter();
                filter.categoryBits = CollisionFilterManager.ENEMY_SENSOR;
                filter.maskBits =(short) (~CollisionFilterManager.NONE & CollisionFilterManager.PLAYER);
                fixture.setFilterData(filter);
                body.setUserData("SpawnTrigger");

                shape.dispose();
            }
        }

        objects = map.getLayers().get("MovingObjects").getObjects();
        for(MapObject object:objects)
        {
            InteractableObjectManager interactableObject;
            if(object.getName().equals("Door"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObjectManager.newObject(world.world, shape, object);
            }
            else if(object.getName().equals("Button"))
            {
                shape = getCircle((EllipseMapObject) object);
                interactableObjectManager.newObject(world.world, shape, object);
            }
            else if(object.getName().equals("Ladder"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObjectManager.newObject(world.world, shape, object);
            }
            else if(object.getName().equals("Teleport"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObjectManager.newObject(world.world, shape, object);
            }
        }
    }

    private PolygonShape getPolygon(PolygonMapObject polygonObject) {
        PolygonShape polygon = new PolygonShape();
        float[] vertices = polygonObject.getPolygon().getTransformedVertices();

        float[] worldVertices = new float[vertices.length];

        for (int i = 0; i < vertices.length; ++i) {
            System.out.println(vertices[i]);
            worldVertices[i] = vertices[i] * ppt;
            System.out.println(worldVertices[i]);
        }

        polygon.set(worldVertices);
        return polygon;
    }

    private PolygonShape getRectangle(RectangleMapObject rectangleObject) {


        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        Vector2 size = new Vector2((rectangle.x + rectangle.width * 0.5f) * ppt,
                (rectangle.y + rectangle.height * 0.5f ) * ppt);
        polygon.setAsBox(rectangle.width * 0.5f * ppt,
                rectangle.height * 0.5f * ppt,
                size,
                0);

        return polygon;
    }

    private CircleShape getCircle(EllipseMapObject ellipseMapObject) {
        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(ellipseMapObject.getEllipse().x, ellipseMapObject.getEllipse().y));
        circle.setRadius(ellipseMapObject.getEllipse().height * Game.WORLD_TO_BOX / 2);
        return circle;
    }


    private ChainShape getPolyline(PolylineMapObject polylineObject) {
        float[] vertices = polylineObject.getPolyline().getTransformedVertices();
        Vector2[] worldVertices = new Vector2[vertices.length / 2];

        for (int i = 0; i < vertices.length / 2; ++i) {
            worldVertices[i] = new Vector2();
            worldVertices[i].x = vertices[i * 2] * ppt;
            worldVertices[i].y = vertices[i * 2 + 1] * ppt;
        }

        ChainShape chain = new ChainShape();
        chain.createChain(worldVertices);
        return chain;
    }
}
