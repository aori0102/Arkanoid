package utils;

import javafx.scene.Node;
import org.Renderable;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

public final class NodeUtils {

    private NodeUtils() {} // prevent instantiation

    public static List<Node> collectNodes(Transform transform) {
        List<Node> nodes = new ArrayList<>();

        for (var comp : transform.getGameObject().getAllComponents()) {
            if (comp instanceof Renderable hasNode) {
                nodes.add(hasNode.getNode());
            }
        }

        for (var child : transform.getChildren()) {
            nodes.addAll(collectNodes(child));
        }

        return nodes;
    }
}

