package org.MouseEvent;

import javafx.scene.Node;
import game.Interface.IHasNode;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

public final class PointerUtils {

    private PointerUtils() {} // prevent instantiation

    public static List<Node> collectNodes(Transform transform) {
        List<Node> nodes = new ArrayList<>();

        for (var comp : transform.getGameObject().getAllComponents()) {
            if (comp instanceof IHasNode hasNode) {
                nodes.add(hasNode.getNode());
            }
        }

        for (var child : transform.getChildren()) {
            nodes.addAll(collectNodes(child));
        }

        return nodes;
    }
}

