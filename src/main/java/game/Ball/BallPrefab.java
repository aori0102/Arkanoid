package game.Ball;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.ParticleSystem.Emitter.ConeEmitter;
import org.ParticleSystem.ParticleType;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

// TODO: Make constants
public final class BallPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        // Core
        var ballObject = GameObjectManager.instantiate("ball")
                .addComponent(Ball.class)
                .addComponent(BallEffectController.class)
                .addComponent(BallDamageDealer.class)
                .addComponent(BallStat.class)
                .getGameObject();
        ballObject.setLayer(Layer.Ball);

        // Ball component
        var ball = ballObject.getComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        // Collider
        var ballCollider = ballObject.getComponent(BoxCollider.class);
        ballCollider.setExcludeLayer(Layer.combineLayerMask(Layer.Ball, Layer.PowerUp));
        ballCollider.setLocalCenter(new Vector2(0, 0));
        ballCollider.setLocalSize(new Vector2(20, 16));

        // Visual
        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball.getGameObject());
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        // Particle object
        var particleEmitter = PrefabManager.instantiatePrefab(PrefabIndex.Ball_Particle)
                .addComponent(ConeEmitter.class);
        particleEmitter.setEmissionRate(50);
        particleEmitter.setMinSpeed(100);
        particleEmitter.setMaxSpeed(300);
        particleEmitter.setMinLifeTime(0.1);
        particleEmitter.setMaxLifeTime(0.3);
        particleEmitter.setSpreadAngle(1);
        particleEmitter.setParticleType(ParticleType.Ball);
        particleEmitter.getGameObject().setParent(ballObject);

        // Link component
        ball.linkBallParticle(particleEmitter);

        return ballObject;

    }

}