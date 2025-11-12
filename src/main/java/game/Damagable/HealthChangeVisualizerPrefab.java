package game.Damagable;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public final class HealthChangeVisualizerPrefab extends Prefab {

    /**
     * Instantiate new HealthChangeVisualizer
     * @return new HealthChangeVisualizer
     */
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("DamageVisualizer")
                .addComponent(HealthChangeVisualizer.class)
                .getGameObject();
    }

}