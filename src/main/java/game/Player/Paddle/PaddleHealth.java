package game.Player.Paddle;

import game.Entity.EntityHealth;
import game.Entity.EntityHealthAlterType;
import game.Entity.EntityStat;
import game.Player.PlayerData.DataManager;
import game.Player.PlayerData.IPlayerProgressHolder;
import org.GameObject.GameObject;

public final class PaddleHealth extends EntityHealth implements
        IPlayerProgressHolder {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PaddleHealth(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        loadProgress();
        getComponent(PlayerStat.class).onStatChanged.addListener(this::paddleStat_onStatChanged);
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return PlayerStat.class;
    }

    @Override
    public void loadProgress() {
        var health = DataManager.getInstance().getProgress().getHealth();
        System.out.println("Loaded health " + health);
        alterHealth(EntityHealthAlterType.Regeneration, null, health - getHealth());
    }

    /**
     * Called when {@link PlayerStat#onStatChanged} is invoked.<br><br>
     * This function resets the health when there's a change in max HP.
     *
     * @param sender Event caller {@link PlayerStat}.
     * @param e      Event argument indicating which stat was modified.
     */
    private void paddleStat_onStatChanged(Object sender, PlayerStat.PlayerStatIndex e) {
        if (e == PlayerStat.PlayerStatIndex.MaxHealth) {
            resetHealth();
        }
    }

}