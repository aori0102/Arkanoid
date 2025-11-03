package org.Particle.Emitter;

import org.GameObject.GameObject;
import org.Particle.Particle;
import org.Particle.ParticlesPrefab.ParticleType;
import utils.Random;
import utils.Vector2;

public class ConeEmitter extends ParticleEmitter {

    private double radius;

    public ConeEmitter(GameObject owner) {
        super(owner);
        setEmitType(EmitTypes.Cone);
    }

    @Override
    public void update() {
        emitCounter();
        if (isEmitting) {
            emit(ParticleType.Fire);
        }
    }

    @Override
    public void emit(ParticleType particleType) {
        Vector2 spawnPos = generateSpawnPosition();

        // Hướng cơ bản của cone — ví dụ hướng lên
        Vector2 baseDir = new Vector2(0, 1); // hoặc em set baseDir trong constructor

        // Lấy góc lệch ngẫu nhiên trong khoảng [-spread/2, +spread/2]
        double halfAngle = Math.toRadians(spreadAngle / 2.0);
        double offsetAngle = Random.range(-halfAngle, halfAngle);

        // Quay vector baseDir quanh góc offset
        Vector2 direction = baseDir.rotate(baseDir, offsetAngle).normalize();

        // Spawn particle
        Particle particle = spawnParticles(particleType);
        particle.getTransform().setGlobalPosition(spawnPos);
        particle.setDirection(direction);
    }



    @Override
    protected void handleDirection() { }

    @Override
    protected Vector2 generateSpawnPosition() {
        Vector2 center = getTransform().getGlobalPosition();
        double offsetX = Random.range(-1.0, 1.0);
        double offsetY = Random.range(-1.0, 1.0);
        return center.add(new Vector2(offsetX, offsetY));
    }


    public void setRadius(double radius) {
        this.radius = radius;
    }
}
