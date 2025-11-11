package game.Voltraxis.Prefab;

import game.Voltraxis.Object.ElectricBall.ElectricBall;
import game.Voltraxis.Object.ElectricBall.ElectricBallDamageDealer;
import game.Voltraxis.Object.ElectricBall.ElectricBallStat;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

/**
 * Prefab of Voltraxis' {@link ElectricBall}.
 */
public final class ElectricBallPrefab extends Prefab {

    private static final Vector2 BALL_SIZE = new Vector2(64.0, 64.0);

    @Override
    public GameObject instantiatePrefab() {

        // Main object
        var electricBallObject = GameObjectManager.instantiate("Electric Ball")
                .addComponent(ElectricBallStat.class)
                .addComponent(ElectricBallDamageDealer.class)
                .addComponent(ElectricBall.class)
                .getGameObject();

        // Collider
        var boxCollider = electricBallObject.addComponent(BoxCollider.class);
        boxCollider.setLocalSize(BALL_SIZE);
        boxCollider.setIncludeLayer(Layer.Paddle.getUnderlyingValue());

        // Rendering
        var spriteRenderer = electricBallObject.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.Voltraxis_ElectricBall.getImage());
        spriteRenderer.setSize(BALL_SIZE);
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));

        return electricBallObject;

    }

}