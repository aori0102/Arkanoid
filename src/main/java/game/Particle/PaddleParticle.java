package game.Particle;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

/**
 * PaddleParticle is a particle effect prefab used for the player paddle.
 * <p>
 * This class generates a cone-shaped particle emitter that emits particles
 * of type Energy around the paddle, typically for visual effects while moving.
 * </p>
 */
public class PaddleParticle extends ParticlePrefab {

    /**
     * Create this PaddleParticle.
     *
     * @param owner The owner GameObject of this particle effect (usually the paddle).
     */
    public PaddleParticle(GameObject owner) {
        super(owner);
        // Generate a cone-shaped particle emitter with specified parameters
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(
                        300, // min particles
                        100, // max particles
                        300, // emission rate
                        0.3, // min lifetime
                        0.8, // max lifetime
                        30   // spawn interval
                );
        // Assign particle type as Energy
        particleEmitter.setParticleType(ParticleType.Energy);
    }
}
