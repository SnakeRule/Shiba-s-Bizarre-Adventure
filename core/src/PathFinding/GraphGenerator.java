package PathFinding;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Jere on 24.8.2016.
 */
class GraphGenerator {
    public static GraphImp generateGraph(TiledMap map){
        Array<Node> nodes = new Array<Node>();

        TiledMapTileLayer tiles = (TiledMapTileLayer)map.getLayers().get("Tile Layer 1");
        int mapHeight = LevelManager.lvlTileHeight;
        int mapWidth = LevelManager.lvlTileWidth;

        //Loops over the tiles in the map, starting from bottom left corner
        //Iterating left to right, then down to up
        for(int y = 0; y < mapHeight; y++){
            for(int x = 0; x < mapWidth; ++x){
                // generate a node for each tile so that they all exist when we create connections
                Node node = new Node();
                node.type = Node.Type.REGULAR;
                nodes.add(node);
            }
        }

        for(int y = 0; y < mapHeight; ++y){
            for(int x = 0; x < mapWidth; ++x){
                TiledMapTileLayer.Cell target = tiles.getCell(x, y);
                TiledMapTileLayer.Cell up = tiles.getCell(x, y+1);
                TiledMapTileLayer.Cell left = tiles.getCell(x-1, y);
                TiledMapTileLayer.Cell right = tiles.getCell(x+1, y);
                TiledMapTileLayer.Cell down = tiles.getCell(x, y-1);

                Node targetNode = nodes.get(mapWidth * y + x);
                if(target == null){
                    if(y !=0 && down == null){
                        Node downNode = nodes.get(mapWidth * (y - 1) + x);
                        targetNode.createConnection(downNode, 1);
                    }
                    if(x !=0 && left == null){
                        Node leftNode = nodes.get(mapWidth * y + x -1);
                        targetNode.createConnection(leftNode, 1);
                    }
                    if(x != mapWidth - 1 && right == null){
                        Node rightNode = nodes.get(mapWidth * y + x + 1);
                        targetNode.createConnection(rightNode, 1);
                    }
                    if(y != mapHeight - 1 && up == null) {
                        Node upNode = nodes.get(mapWidth * (y+1) + x);
                        targetNode.createConnection(upNode, 1);
                    }
                }
            }
        }

        return new GraphImp(nodes);
    }
}
