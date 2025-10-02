package org;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RendererManager {

    private static Group root = null;

    /**
     * Initialize the program windows and render graph.
     * Should only be called within {@code main}.
     *
     * @param stage The stage of the application.
     */
    public static void initializeMain(Stage stage, Scene mainScene, Group mainGroup) {

        // root node (empty container for now)
         root = mainGroup;

        // scene = like your canvas
        Scene scene = mainScene;

        stage.setTitle("NigArkanoid");
        stage.setScene(scene);
        stage.show(); // 🚀 show the window

    }

    /**
     * Register {@code node} for rendering.
     *
     * @param node The registering node.
     */
    protected static void registerNode(Node node) {
        root.getChildren().add(node);
    }

    /**
     * Remove {@code node} from rendering.
     *
     * @param node The unregistering node.
     */
    protected static void unregisterNode(Node node) {
        root.getChildren().remove(node);
    }

}
