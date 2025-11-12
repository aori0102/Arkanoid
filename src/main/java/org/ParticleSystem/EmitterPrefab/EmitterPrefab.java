package org.ParticleSystem.EmitterPrefab;

import org.ParticleSystem.Emitter.ParticleEmitter;

public abstract class EmitterPrefab {
    /**
     * Generate emitter base and assign it's base information
     * @param emissionRate the number of particles spawned each seconds
     * @param minsSpeed minimum travel speed of the particle
     * @param maxSpeed maximum travel speed of the particle
     * @param minLife minimum lifetime of the particle
     * @param maxLife maximum lifetime of the particle
     * @param spreadAngle the spread angle of the particle - use for randomize position
     * @return the particle emitter
     */
    public abstract ParticleEmitter generateEmitter(double emissionRate
            , double minsSpeed
            , double maxSpeed
            , double minLife
            , double maxLife
            , double spreadAngle);
}
