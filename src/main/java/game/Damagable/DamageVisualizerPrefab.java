package game.Damagable;

import org.GameObject.GameObjectManager;

public final class DamageVisualizerPrefab {

    public static DamagePopUpHandler instantiatePrefab() {
        return GameObjectManager.instantiate("DamageVisualizer")
                .addComponent(DamagePopUpHandler.class);
    }

}
