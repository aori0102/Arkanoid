package game.Particle;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

// TODO: Doc + fix offsetting
// TODO: Remove if not transferring event from central class to particle emitter
public class BallParticle extends ParticlePrefab {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallParticle(GameObject owner) {
        super(owner);
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(50, 300, 600, 0.1, 0.3, 1);
        particleEmitter.setParticleType(ParticleType.Ball);
    }
}
