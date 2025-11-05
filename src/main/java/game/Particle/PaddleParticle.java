package game.Particle;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

// TODO: Remove if not transferring event from central class to particle emitter
public class PaddleParticle extends ParticlePrefab {


    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleParticle(GameObject owner) {
        super(owner);
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(300, 100, 300, 0.3, 0.8, 30);
        particleEmitter.setParticleType(ParticleType.Energy);
    }

}
