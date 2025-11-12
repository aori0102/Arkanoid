package org.ParticleSystem.EmitterPrefab;

import org.GameObject.GameObjectManager;
import org.ParticleSystem.Emitter.CircleEmitter;
import org.ParticleSystem.Emitter.ParticleEmitter;

/**
 * Spawning a circle emitter - which will emit the particle in a circle shape field.
 */
public class CircleEmitterPrefab extends EmitterPrefab{
    @Override
    public ParticleEmitter generateEmitter(double emissionRate
            , double minsSpeed
            , double maxSpeed
            , double minLife
            , double maxLife
            , double spreadAngle) {
        var circleEmitter = GameObjectManager.instantiate("CircleEmitter").addComponent(CircleEmitter.class);
        circleEmitter.setEmissionRate(emissionRate);
        circleEmitter.setMinSpeed(minsSpeed);
        circleEmitter.setMaxSpeed(maxSpeed);
        circleEmitter.setMinLifeTime(minLife);
        circleEmitter.setMaxLifeTime(maxLife);
        circleEmitter.setSpreadAngle(spreadAngle);

        return circleEmitter;
    }
}
