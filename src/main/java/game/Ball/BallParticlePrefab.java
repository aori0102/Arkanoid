package game.Ball;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import game.Particle.BallParticle;
import org.Prefab.Prefab;

public final class BallParticlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("BallParticle")
                .addComponent(BallParticle.class)
                .getGameObject();
    }
}
