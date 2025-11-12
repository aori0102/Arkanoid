package game.Player;

import game.Player.Paddle.PlayerStat;
import game.Player.Prefab.PlayerStatUIHandlerPrefab;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class PlayerStatUIHandler extends MonoBehaviour {

    private static PlayerStatUIHandler instance = null;

    @LinkViaPrefab
    private PlayerStatUI movementSpeedStatUI = null;

    @LinkViaPrefab
    private PlayerStatUI defenseStatUI = null;

    @LinkViaPrefab
    private PlayerStatUI attackStatUI = null;

    @LinkViaPrefab
    private PlayerStatUI maxHealthStatUI = null;

    private EventActionID paddleStat_onStatChanged_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerStatUIHandler(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("PlayerStatUIHandler is a singleton");
        }
        instance = this;
    }

    @Override
    protected void onDestroy() {
        try {
            Player.getInstance().getPlayerPaddle().getPaddleStat().onStatChanged
                    .removeListener(paddleStat_onStatChanged_ID);
        } catch (NullPointerException _) {
        }

        instance = null;
    }

    @Override
    public void start() {
        paddleStat_onStatChanged_ID = Player.getInstance().getPlayerPaddle().getPaddleStat().onStatChanged
                .addListener(this::paddleStat_onStatChanged);
        updateStatUI();
    }

    /**
     * Called when {@link PlayerStat#onStatChanged} is invoked.<br><br>
     * This function updates the stat UI when stat changes.
     *
     * @param sender Event caller {@link PlayerStat}.
     * @param e      Event argument .
     */
    private void paddleStat_onStatChanged(Object sender, PlayerStat.PlayerStatIndex e) {
        updateStatUI();
    }

    /**
     * Update all {@link PlayerStatUI} based on {@link PlayerStat}.
     */
    private void updateStatUI() {
        var paddleStat = Player.getInstance().getPlayerPaddle().getPaddleStat();
        movementSpeedStatUI.setAmountText(
                paddleStat.getActualMovementSpeed(),
                paddleStat.getMovementSpeedMultiplier()
        );
        attackStatUI.setAmountText(
                paddleStat.getActualAttack(),
                paddleStat.getAttackMultiplier()
        );
        defenseStatUI.setAmountText(
                paddleStat.getActualDefense(),
                paddleStat.getDefenceMultiplier()
        );
        maxHealthStatUI.setAmountText(
                paddleStat.getActualMaxHealth(),
                paddleStat.getMaxHealthMultiplier()
        );
    }

    /**
     * Link max health stat UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIHandlerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param maxHealthStatUI Max health stat UI.
     */
    public void linkMaxHealthStatUI(PlayerStatUI maxHealthStatUI) {
        this.maxHealthStatUI = maxHealthStatUI;
    }

    /**
     * Link attack stat UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIHandlerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param attackStatUI Attack stat UI.
     */
    public void linkAttackStatUI(PlayerStatUI attackStatUI) {
        this.attackStatUI = attackStatUI;
    }

    /**
     * Link defense stat UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIHandlerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param defenseStatUI Defense start UI.
     */
    public void linkDefenseStatUI(PlayerStatUI defenseStatUI) {
        this.defenseStatUI = defenseStatUI;
    }

    /**
     * Link movement speed stat UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIHandlerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param movementSpeedStatUI Movement speed stat UI.
     */
    public void linkMovementSpeedStat(PlayerStatUI movementSpeedStatUI) {
        this.movementSpeedStatUI = movementSpeedStatUI;
    }

}