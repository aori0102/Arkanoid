package org;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;

public class RendererManager {

    private static Group root = null;
    private final static EnumMap<RenderLayer, Group> renderLayerGroupMap = new EnumMap<>(RenderLayer.class);

    /**
     * Initialize the program windows and render graph.
     * Should only be called within {@code main}.
     *
     * @param stage The stage of the application.
     */
    public static void initializeMain(Stage stage, Scene mainScene, Group mainGroup) {

        // Root rendering node
        root = mainGroup;

        // Children rendering nodes for per-layer rendering
        var renderLayerArray = RenderLayer.values();
        for (var renderLayer : renderLayerArray) {
            Group childGroup = new Group();
            root.getChildren().add(childGroup);
            renderLayerGroupMap.put(renderLayer, childGroup);
        }

        // Set window title
        stage.setTitle("NigArkanoid");

        // Set main window
        stage.setScene(mainScene);

        // Show window
        stage.show();

    }

    /**
     * Register {@code node} for rendering.
     *
     * @param renderable The registering rendering object.
     */
    protected static void registerNode(Renderable renderable) {
        renderable.onRenderLayerChanged.addListener(RendererManager::renderable_onRenderLayerChanged);
        renderLayerGroupMap.get(renderable.getRenderLayer())
                .getChildren().add(renderable.getNode());
    }

    /**
     * Remove {@code node} from rendering.
     *
     * @param renderable The unregistering rendering object.
     */
    protected static void unregisterNode(Renderable renderable) {
        renderable.onRenderLayerChanged.removeListener(RendererManager::renderable_onRenderLayerChanged);
        renderLayerGroupMap.get(renderable.getRenderLayer())
                .getChildren().remove(renderable.getNode());
    }

    private static void renderable_onRenderLayerChanged(Object sender, Renderable.OnRenderLayerChangedEventArgs e) {

        if (sender instanceof Renderable renderable) {
            renderLayerGroupMap.get(e.previousLayer())
                    .getChildren().remove(renderable.getNode());
            renderLayerGroupMap.get(e.newLayer())
                    .getChildren().add(renderable.getNode());
        }

    }

}
