package PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Jere on 24.8.2016.
 */
public class GraphImp implements IndexedGraph<Node>{
    private Array<Node> nodes = new Array<Node>();

    public GraphImp(){
        super();
    }

    public GraphImp(int capacity){
        int capacity1 = capacity;
    }

    public GraphImp(Array<Node> nodes){
        this.nodes = nodes;

        //speedier than indexOf()
        for (int x = 0; x < nodes.size; ++x){
            nodes.get(x).index = x;
        }
    }

    public Node getNodeByXY(int x, int y){
        int modX = x / LevelManager.tilePixelWidth;
        int modY = y / LevelManager.tilePixelHeight;

        return  nodes.get(LevelManager.lvlTileWidth * modY + modX);
    }


    @Override
    public int getIndex(Node node) {
        return nodes.indexOf(node,true);
    }

    @Override
    public int getNodeCount() {
        return nodes.size;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node fromNode) {
        return fromNode.getConnections();
    }
}
