package org.Rendering;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.Event.EventActionID;
import org.Layer.RenderLayer;
import org.Scene.SceneManager;

import java.util.EnumMap;
import java.util.HashMap;

public class RendererManager {

    private final static EnumMap<RenderLayer, Group> groupByLayerMap = new EnumMap<>(RenderLayer.class);
    private final static HashMap<Renderable, EventActionID> renderableLayerChangeEventIDMap = new HashMap<>();

    /**
     * Initialize the program windows and render graph.
     * Should only be called within {@code main}.
     *
     * @param stage The stage of the application.
     */
    public static void initializeRenderSystem(Stage stage, Scene mainScene) {

        // Children rendering nodes for per-layer rendering
        var scene = SceneManager.getScene();
        var root = (Group) scene.getRoot();
        var renderLayerArray = RenderLayer.values();

        for (var renderLayer : renderLayerArray) {

            Group childGroup = new Group();
            root.getChildren().add(childGroup);
            groupByLayerMap.put(renderLayer, childGroup);

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
        renderableLayerChangeEventIDMap.put(
                renderable,
                renderable.onRenderLayerChanged.addListener(RendererManager::renderable_onRenderLayerChanged)
        );
        groupByLayerMap.get(renderable.getRenderLayer())
                .getChildren().add(renderable.getNode());
    }

    /**
     * Remove {@code node} from rendering.
     *
     * @param renderable The unregistering rendering object.
     */
    protected static void unregisterNode(Renderable renderable) {
        renderable.onRenderLayerChanged.removeListener(renderableLayerChangeEventIDMap.get(renderable));
        groupByLayerMap.get(renderable.getRenderLayer())
                .getChildren().remove(renderable.getNode());
    }

    private static void renderable_onRenderLayerChanged(Object sender, Renderable.OnRenderLayerChangedEventArgs e) {

        if (sender instanceof Renderable renderable) {
            groupByLayerMap.get(e.previousLayer())
                    .getChildren().remove(renderable.getNode());
            groupByLayerMap.get(e.newLayer())
                    .getChildren().add(renderable.getNode());
        }

    }

}
