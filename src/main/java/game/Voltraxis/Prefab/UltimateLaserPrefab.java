package game.Voltraxis.Prefab;

import game.Voltraxis.Object.UltimateLaser;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import utils.Vector2;

/**
 * Prefab of Voltraxis' ultimate EX laser.
 */
public final class UltimateLaserPrefab implements IVoltraxisPrefab {

    private static final Vector2 LASER_SIZE = new Vector2(128.0, 720.0);
    private static final Vector2 LASER_COLLIDER_OFFSET = new Vector2(64.0, 0.0);

    @Override
    public GameObject instantiatePrefab() {

        var laser = GameObjectManager.instantiate("Laser").addComponent(UltimateLaser.class);
        var animator = laser.addComponent(SpriteAnimator.class);
        animator.addAnimationClip(AnimationClipData.Voltraxis_UltimateLaser);
        var boxCollider = laser.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(LASER_SIZE);
        boxCollider.setLocalCenter(LASER_COLLIDER_OFFSET);
        boxCollider.setIncludeLayer(Layer.Player.getUnderlyingValue());
        boxCollider.setTrigger(true);

        return laser.getGameObject();

    }

}