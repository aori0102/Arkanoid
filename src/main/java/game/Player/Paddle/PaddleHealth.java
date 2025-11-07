package game.Player.Paddle;

import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.Event.EventHandler;
import org.GameObject.GameObject;

public final class PaddleHealth extends EntityHealth {

    public EventHandler<Void> onPaddleHealthReachesZero = new EventHandler<>(PaddleHealth.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleHealth(GameObject owner) {
        super(owner);
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return PaddleStat.class;
    }

}