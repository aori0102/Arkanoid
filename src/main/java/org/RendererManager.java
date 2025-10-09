package org;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.EnumMap;

public class RendererManager {

    private static class SceneInfo {
        public Scene scene = null;
        public Group root = null;
        public EnumMap<RenderLayer, Group> renderLayerGroupMap = null;
    }

    private final static EnumMap<SceneKey, SceneInfo> sceneInfoMap = new EnumMap<>(SceneKey.class);

    /**
     * Initialize the program windows and render graph.
     * Should only be called within {@code main}.
     *
     * @param stage The stage of the application.
     */
    public static void initializeRenderSystem(Stage stage, Scene mainScene) {

        // Children rendering nodes for per-layer rendering
        var renderLayerArray = RenderLayer.values();
        var sceneArray = SceneKey.values();
        var sceneMap = SceneManager.getSceneMap();
        for (var sceneKey : sceneArray) {

            if (!sceneMap.containsKey(sceneKey)) {
                throw new IllegalStateException("Scene " + sceneKey + " doesn't exist");
            }

            var scene = sceneMap.get(sceneKey);
            var root = (Group) scene.getRoot();
            EnumMap<RenderLayer, Group> renderLayerMap = new EnumMap<>(RenderLayer.class);

            for (var renderLayer : renderLayerArray) {

                Group childGroup = new Group();
                root.getChildren().add(childGroup);
                renderLayerMap.put(renderLayer, childGroup);

            }

            var sceneInfo = new SceneInfo();
            sceneInfo.scene = scene;
            sceneInfo.root = root;
            sceneInfo.renderLayerGroupMap = renderLayerMap;
            sceneInfoMap.put(sceneKey, sceneInfo);

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
        var sceneKey = renderable.gameObject.getRegisteredSceneKey();
        sceneInfoMap.get(sceneKey).renderLayerGroupMap.get(renderable.getRenderLayer())
                .getChildren().add(renderable.getNode());
    }

    /**
     * Remove {@code node} from rendering.
     *
     * @param renderable The unregistering rendering object.
     */
    protected static void unregisterNode(Renderable renderable) {
        renderable.onRenderLayerChanged.removeListener(RendererManager::renderable_onRenderLayerChanged);
        var sceneKey = renderable.gameObject.getRegisteredSceneKey();
        sceneInfoMap.get(sceneKey).renderLayerGroupMap.get(renderable.getRenderLayer())
                .getChildren().remove(renderable.getNode());
    }

    private static void renderable_onRenderLayerChanged(Object sender, Renderable.OnRenderLayerChangedEventArgs e) {

        if (sender instanceof Renderable renderable) {
            var sceneKey = renderable.gameObject.getRegisteredSceneKey();
            sceneInfoMap.get(sceneKey).renderLayerGroupMap.get(e.previousLayer())
                    .getChildren().remove(renderable.getNode());
            sceneInfoMap.get(sceneKey).renderLayerGroupMap.get(e.newLayer())
                    .getChildren().add(renderable.getNode());
        }

    }

}
