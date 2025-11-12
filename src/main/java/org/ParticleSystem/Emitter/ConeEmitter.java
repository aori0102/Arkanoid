package org.ParticleSystem.Emitter;

import org.GameObject.GameObject;
import org.ParticleSystem.ParticleObject;
import utils.Random;
import utils.Vector2;

/**
 * Emit particle in a cone shape field
 */
public class ConeEmitter extends ParticleEmitter {

    public ConeEmitter(GameObject owner) {
        super(owner);
        setEmitType(EmitTypes.Cone);
    }

    public void awake() {
        System.out.println(particleType);
    }

    /**
     * Emit the particle in a cone shape field.
     * <p>
     *  First of all, a spawn position will be generated randomly<br>
     *  After that, the direction will be calculated by rotating the base direction
     *  by an offset angle (which is limited by the apex angle)
     * </p>
     */
    @Override
    public void emit() {

        Vector2 spawnPos = generateSpawnPosition();

        double halfAngle = Math.toRadians(spreadAngle / 2.0);
        double offsetAngle = Random.range(-halfAngle, halfAngle);

        Vector2 direction = Vector2.rotate(baseDirection, offsetAngle).normalize();

        ParticleObject particle = spawnParticles(particleType);
        particle.getTransform().setGlobalPosition(spawnPos);
        particle.setParticleType(particleType);
        particle.setDirection(direction);
    }

    /**
     * Generate a spawn position in cone field.
     * @return spawn position
     */
    @Override
    protected Vector2 generateSpawnPosition() {
        Vector2 center = getTransform().getGlobalPosition();
        double offsetX = Random.range(-1.0, 1.0);
        double offsetY = Random.range(-1.0, 1.0);
        return center.add(new Vector2(offsetX, offsetY));
    }

}
