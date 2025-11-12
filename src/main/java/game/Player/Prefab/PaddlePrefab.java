package game.Player.Prefab;

import game.Damagable.HealthChangeVisualizer;
import game.GameObject.Arrow;
import game.Particle.PaddleParticle;
import game.Player.Paddle.*;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class PaddlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {

        // Main object
        var paddleObject = GameObjectManager.instantiate("Paddle")
                .addComponent(PlayerPaddle.class)
                .addComponent(PaddleHealth.class)
                .addComponent(PaddleEffectController.class)
                .addComponent(PaddleStat.class)
                .addComponent(BoxCollider.class)
                .getGameObject();
        var paddle = paddleObject.getComponent(PlayerPaddle.class);
        paddleObject.getTransform().setGlobalPosition(new Vector2(300, 700));
        paddleObject.setLayer(Layer.Paddle);

        // Collider
        var collider = paddleObject.getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(64, 16));

        // Visual
        var paddleVisualObject = GameObjectManager.instantiate("PaddleVisual")
                .addComponent(PaddleVisual.class)
                .addComponent(SpriteRenderer.class)
                .getGameObject();
        var paddleVisual = paddleVisualObject.getComponent(PaddleVisual.class);
        paddleVisual.linkPaddle(paddle);
        paddleVisualObject.setParent(paddleObject);
        paddleVisualObject.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Paddle.getImage());
        paddleVisualObject.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        // Indicator arrow
        var arrow = GameObjectManager.instantiate("Arrow");
        arrow.addComponent(Arrow.class);
        arrow.setParent(paddleObject);
        arrow.getTransform().setLocalPosition(new Vector2(0, 0));
        arrow.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Arrow.getImage());
        arrow.getComponent(SpriteRenderer.class).setPivot(new Vector2(0, 0.5));
        arrow.getTransform().setLocalPosition(new Vector2(0, -20));
        paddleObject.getComponent(PlayerPaddle.class).linkArrow(arrow.getComponent(Arrow.class));

        // Health change visualizer
        var healthChangeVisualizer = PrefabManager.instantiatePrefab(PrefabIndex.HealthChange_VisualizeHandler)
                .getComponent(HealthChangeVisualizer.class);
        healthChangeVisualizer.getGameObject().setParent(paddleObject);
        healthChangeVisualizer.linkEntityHealth(paddleObject.getComponent(PaddleHealth.class));
        var paddleParticle = GameObjectManager.instantiate("PaddleParticle").addComponent(PaddleParticle.class);
        paddleParticle.setPosition(new Vector2(40, 0));
        paddleParticle.setDirection(Vector2.down());
        paddleParticle.setParent(paddleObject);
        paddleParticle.startEmit();

        var paddleParticle1 = GameObjectManager.instantiate("PaddleParticle").addComponent(PaddleParticle.class);
        paddleParticle1.setPosition(new Vector2(-40, 0));
        paddleParticle1.setDirection(Vector2.down());
        paddleParticle1.setParent(paddleObject);
        paddleParticle1.startEmit();

        var dashParticle = GameObjectManager.instantiate("DashParticle")
                .addComponent(PaddleDashParticle.class);
        dashParticle.setParent(paddleObject);
        paddle.getComponent(PlayerPaddle.class).linkPaddleDashParticle(dashParticle);

        return paddleObject;

    }

}