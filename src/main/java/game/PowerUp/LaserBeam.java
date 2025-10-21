package game.PowerUp;

import game.PowerUp.Index.PowerUp;
import game.PowerUp.Index.PowerUpIndex;
import org.GameObject.GameObject;

public class LaserBeam extends PowerUp {
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public LaserBeam(GameObject owner) {
        super(owner);
    }

    public void awake() {
        setPowerUpIndex(PowerUpIndex.LaserBeam);
    }

    public void start() {

    }
}
