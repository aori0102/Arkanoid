package org.Particle.ParticlesPrefab;

import javafx.scene.image.Image;
import org.Particle.Particle;
import utils.Vector2;


public abstract class ParticlePrefab {

    public abstract Particle spawnParticle( double speed, double lifeTime);

}
