package org.ParticleSystem.Emitter;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.ParticleObject;
import org.ParticleSystem.ParticlePool;
import org.ParticleSystem.ParticleType;
import utils.Random;
import utils.Time;
import utils.Vector2;

public abstract class ParticleEmitter extends MonoBehaviour {

    /// The spawn rate of particle
    protected double emissionRate;

    ///  Delay time between each spawn
    protected double emissionTime;

    /// Spread Angle of the particle in the world position
    protected double spreadAngle;

    /// Minimum travel speed
    protected double minSpeed;

    /// Maximum travel speed
    protected double maxSpeed;
    protected double minLifeTime;
    protected double maxLifeTime;
    protected double timer = 0;
    protected Vector2 baseDirection;

    protected boolean isEmitting;
    protected boolean canEmit = false;

    protected EmitTypes emitType;
    protected ParticleType particleType;

    public ParticleEmitter(GameObject owner) {
        super(owner);
    }

    @Override
    public void update() {
        if (!canEmit) return;
        emitCounter();
        if (isEmitting) {
            emit();
        }
    }

    protected void emitCounter() {
        timer += Time.getDeltaTime();
        emissionTime = 1 / emissionRate;

        if (timer >= emissionTime) {
            isEmitting = true;
            timer = 0;
        } else isEmitting = false;
    }

    public abstract void emit();

    protected abstract Vector2 generateSpawnPosition();

    protected ParticleObject spawnParticles(ParticleType particleType) {
        double speed = Random.range(minSpeed, maxSpeed);
        double lifetime = Random.range(minLifeTime, maxLifeTime);

        ParticleObject particle = ParticlePool.getInstance().getParticle(particleType);
        particle.setParticleType(particleType);

        particle.assignSpecs(speed, lifetime);
        particle.getGameObject().setActive(true);

        return particle;
    }

    protected void setEmitType(EmitTypes emitType) {
        this.emitType = emitType;
    }

    public void setEmissionRate(double emissionRate) {
        this.emissionRate = emissionRate;
    }

    public void setSpreadAngle(double spreadAngle) {
        this.spreadAngle = spreadAngle;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMinLifeTime(double minLifeTime) {
        this.minLifeTime = minLifeTime;
    }

    public void setMaxLifeTime(double maxLifeTime) {
        this.maxLifeTime = maxLifeTime;
    }

    public void setParticleType(ParticleType particleType) {
        this.particleType = particleType;
    }

    public void startEmit() {
        canEmit = true;
    }

    public void setBaseDirection(Vector2 baseDirection) {
        this.baseDirection = baseDirection;
    }

    public void stopEmit() {
        canEmit = false;
    }
}
