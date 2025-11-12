package org.ParticleSystem.Emitter;

import org.GameObject.GameObject;
import org.ParticleSystem.ParticleObject;
import utils.Random;
import utils.Vector2;

/**
 * Emit the particles around the object
 */
public class CircleEmitter extends ParticleEmitter {

    private double radius;

    public CircleEmitter(GameObject owner) {
        super(owner);
        setEmitType(EmitTypes.Circle);
    }

    @Override
    public void update() {
        if (!canEmit) return;
        super.update();
        if (isEmitting) {
            emit();
        }
    }


    /**
     * Emit the particles around the object
     * <p>
     *     Firstly, generate a spawn position. <br>
     *     Then calculate the direction by take randomly a direction in a circle
     * </p>
     */
    @Override
    public void emit() {
        Vector2 spawnPosition = generateSpawnPosition();

        double angle = Random.range(0.0, 2 * Math.PI);
        Vector2 direction = new Vector2(Math.cos(angle), Math.sin(angle)).normalize();

        ParticleObject particle = spawnParticles(particleType);
        particle.getTransform().setGlobalPosition(spawnPosition);
        particle.setParticleType(particleType);
        particle.setDirection(direction);
    }

    /**
     * Generate a spawn position in a circle
     * @return the spawn position
     */
    @Override
    protected Vector2 generateSpawnPosition() {
        Vector2 center = getTransform().getGlobalPosition();

        double angle = Random.range(0.0, 2 * Math.PI);
        double r = Math.sqrt(Random.range(0.1, 1.0)) * radius;

        double x = center.x + Math.cos(angle) * r;
        double y = center.y + Math.sin(angle) * r;

        return new Vector2(x, y);
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }
}
