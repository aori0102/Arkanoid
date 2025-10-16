package game.Interface;


import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import org.GameObject;
import org.Transform;

import java.util.ArrayList;
import java.util.List;

import static utils.NodeUtils.collectNodes;

public interface IPointerExitHandler {
    /**
     * Provides a contract for handling pointer (mouse) exit events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override this method
     * to define the behavior that occurs when a node is exited.
     * </p>
     */
    public void onPointerExited(MouseEvent event);
    /**
     * Attach area that the event will occur.
     *
     * @param node Area that you want the event to occur on.
     *             <p>
     *             If the node has an instance of an {@link ImageView} type, the area of that image will be used.
     *             (Recommend to use {@link ImageView} to initialize effective area).
     *             </p>
     */
    /**
     * Attach this click handler to a {@link GameObject} and all of its children.
     * <p>
     * This will collect every component in the GameObject hierarchy that implements {@link org.Renderable},
     * retrieve its underlying JavaFX {@link Node}, and register a mouse exited event listener on it.
     * </p>
     *
     * @param transform The root {@link Transform} {@link GameObject} to attach press detection to.
     */
    default void attachPointerExited(Transform transform) {
        List<Node> nodes = new ArrayList<>(collectNodes(transform));
        for (var node : nodes) {
            node.setOnMouseExited(this::onPointerExited);
        }
    }
}
