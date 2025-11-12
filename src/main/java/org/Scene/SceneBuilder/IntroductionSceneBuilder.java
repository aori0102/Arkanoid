package org.Scene.SceneBuilder;

import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

public class IntroductionSceneBuilder extends SceneBuilder {
    @Override
    protected void build() {
        PrefabManager.instantiatePrefab(PrefabIndex.Introduction);
    }
}
