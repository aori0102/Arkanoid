package org.MouseEvent;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.GameObject;
import org.IHasNode;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

import static org.MouseEvent.PointerUtils.collectNodes;


public interface IPointerClickHandler {
    /**
     * Provides a contract for handling pointer (mouse) click events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override {@link #onPointerClicked()}
     * to define the behavior that occurs when a node is clicked.
     * </p>
     */
    public void onPointerClicked();

    /**
     * Attach this click handler to a {@link GameObject} and all of its children.
     * <p>
     * This will collect every component in the GameObject hierarchy that implements {@link IHasNode},
     * retrieve its underlying JavaFX {@link Node}, and register a mouse click event listener on it.
     * </p>
     * @param transform The root {@link Transform} {@link GameObject} to attach click detection to.
     */
    default void attachPointerClick(Transform transform) {
        List<Node> nodes = new ArrayList<>(collectNodes(transform));
        for(var node : nodes) {
            node.setOnMouseClicked(e -> onPointerClicked());
        }
    }

}
