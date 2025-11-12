package game.Brick;

import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import game.Particle.ExplodingBrick.ExplodingBrickParticle;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Time;
import utils.Vector2;

/**
 * Handles particle when a {@link Brick} is destroyed.
 */
public class ExplodingBrickParticleManager extends MonoBehaviour {

    private static final double PARTICLE_EXISTING_TIME = 0.3;

    private static ExplodingBrickParticleManager instance = null;

    private ExplodingBrickParticle explodingBrickParticle;

    private Time.CoroutineID explodingBrickCoroutineID = null;

    private EventActionID brick_onAnyBrickDestroyed_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ExplodingBrickParticleManager(GameObject owner) {
        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("ExplodingBrickParticleManager is a singleton");
        }
        instance = this;
    }

    public static ExplodingBrickParticleManager getInstance() {
        return instance;
    }

    @Override
    public void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
        Time.removeCoroutine(explodingBrickCoroutineID);

        instance = null;
    }

    @Override
    public void awake() {
        explodingBrickParticle = PrefabManager.instantiatePrefab(PrefabIndex.ExplodingBrickParticle)
                .getComponent(ExplodingBrickParticle.class);
        brick_onAnyBrickDestroyed_ID = Brick.onAnyBrickDestroyed
                .addListener(this::brick_onAnyBrickDestroyed);
    }

    /**
     * Called when {@link Brick#onAnyBrickDestroyed} is invoked.<br><br>
     * This function emits explosive particle when a brick is destroyed.
     *
     * @param sender Event caller {@link Brick}.
     * @param e      Event argument containing data on the brick destroyed.
     */
    private void brick_onAnyBrickDestroyed(Object sender, Brick.OnBrickDestroyedEventArgs e) {
        startEmit(e.brickPosition);
    }

    /**
     * Starts emitting particle on the destroyed brick.
     *
     * @param position The position where the brick was destroyed.
     */
    public void startEmit(Vector2 position) {
        explodingBrickParticle.setPosition(position);
        explodingBrickParticle.startEmit();
        explodingBrickCoroutineID = Time.addCoroutine(this::stopEmit, PARTICLE_EXISTING_TIME);
    }

    /**
     * Stop emitting particle.
     */
    public void stopEmit() {
        explodingBrickParticle.stopEmit();
    }

}
