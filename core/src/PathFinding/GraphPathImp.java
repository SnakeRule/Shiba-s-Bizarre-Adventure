package PathFinding;

import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

/**
 * Created by Jere on 25.8.2016.
 */
class GraphPathImp implements GraphPath<Node> {
    private Array<Node> nodes = new Array<Node>();

    public GraphPathImp() {}

    @Override
    public Iterator<Node> iterator() {
        return nodes.iterator();
    }

    @Override
    public int getCount() {
        return nodes.size;
    }

    @Override
    public Node get(int i) {
        return nodes.get(i);
    }

    public Node removeIndex(int index) {
        return nodes.removeIndex(index);
    }

    @Override
    public void add(Node node) {
        nodes.add(node);
    }

    @Override
    public void clear() {
        nodes.clear();
    }

    @Override
    public void reverse() {
        nodes.reverse();
    }
}
