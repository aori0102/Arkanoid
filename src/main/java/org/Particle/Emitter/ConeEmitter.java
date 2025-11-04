package org.Particle.Emitter;

import org.GameObject.GameObject;
import org.Particle.Particle;
import org.Particle.ParticleType;
import utils.Random;
import utils.Vector2;

public class ConeEmitter extends ParticleEmitter {


    public ConeEmitter(GameObject owner) {
        super(owner);
        setEmitType(EmitTypes.Cone);
    }

    public void awake() {
        System.out.println(particleType);
    }

    @Override
    public void update() {
        if (!canEmit) {
            return;
        }
        super.update();
        if (isEmitting) {
            emit();
        }
    }

    @Override
    public void emit() {

        Vector2 spawnPos = generateSpawnPosition();

        double halfAngle = Math.toRadians(spreadAngle / 2.0);
        double offsetAngle = Random.range(-halfAngle, halfAngle);

        Vector2 direction = Vector2.rotate(baseDirection, offsetAngle).normalize();

        Particle particle = spawnParticles(particleType);
        particle.getTransform().setGlobalPosition(spawnPos);
        particle.setParticleType(particleType);
        particle.setDirection(direction);
    }

    @Override
    protected Vector2 generateSpawnPosition() {
        Vector2 center = getTransform().getGlobalPosition();
        double offsetX = Random.range(-1.0, 1.0);
        double offsetY = Random.range(-1.0, 1.0);
        return center.add(new Vector2(offsetX, offsetY));
    }

}
