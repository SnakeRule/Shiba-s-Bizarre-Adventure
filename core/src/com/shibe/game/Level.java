package com.shibe.game;

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

import java.util.ArrayList;

/**
 * Created by Jere on 4.6.2016.
 */
public class Level
{
    public Texture bg;
    float ppt = Game.WORLD_TO_BOX;
    public Sprite bgSprite;
    private String objectName;
    public TiledMap map;
    private Array<Shape> shapes;
    public ArrayList<Object> objects;
    private Object object;
    public Enemy enemy;
    public ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public float SpawnX;
    public float SpawnY;
    public ArrayList<InteractableObject> interactableObjects = new ArrayList<InteractableObject>();
    public InteractableObject interactableObject;
    String LevelData;
    Player player;
    private Shape shape;

    public TiledMap BuildLevel(World world, Character player)
    {
        /*player = player;
        int row = 0;
        int field = 0;
        int x = 0;
        int y = 0;
        int f = 0;
        objects = new ArrayList<Object>();
        //enemies = new ArrayList<Enemy>();

        for(row = 0; row < 27; row++)
        {
            y = 1080 - (row * 40);
            for(field = 0; field < 24; field++)
            {
                x = field * 128;
                switch(LevelData.charAt(f))
                {
                    case '0':
                    {
                        f++;
                        break;
                    }
                    case '\n':
                    {
                        f++;
                        break;
                    }
                    case '1':
                    {
                        ground = new InteractableObject(world, "2", x, y);
                        objects.add(ground);
                        f++;
                        break;
                    }
                    case '2':
                    {
                        ground = new InteractableObject(world ,"3", x, y);
                        objects.add(ground);
                        f++;
                        break;
                    }
                    case 'P':
                    {
                        player = new Character(world, x,y);
                        f++;
                        break;
                    }
                    /*case 'E':
                    {
                        enemy = new Enemy(x, y, world);
                        enemies.add(enemy);
                        f++;
                        break;
                    }
                    default:
                    {
                        f++;
                        break;
                    }
                }
            }
        }*/
        map = new TmxMapLoader().load("Level1.tmx");
        bg = new Texture("BackGround.jpg");
        bgSprite = new Sprite(bg);
        return map;
    }

    /*public void LoadLevel(int levelNumber)
    {
        FileHandle fileHandle;
        try {
            fileHandle = Gdx.files.internal("Level"+levelNumber+".txt");
            LevelData = fileHandle.readString();
        } catch (Exception exception) {
        }
        finally {
        }
    }*/

    public void MapBodyBuilder(World world, TiledMap map)
    {
        MapObjects objects = new MapObjects();
        objects = map.getLayers().get("Obstacles").getObjects();

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
            Body body = world.createBody(bd);
            body.createFixture(shape, 1);

            shape.dispose();
        }

        /*objects = map.getLayers().get("Player").getObjects();
        for (MapObject object:objects)
        {
            if (object instanceof  TextureMapObject)
            {
                player = new Character(world, 80,300, (TextureMapObject) object);
            }

            if (object instanceof PolygonMapObject)
            {
                shape = getPolygon((PolygonMapObject)object);
                player.HitBox(shape, world);
            }
            else if (object instanceof PolylineMapObject && player != null)
            {
                shape = getPolyline((PolylineMapObject)object);
                player.HitBox(shape, world);
            }
        }*/

        objects = map.getLayers().get("Objects").getObjects();
        for(MapObject object:objects)
        {
            objectName = object.getName();
            if(object.getName().equals("StartPoint"))
            {
                shape = getRectangle((RectangleMapObject)object);
                player = new Player(world, (RectangleMapObject)object, map);
                SpawnX = ((RectangleMapObject) object).getRectangle().x;
                SpawnY = ((RectangleMapObject) object).getRectangle().y;
            }
            else if(object.getName().equals("Goal"))
            {
                shape = getRectangle((RectangleMapObject)object);
                BodyDef bd = new BodyDef();
                bd.type = BodyDef.BodyType.StaticBody;
                Body body = world.createBody(bd);
                body.createFixture(shape, 1);
                body.setUserData("Goal");

                shape.dispose();
            }
            else if(object.getName().equals("EnemySpawn"))
            {
                shape = getRectangle((RectangleMapObject)object);
                enemy = new Enemy(world, (RectangleMapObject)object, map);
                enemies.add(enemy);
            }
        }

        objects = map.getLayers().get("MovingObjects").getObjects();
        for(MapObject object:objects)
        {
            if(object.getName().equals("Door"))
            {
                shape = getRectangle((RectangleMapObject) object);
                interactableObject = new InteractableObject(world, shape, object);
                interactableObjects.add(interactableObject);
            }
            else if(object.getName().equals("Button"))
            {
                shape = getCircle((EllipseMapObject) object);
                interactableObject = new InteractableObject(world, shape, object);
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
