package org.Prefab;

import org.GameObject.GameObject;

public class PrefabManager {
    public static GameObject instantiatePrefab(PrefabIndex prefabIndex) {
        return prefabIndex.prefab.instantiatePrefab();
    }
}