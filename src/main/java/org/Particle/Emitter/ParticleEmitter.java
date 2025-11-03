package org.Particle.Emitter;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Particle.Particle;
import org.Particle.ParticleGenerator;
import org.Particle.ParticlesPrefab.ParticleType;
import utils.Random;
import utils.Time;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticleEmitter extends MonoBehaviour {

    protected double emissionRate;
    protected double emissionTime;
    protected double spreadAngle;
    protected double minSpeed;
    protected double maxSpeed;
    protected double minLifeTime;
    protected double maxLifeTime;
    protected double timer = 0;

    protected boolean isEmitting;

    protected EmitTypes emitType;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ParticleEmitter(GameObject owner) {
        super(owner);
    }

    @Override
    public void update() {
        emitCounter();
    }

    protected void emitCounter() {
        timer += Time.getDeltaTime();
        emissionTime = 1 / emissionRate;

        if (timer >= emissionTime) {
            isEmitting = true;
            timer = 0;
        } else isEmitting = false;
    }

    public abstract void emit( ParticleType particleType);

    protected abstract void handleDirection();

    protected abstract Vector2 generateSpawnPosition();

    protected Particle spawnParticles(ParticleType particleType) {
        double speed = Random.range(minSpeed, maxSpeed);
        double lifetime =  Random.range(minLifeTime, maxLifeTime);

        Particle particle = ParticleGenerator.particlePrefabHashMap
                .get(particleType).spawnParticle(speed, lifetime);

        return particle;
    }

    protected void setEmitType(EmitTypes emitType) {
        this.emitType = emitType;
    }

    public void assignEmitterSpecs(double emissionRate, double spreadAngle,
                                   double minSpeed, double maxSpeed,
                                   double minLifeTime, double maxLifeTime) {
        this.emissionRate = emissionRate;
        this.spreadAngle = spreadAngle;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.minLifeTime = minLifeTime;
        this.maxLifeTime = maxLifeTime;
    }

    public void start() {
        isEmitting = true;
    }

    public void stop() {
        isEmitting = false;
    }
}
