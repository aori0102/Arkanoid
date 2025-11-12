package game.Particle.ExplodingBrick;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.CircleEmitter;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

/**
 * ExplodingBrickParticle is a particle effect prefab used when a brick explodes.
 * <p>
 * This class generates a circular particle emitter that emits particles of type
 * ExplodingBrick with specified quantity, lifetime, and emission rate.
 * </p>
 */
public class ExplodingBrickParticle extends ParticlePrefab {

    /**
     * Create this ExplodingBrickParticle.
     *
     * @param owner The owner GameObject of this particle effect.
     */
    public ExplodingBrickParticle(GameObject owner) {
        super(owner);
        // Generate a circle-shaped particle emitter
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Circle)
                .generateEmitter(
                        50,   // min particles
                        100,  // max particles
                        300,  // emission rate
                        0.5,  // min lifetime
                        1.5,  // max lifetime
                        30    // spawn interval
                );
        // Assign particle type as ExplodingBrick
        particleEmitter.setParticleType(ParticleType.ExplodingBrick);
    }

    /**
     * Set the radius of the circular particle emitter.
     *
     * @param radius The radius of the emitter circle.
     */
    public void setRadius(double radius) {
        if (particleEmitter instanceof CircleEmitter circleEmitter) {
            circleEmitter.setRadius(radius);
        }
    }
}
