package org.Particle.ParticlesPrefab;

import javafx.scene.image.Image;
import org.GameObject.GameObjectManager;
import org.Particle.Particle;
import utils.Vector2;

public class FireParticlePrefab extends ParticlePrefab{
    @Override
    public Particle spawnParticle( double speed, double lifeTime) {
        var fireParticle = GameObjectManager.instantiate("FireParticle").addComponent(Particle.class);
        fireParticle.setParticleType(ParticleType.Fire);
        fireParticle.assignSpecs(speed,lifeTime);

        return fireParticle;
    }
}
