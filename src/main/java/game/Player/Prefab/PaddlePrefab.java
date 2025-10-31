package game.Player.Prefab;

import game.GameObject.Arrow;
import game.Player.PaddleDamageAcceptor;
import game.Player.PlayerPaddle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class PaddlePrefab implements IPlayerPrefab {
    @Override
    public GameObject instantiatePrefab() {

        // Main object
        var paddle = GameObjectManager.instantiate("Paddle");
        paddle.getTransform().setGlobalPosition(new Vector2(300,700));
        paddle.addComponent(PlayerPaddle.class)
                .addComponent(BoxCollider.class)
                .addComponent(PaddleDamageAcceptor.class);
        paddle.setLayer(Layer.Player);

        // Collider
        var collider = paddle.getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(64, 16));

        // Visual
        var paddleVisual = GameObjectManager.instantiate("PaddleVisual");
        paddleVisual.setParent(paddle);
        paddleVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Paddle.getImage());
        paddleVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        // Indicator arrow
        var arrow = GameObjectManager.instantiate("Arrow");
        arrow.addComponent(Arrow.class);
        arrow.setParent(paddle);
        arrow.getTransform().setLocalPosition(new Vector2(0, 0));
        arrow.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Arrow.getImage());
        arrow.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
        paddle.getComponent(PlayerPaddle.class).linkArrow(arrow.getComponent(Arrow.class));

        return paddle;

    }

}