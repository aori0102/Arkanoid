package game.Player.PlayerSkills.Skills;

import game.GameManager.LevelState;
import game.Level.LevelManager;
import game.Player.Paddle.PlayerStat;
import game.Player.Player;
import game.Player.PlayerSkills.SkillIndex;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Base class representing a player skill.
 * <p>
 * Handles:
 * - Skill charge accumulation
 * - Cooldown management
 * - Skill usage and events
 * </p>
 */
public abstract class Skill extends MonoBehaviour {

    /**
     * Current cooldown time remaining before a new charge can be obtained.
     * Read-only. Use {@link #setSkillCooldownTime(double)} internally.
     */
    private double _skillCooldownTime = 0.0;

    /**
     * Current available skill charges.
     * Read-only. Use {@link #setSkillCharge(int)} internally.
     */
    private int _skillCharge = 0;

    private PlayerStat playerStat = null;

    /** Event invoked whenever the skill charging ratio changes. */
    public EventHandler<Void> onChargingRatioChanged = new EventHandler<>(Skill.class);

    /** Event invoked whenever the charge amount changes. */
    public EventHandler<Void> onChargeAmountChanged = new EventHandler<>(Skill.class);

    /** Event invoked when a new charge is obtained. */
    public EventHandler<Void> onNewChargeObtained = new EventHandler<>(Skill.class);

    /** Event invoked when all charges reach zero. */
    public EventHandler<Void> onChargeReachesZero = new EventHandler<>(Skill.class);

    /** Event invoked when a skill is used. */
    public EventHandler<Void> onSkillUsed = new EventHandler<>(Skill.class);

    /**
     * Create this Skill component.
     *
     * @param owner The GameObject that owns this skill (usually the player).
     */
    public Skill(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        playerStat = Player.getInstance().getPlayerPaddle().getPaddleStat();

        // Initialize skill charge and cooldown using skill properties
        setSkillCharge(getSkillIndex().maxSkillCharge);
        setSkillCooldownTime(playerStat.getSkillCooldown(getSkillIndex()));
    }

    @Override
    public void update() {
        // Reduce cooldown if skill is not at max charge
        if (!maxCharge()) {
            reduceCooldown(Time.getDeltaTime());
        }
    }

    /** Adds one skill charge and fires the onNewChargeObtained event. */
    private void addSkillCharge() {
        setSkillCharge(_skillCharge + 1);
        onNewChargeObtained.invoke(this, null);
    }

    /** Reduces cooldown by a given amount and grants a new charge if cooldown reaches zero. */
    private void reduceCooldown(double amount) {
        setSkillCooldownTime(_skillCooldownTime - amount);
        if (_skillCooldownTime <= 0) {
            addSkillCharge();
            resetCooldown();
        }
    }

    /**
     * Attempts to use the skill.
     * <p>
     * - Reduces skill charge by 1 if available
     * - Invokes the skill effect
     * - Fires onChargeReachesZero if charges drop to zero
     */
    public void useSkill() {

        if (LevelManager.getInstance().getLevelState() != LevelState.Playing) {
            return;
        }

        if (_skillCharge > 0) {
            setSkillCharge(_skillCharge - 1);
            invoke();
            onSkillUsed.invoke(this, null);
            if (_skillCharge == 0) {
                onChargeReachesZero.invoke(this, null);
            }
        }

    }

    /** Resets the cooldown to the base cooldown of the skill. */
    private void resetCooldown() {
        _skillCooldownTime = playerStat.getSkillCooldown(getSkillIndex());
    }

    /** Setter for read-only _skillCharge */
    private void setSkillCharge(int skillCharge) {
        this._skillCharge = skillCharge;
        onChargeAmountChanged.invoke(this, null);
    }

    /** Setter for read-only _skillCooldownTime */
    private void setSkillCooldownTime(double skillCooldownTime) {
        this._skillCooldownTime = skillCooldownTime;
        onChargingRatioChanged.invoke(this, null);
    }

    /** Returns the current number of available charges. */
    public int getSkillCharge() {
        return _skillCharge;
    }

    /** Returns the charging ratio from 0.0 to 1.0. */
    public double getChargingRatio() {
        var base = playerStat.getSkillCooldown(getSkillIndex());
        return maxCharge()
                ? 1.0
                : (base - _skillCooldownTime) / base;
    }

    /** Checks if the skill has at least one charge available. */
    public boolean hasCharge() {
        return _skillCharge > 0;
    }

    /** Checks if the skill is at maximum charge. */
    public boolean maxCharge() {
        return _skillCharge == getSkillIndex().maxSkillCharge;
    }

    /**
     * Abstract method to define the actual effect of the skill when used.
     * Subclasses must implement this to perform the skill action.
     */
    protected abstract void invoke();

    /**
     * Abstract method to provide the SkillIndex associated with this skill.
     * Subclasses must implement to return their corresponding enum value.
     */
    protected abstract SkillIndex getSkillIndex();

}
