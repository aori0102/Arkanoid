package game.Voltraxis.Prefab;

import game.Voltraxis.Object.UltimateLaser.UltimateLaserVisual;
import game.Voltraxis.Object.UltimateLaser.UltimateLaserDamageDealer;
import game.Voltraxis.Object.UltimateLaser.UltimateLaserStat;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import utils.Vector2;

/**
 * Prefab of Voltraxis' ultimate EX laser.
 */
public final class UltimateLaserPrefab extends Prefab {

    private static final Vector2 LASER_SIZE = new Vector2(128.0, 720.0);
    private static final Vector2 LASER_COLLIDER_OFFSET = new Vector2(64.0, 0.0);

    @Override
    public GameObject instantiatePrefab() {

        var laserObject = GameObjectManager.instantiate("Laser")
                .addComponent(UltimateLaserStat.class)
                .addComponent(UltimateLaserDamageDealer.class)
                .addComponent(BoxCollider.class)
                .getGameObject();

        // Collider
        var boxCollider = laserObject.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(LASER_SIZE);
        boxCollider.setLocalCenter(LASER_COLLIDER_OFFSET);
        boxCollider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());
        boxCollider.setTrigger(true);

        // Visual
        var visual = GameObjectManager.instantiate("VisualObject")
                .addComponent(UltimateLaserVisual.class)
                .addComponent(SpriteAnimator.class);
        var visualAnimator = visual.addComponent(SpriteAnimator.class);
        visualAnimator.addAnimationClip(AnimationClipData.Voltraxis_UltimateLaser);
        visual.getGameObject().setParent(laserObject);

        return laserObject;

    }

}