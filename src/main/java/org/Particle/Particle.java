package org.Particle;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.Time;
import utils.Vector2;

public class Particle extends MonoBehaviour {

    public Vector2 direction;
    public double speed;
    public double lifeTime;
    private double lifeTimer = 0;

    private ParticleType type;

    public Particle(GameObject owner) {
        super(owner);
        setParticleType(ParticleType.Fire);
    }

    public void assignSpecs(double speed, double lifeTime) {
        this.speed = speed;
        this.lifeTime = lifeTime;
    }

    @Override
    public void update() {
        handleMovement(direction, speed);
    }

    public void handleMovement(Vector2 direction, double speed) {
        getTransform().translate(direction.normalize().multiply(speed * Time.getDeltaTime()));

        lifeTimer += Time.getDeltaTime();
        if (lifeTimer >= lifeTime) {
            gameObject.setActive(false);
            lifeTimer = 0;
            ParticlePool.getInstance().releaseParticle(this);
        }
    }

    public void setParticleType(ParticleType type) {
        this.type = type;
    }

    public ParticleType getParticleType() {
        return type;
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }
}
