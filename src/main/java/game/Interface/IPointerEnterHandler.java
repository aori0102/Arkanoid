package org.MouseEvent;


import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import org.GameObject;
import org.IRenderable;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

import static utils.NodeUtils.collectNodes;

public interface IPointerEnterHandler {
    /**
     * Provides a contract for handling pointer (mouse) enter events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override this method
     * to define the behavior that occurs when a node is entered.
     * </p>
     */
    public void onPointerEntered(MouseEvent event);

    /**
     * Attach this click handler to a {@link GameObject} and all of its children.
     * <p>
     * This will collect every component in the GameObject hierarchy that implements {@link IRenderable},
     * retrieve its underlying JavaFX {@link Node}, and register a mouse entered event listener on it.
     * </p>
     *
     * @param transform The root {@link Transform} {@link GameObject} to attach enter detection to.
     */
    default void attachPointerEnter(Transform transform) {
        List<Node> nodes = new ArrayList<>(collectNodes(transform));
        for (var node : nodes) {
            node.setOnMouseEntered(this::onPointerEntered);
        }
    }
}

