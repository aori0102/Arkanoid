package game.Particle.ExplodingBrick;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.ParticleSystem.ParticleType;
import org.Prefab.Prefab;

public final class ExplodingBrickParticlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        var explodingBrickParticle = GameObjectManager.instantiate("ExplodingBrickParticle")
                .addComponent(ExplodingBrickParticle.class);
        explodingBrickParticle.setRadius(10);
        explodingBrickParticle.setParticleType(ParticleType.ExplodingBrick);

        return explodingBrickParticle.getGameObject();
    }
}
