package org.ParticleSystem.Particles;

import game.Player.Player;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.Emitter.EmitTypes;
import org.ParticleSystem.Emitter.ParticleEmitter;
import org.ParticleSystem.EmitterGenerator;
import org.ParticleSystem.ParticleType;
import utils.Vector2;

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
