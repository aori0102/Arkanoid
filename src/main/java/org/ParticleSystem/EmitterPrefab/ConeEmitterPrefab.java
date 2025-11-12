package org.ParticleSystem.EmitterPrefab;

import org.GameObject.GameObjectManager;
import org.ParticleSystem.Emitter.ConeEmitter;
import org.ParticleSystem.Emitter.ParticleEmitter;

/**
 * Spawning a cone emitter - which will emit the particle in a cone shape field.
 */
public class ConeEmitterPrefab extends EmitterPrefab {
    @Override
    public ParticleEmitter generateEmitter(double emissionRate
            , double minsSpeed
            , double maxSpeed
            , double minLife
            , double maxLife
            , double spreadAngle) {
        var coneEmitter =  GameObjectManager.instantiate("Circle_Emitter").addComponent(ConeEmitter.class);
        coneEmitter.setEmissionRate(emissionRate);
        coneEmitter.setMinSpeed(minsSpeed);
        coneEmitter.setMaxSpeed(maxSpeed);
        coneEmitter.setMinLifeTime(minLife);
        coneEmitter.setMaxLifeTime(maxLife);
        coneEmitter.setSpreadAngle(spreadAngle);

        return coneEmitter;
    }
}
