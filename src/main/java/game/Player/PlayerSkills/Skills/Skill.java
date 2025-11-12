package game.Player.PlayerSkills.Skills;

import game.Player.Paddle.PaddleStat;
import game.Player.Player;
import game.Player.PlayerSkills.SkillIndex;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

public abstract class Skill extends MonoBehaviour {

    /**
     * <b>Read-only. Write via {@link #setSkillCooldownTime}.</b>
     */
    private double _skillCooldownTime = 0.0;

    /**
     * <b>Read-only. Write via {@link #setSkillCharge}.</b>
     */
    private int _skillCharge = 0;

    private PaddleStat paddleStat = null;

    public EventHandler<Void> onChargingRatioChanged = new EventHandler<>(Skill.class);
    public EventHandler<Void> onChargeAmountChanged = new EventHandler<>(Skill.class);
    public EventHandler<Void> onNewChargeObtained = new EventHandler<>(Skill.class);
    public EventHandler<Void> onChargeReachesZero = new EventHandler<>(Skill.class);
    public EventHandler<Void> onSkillUsed = new EventHandler<>(Skill.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Skill(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        paddleStat = Player.getInstance().getPlayerPaddle().getPaddleStat();

        setSkillCharge(getSkillIndex().maxSkillCharge);
        setSkillCooldownTime(paddleStat.getSkillCooldown(getSkillIndex()));
    }

    @Override
    public void update() {
        if (!maxCharge()) {
            reduceCooldown(Time.getDeltaTime());
        }
    }

    private void addSkillCharge() {
        setSkillCharge(_skillCharge + 1);
        onNewChargeObtained.invoke(this, null);
    }

    private void reduceCooldown(double amount) {
        setSkillCooldownTime(_skillCooldownTime - amount);
        if (_skillCooldownTime <= 0) {
            addSkillCharge();
            resetCooldown();
        }
    }

    public void useSkill() {
        if (_skillCharge > 0) {
            setSkillCharge(_skillCharge - 1);
            invoke();
            onSkillUsed.invoke(this, null);
            if (_skillCharge == 0) {
                onChargeReachesZero.invoke(this, null);
            }
        }
    }

    private void resetCooldown() {
        _skillCooldownTime = paddleStat.getSkillCooldown(getSkillIndex());
    }

    /**
     * Setter for read-only field {@link #_skillCharge}
     *
     * @param skillCharge The value to set.
     */
    private void setSkillCharge(int skillCharge) {
        this._skillCharge = skillCharge;
        onChargeAmountChanged.invoke(this, null);
    }

    /**
     * Setter for read-only field {@link #_skillCooldownTime}
     *
     * @param skillCooldownTime The value to set.
     */
    private void setSkillCooldownTime(double skillCooldownTime) {
        this._skillCooldownTime = skillCooldownTime;
        onChargingRatioChanged.invoke(this, null);
    }

    public int getSkillCharge() {
        return _skillCharge;
    }

    public double getChargingRatio() {
        var base = paddleStat.getSkillCooldown(getSkillIndex());
        return maxCharge()
                ? 1.0
                : (base - _skillCooldownTime) / base;
    }

    public boolean hasCharge() {
        return _skillCharge > 0;
    }

    public boolean maxCharge() {
        return _skillCharge == getSkillIndex().maxSkillCharge;
    }

    protected abstract void invoke();

    protected abstract SkillIndex getSkillIndex();

}
