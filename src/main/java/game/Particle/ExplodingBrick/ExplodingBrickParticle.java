package game.Particle.ExplodingBrick;

import org.GameObject.GameObject;
import org.ParticleSystem.Emitter.CircleEmitter;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

public class ExplodingBrickParticle extends ParticlePrefab {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ExplodingBrickParticle(GameObject owner) {
        super(owner);
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Circle)
                .generateEmitter(50, 100, 300, 0.5, 1.5, 30);
        particleEmitter.setParticleType(ParticleType.ExplodingBrick);
    }

    public void setRadius(double radius) {
        if (particleEmitter instanceof CircleEmitter circleEmitter) {
            circleEmitter.setRadius(radius);
        }
    }

}
