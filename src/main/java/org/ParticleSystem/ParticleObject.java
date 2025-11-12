package org.ParticleSystem;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import utils.MathUtils;
import utils.Time;
import utils.Vector2;

public class ParticleObject extends MonoBehaviour {

    public Vector2 direction;
    public double speed;
    public double lifeTime;
    private double lifeTimer = 0;
    private SpriteRenderer particleVisual;

    private ParticleType type;

    public ParticleObject(GameObject owner) {
        super(owner);
    }

    public void awake() {
        particleVisual = getComponent(SpriteRenderer.class);
    }

    /**
     * Assigning particle specs.
     * @param speed : travel speed
     * @param lifeTime : existing time
     */
    public void assignSpecs(double speed, double lifeTime) {
        this.speed = speed;
        this.lifeTime = lifeTime;
    }

    @Override
    public void update() {
        handleMovement(direction, speed);
    }

    /**
     * Handle the movement of the particle.
     * <p>
     *     Moving on a specific direction.<p></p>
     *     Fading over the lifetime.
     * </p>
     * @param direction
     * @param speed
     */
    public void handleMovement(Vector2 direction, double speed) {
        getTransform().translate(direction.normalize().multiply(speed * Time.getDeltaTime()));

        lifeTimer += Time.getDeltaTime();
        double t = lifeTimer / lifeTime;
        double opacity = MathUtils.lerp(1, 0, t);
        particleVisual.setOpacity(opacity);
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
