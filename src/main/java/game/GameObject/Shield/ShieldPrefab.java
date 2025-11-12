package game.GameObject.Shield;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class ShieldPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        var shieldObject = GameObjectManager.instantiate("Shield");
        shieldObject.addComponent(Shield.class);

        var shieldVisual = GameObjectManager.instantiate("ShieldVisual");
        shieldVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Shield.getImage());
        shieldVisual.getTransform().setGlobalPosition(new Vector2(0, 720));
        shieldVisual.getTransform().setLocalScale(new Vector2(1, 0.5));
        shieldVisual.setActive(false);

        shieldObject.getComponent(Shield.class).linkShieldVisual(shieldVisual);

        return shieldObject;
    }
}
