package org.Scene.SceneBuilder;

import org.GameObject.GameObjectManager;
import org.Scene.SceneKey;

import java.util.HashMap;

public final class SceneBuilderManager {

    private static final HashMap<SceneKey, SceneBuilder> sceneBuilderMap = new HashMap<>();

    static {
        sceneBuilderMap.put(SceneKey.Menu, new MenuSceneBuilder());
        sceneBuilderMap.put(SceneKey.InGame, new InGameSceneBuilder());
        sceneBuilderMap.put(SceneKey.DoNotDestroyOnLoad, new DoNotDestroyOnLoadSceneBuilder());
    }

    public static void buildScene(SceneKey key) {
        GameObjectManager.clearCurrentScene();
        sceneBuilderMap.get(key).build();
    }
}
