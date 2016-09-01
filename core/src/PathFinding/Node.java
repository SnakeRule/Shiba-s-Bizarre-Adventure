package PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Jere on 24.8.2016.
 */
public class Node{
    private Array<Connection<Node>> connections = new Array<Connection<Node>>();
    public int type;
    public int index;

    public int getIndex(){
        return index;
    }

    public Array<Connection<Node>> getConnections(){
        return connections;
    }

    public void createConnection(Node toNode, float cost){
        connections.add(new ConnectionImp(this, toNode, cost));
    }

    public static class Type{
        public static final int REGULAR = 1;
    }
}
