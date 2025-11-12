package org.Prefab;

import org.GameObject.GameObject;

/**
 * The {@code PrefabManager} class provides utility methods for creating instances
 * of prefabs. It acts as a central point to instantiate {@link GameObject} objects
 * from predefined prefabs identified by a {@link PrefabIndex}.
 *
 * <p><b>Usage example:</b></p>
 * <pre>{@code
 * GameObject enemy = PrefabManager.instantiatePrefab(PrefabIndex.ENEMY);
 * }</pre>
 */
public class PrefabManager {

    /**
     * Instantiates a {@link GameObject} from the given {@link PrefabIndex}.
     * <p>
     * This method retrieves the prefab associated with the provided {@code prefabIndex}
     * and calls its {@link Prefab#instantiatePrefab()} method to create a new instance.
     * </p>
     *
     * @param prefabIndex the {@link PrefabIndex} identifying which prefab to instantiate
     * @return a new {@link GameObject} instance created from the specified prefab
     */
    public static GameObject instantiatePrefab(PrefabIndex prefabIndex) {
        return prefabIndex.prefab.instantiatePrefab();
    }
}
