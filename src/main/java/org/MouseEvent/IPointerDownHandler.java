package org.MouseEvent;


import javafx.scene.Node;
import org.GameObject;
import org.IHasNode;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

import static utils.NodeUtils.collectNodes;

public interface IPointerDownHandler {
    /**
     * Provides a contract for handling pointer (mouse) press events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override {@link #onPointerDown()}
     * to define the behavior that occurs when a node is pressed.
     * </p>
     */
    public void onPointerDown();
    /**
     * Attach this click handler to a {@link GameObject} and all of its children.
     * <p>
     * This will collect every component in the GameObject hierarchy that implements {@link IHasNode},
     * retrieve its underlying JavaFX {@link Node}, and register a mouse pressed event listener on it.
     * </p>
     * @param transform The root {@link Transform} {@link GameObject} to attach press detection to.
     */
    default void attachPointerDown(Transform transform) {
        List<Node> nodes = new ArrayList<>(collectNodes(transform));
        for(var node : nodes) {
            node.setOnMousePressed(e -> onPointerDown());
        }
    }

}
