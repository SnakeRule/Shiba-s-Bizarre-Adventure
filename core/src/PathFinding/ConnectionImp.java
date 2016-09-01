package PathFinding;

import com.badlogic.gdx.ai.pfa.Connection;

/**
 * Created by Jere on 24.8.2016.
 */
public class ConnectionImp implements Connection<Node> {
    private Node toNode;
    private Node fromNode;
    private float cost;

    public ConnectionImp(Node fromNode, Node toNode, float cost){
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.cost = cost;
    }

    @Override
    public float getCost() {
        return cost;
    }

    @Override
    public Node getFromNode() {
        return fromNode;
    }

    @Override
    public Node getToNode() {
        return toNode;
    }
}
