package game.Player.Paddle;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ParticlePrefab;

public class PaddleDashParticle extends ParticlePrefab {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleDashParticle(GameObject owner) {
        super(owner);
        particleEmitter = EmitterGenerator.emitterHashMap.get(EmitTypes.Cone)
                .generateEmitter(50, 1000, 3000, 0.1, 0.2, 1);
        particleEmitter.setParticleType(ParticleType.Dash);
    }


}
