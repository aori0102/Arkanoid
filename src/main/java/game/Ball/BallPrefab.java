package game.Ball;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public final class BallPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        // Core
        var ballObject = GameObjectManager.instantiate("ball")
                .addComponent(Ball.class)
                .addComponent(BallDamageDealer.class)
                .addComponent(BallStat.class)
                .getGameObject();

        // Ball component
        var ball = ballObject.getComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        // Visual
        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball.getGameObject());
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        return ballObject;

    }

}