package game.Player.Paddle;

import game.Entity.EntityHealth;
import game.Entity.EntityHealthAlterType;
import game.Entity.EntityStat;
import game.Player.PlayerAttributes;
import game.Player.PlayerData.DataManager;
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

    public void loadProgress() {
        var health = DataManager.getInstance().getProgress().getHealth();
        alterHealth(EntityHealthAlterType.NormalDamage, null, PlayerAttributes.MAX_HEALTH - health);
    }

}