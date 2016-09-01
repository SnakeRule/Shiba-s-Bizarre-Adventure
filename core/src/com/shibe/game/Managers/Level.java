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
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.shibe.game.Components.WorldComponent;

import java.util.ArrayList;

/**
 * Created by Jere on 4.6.2016.
 */
class Level
{
    private Texture bg;
    private float ppt = Game.WORLD_TO_BOX;
    private Sprite bgSprite;
    private String objectName;
    private TiledMap map;
    private Array<Shape> shapes;
    public ArrayList<Object> objects;
    private Object object;
    private EnemyManager enemy;
    private ArrayList<EnemyManager> enemies = new ArrayList<EnemyManager>();
    private float SpawnX;
    private float SpawnY;
    private ArrayList<InteractableObjectManager> interactableObjects = new ArrayList<InteractableObjectManager>();
    private InteractableObjectManager interactableObject;
    String LevelData;
    private PlayerManager player;
    private Shape shape;
    private ComponentMapper<WorldComponent> wm = ComponentMapper.getFor(WorldComponent.class);
    private ImmutableArray<Entity> worlds;
    private Entity e = new Entity();

    public TiledMap BuildLevel(World world)
    {
        map = new TmxMapLoader().load("Level1.tmx");
        bg = new Texture("BackGround.jpg");
        bgSprite = new Sprite(bg);
        return map;
    }

    public void MapBodyBuilder(TiledMap map, Engine engine)
    {
        MapObjects objects = new MapObjects();
        objects = map.getLayers().get("Obstacles").getObjects();
        worlds = engine.getEntitiesFor(Family.all(WorldComponent.class).get());
        e = worlds.get(0);
        WorldComponent world = wm.get(e);

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

            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.world.createBody(bd);
            body.createFixture(shape, 1);
            body.setUserData("Obstacle");

            shape.dispose();
        }



        objects = map.getLayers().get("Objects").getObjects();
        for(MapObject object:objects)
        {
            objectName = object.getName();
            if(object.getName().equals("StartPoint"))
            {
                shape = getRectangle((RectangleMapObject)object);
                player = new PlayerManager(world.world, (RectangleMapObject)object, map);
                SpawnX = ((RectangleMapObject) object).getRectangle().x;
                SpawnY = ((RectangleMapObject) object).getRectangle().y;

                shape.dispose();
            }
            else if(object.getName().equals("Goal"))
            {
                shape = getRectangle((RectangleMapObject)object);
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                Body body = world.world.createBody(bd);
                body.createFixture(shape, 1);
                body.setUserData("Goal");

                shape.dispose();
            }
            else if(object.getName().equals("EnemySpawn"))
            {
                shape = getRectangle((RectangleMapObject)object);
                enemy = new EnemyManager(world.world, (RectangleMapObject)object, map);
                enemies.add(enemy);

                shape.dispose();
            }
        }

        objects = map.getLayers().get("MovingObjects").getObjects();
        for(MapObject object:objects)
        {
            if(object.getName().equals("Door"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObject = new InteractableObjectManager(world.world, shape, object);
                interactableObjects.add(interactableObject);
            }
            else if(object.getName().equals("Button"))
            {
                shape = getCircle((EllipseMapObject) object);
                interactableObject = new InteractableObjectManager(world.world, shape, object);
                interactableObjects.add(interactableObject);
            }
            else if(object.getName().equals("Ladder"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObject = new InteractableObjectManager(world.world, shape, object);
                interactableObjects.add(interactableObject);
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
                0.0f);
        return polygon;
    }

    private CircleShape getCircle(EllipseMapObject ellipseMapObject) {
        CircleShape circle = new CircleShape();
        circle.setPosition(new Vector2(ellipseMapObject.getEllipse().x, ellipseMapObject.getEllipse().y));
        circle.setRadius(ellipseMapObject.getEllipse().area()*Game.WORLD_TO_BOX);
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
