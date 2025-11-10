package org.Scene.SceneBuilder;

import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

public final class DoNotDestroyOnLoadSceneBuilder extends SceneBuilder {
    @Override
    protected void build() {
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_GameManager)
                .setDoNotDestroyOnLoad(true);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ConfigManager)
                .setDoNotDestroyOnLoad(true);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ProgressManager)
                .setDoNotDestroyOnLoad(true);
    }
}
