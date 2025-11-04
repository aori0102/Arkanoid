package org.Particle.Emitter;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Particle.Particle;
import org.Particle.ParticlePool;
import org.Particle.ParticleType;
import utils.Random;
import utils.Time;
import utils.Vector2;

public abstract class ParticleEmitter extends MonoBehaviour {

    protected double emissionRate;
    protected double emissionTime;
    protected double spreadAngle;
    protected double minSpeed;
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

    public abstract void emit();

    protected abstract Vector2 generateSpawnPosition();

    protected Particle spawnParticles(ParticleType particleType) {
        double speed = Random.range(minSpeed, maxSpeed);
        double lifetime = Random.range(minLifeTime, maxLifeTime);

        Particle particle = ParticlePool.getInstance().getParticle(particleType);

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
        System.out.println("This is set");
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
