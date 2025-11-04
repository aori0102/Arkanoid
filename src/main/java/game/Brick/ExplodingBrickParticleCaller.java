package game.Brick;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.ParticleSystem.ParticleType;
import org.ParticleSystem.Particles.ExplodingBrickParticle;
import utils.Time;
import utils.Vector2;

public class ExplodingBrickParticleCaller extends MonoBehaviour {
    private static final double PARTICLE_EXISTING_TIME = 0.3;

    private static ExplodingBrickParticleCaller instance;

    private ExplodingBrickParticle explodingBrickParticle;

    private Time.CoroutineID explodingBrickCoroutineID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ExplodingBrickParticleCaller(GameObject owner) {
        super(owner);
        instance = this;
    }

    public static ExplodingBrickParticleCaller getInstance() {
        return instance;
    }

    public void awake() {
        explodingBrickParticle = GameObjectManager.instantiate("ExplodingBrickParticle")
                .addComponent(ExplodingBrickParticle.class);
        explodingBrickParticle.setRadius(10);
        explodingBrickParticle.setParticleType(ParticleType.ExplodingBrick);
    }

    public void startEmit(Vector2 position) {
        explodingBrickParticle.setPosition(position);
        explodingBrickParticle.startEmit();
        explodingBrickCoroutineID = Time.addCoroutine(this::stopEmit, Time.getTime() + PARTICLE_EXISTING_TIME);
    }

    public void stopEmit() {
        explodingBrickParticle.stopEmit();
        Time.removeCoroutine(explodingBrickCoroutineID);
    }

}
