package ecs;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

public class RendererManager {

    private static Group root = null;

    /**
     * Initialize the program windows and render graph.
     * Should only be called within {@code main}.
     *
     * @param stage The stage of the application.
     */
    public static void initializeMain(Stage stage) {

        // root node (empty container for now)
        root = new Group();

        // scene = like your canvas
        Scene scene = new Scene(root, 1200, 800);

        stage.setTitle("NigArkanoid");
        stage.setScene(scene);
        stage.show(); // 🚀 show the window

    }

    /**
     * Register {@code node} for rendering.
     *
     * @param node The registering node.
     */
    public static void RegisterNode(Node node) {
        root.getChildren().add(node);
    }

    /**
     * Remove {@code node} from rendering.
     *
     * @param node The unregistering node.
     */
    protected static void UnregisterNode(Node node) {
        root.getChildren().remove(node);
    }

}
