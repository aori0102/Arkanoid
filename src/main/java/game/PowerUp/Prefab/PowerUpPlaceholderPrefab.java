package game.PowerUp.Prefab;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public final class PowerUpPlaceholderPrefab extends Prefab {

    private static final Vector2 POWER_UP_SIZE = new Vector2(65.0, 65.0);

    @Override
    public GameObject instantiatePrefab() {

        var powerUpObject = GameObjectManager.instantiate("PowerUp")
                .addComponent(BoxCollider.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();
        powerUpObject.setLayer(Layer.PowerUp);

        // Renderer
        var spriteRenderer = powerUpObject.getComponent(SpriteRenderer.class);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
        spriteRenderer.setSize(POWER_UP_SIZE);

        var boxCollider = powerUpObject.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(POWER_UP_SIZE);
        boxCollider.setTrigger(true);

        return powerUpObject;

    }
}
