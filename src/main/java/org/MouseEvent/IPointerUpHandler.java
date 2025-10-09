package org.MouseEvent;


import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.GameObject;
import org.IRenderable;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

import static org.MouseEvent.PointerUtils.collectNodes;

public interface IPointerUpHandler {
    /**
     * Provides a contract for handling pointer (mouse) release events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override this method
     * to define the behavior that occurs when a node is released.
     * </p>
     */
    public void onPointerUp(MouseEvent event);

    /**
     * Attach this click handler to a {@link GameObject} and all of its children.
     * <p>
     * This will collect every component in the GameObject hierarchy that implements {@link IRenderable},
     * retrieve its underlying JavaFX {@link Node}, and register a mouse released event listener on it.
     * </p>
     *
     * @param transform The root {@link Transform} {@link GameObject} to attach release detection to.
     */
    default void attachPointerUp(Transform transform) {
        List<Node> nodes = new ArrayList<>(collectNodes(transform));
        for (var node : nodes) {
            node.setOnMouseReleased(this::onPointerUp);
        }
    }
}
