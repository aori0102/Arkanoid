package game.Particle;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

/**
 * BallParticle is a particle effect prefab used for ball-related visual effects.
 * <p>
 * When instantiated, it creates a particle emitter of type "Cone" that emits
 * Ball particles with specific speed, lifetime, and emission rate parameters.
 * </p>
 */
public class BallParticle extends ParticlePrefab {

    /**
     * Create this BallParticle.
     *
     * @param owner The owner GameObject of this particle effect.
     */
    public BallParticle(GameObject owner) {
        super(owner);
        // Generate a cone-shaped particle emitter
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(
                        50,    // min particles
                        300,   // max particles
                        600,   // emission rate
                        0.1,   // min lifetime
                        0.3,   // max lifetime
                        1      // spawn interval
                );
        // Assign particle type as Ball
        particleEmitter.setParticleType(ParticleType.Ball);
    }
}
