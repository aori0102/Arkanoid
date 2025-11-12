package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

public final class VoltraxisSkillHandler extends MonoBehaviour {

    private Time.CoroutineID enhanceSkillStartCoroutineID = null;
    private Time.CoroutineID enhanceSkillEndCoroutineID = null;

    public EventHandler<Void> onEnhanceSkillStarted = new EventHandler<>(VoltraxisSkillHandler.class);
    public EventHandler<Void> onEnhanceSkillEnded = new EventHandler<>(VoltraxisSkillHandler.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisSkillHandler(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {

        enhanceSkillStartCoroutineID = Time.addCoroutine(this::startEnhanceSkill, VoltraxisData.ENHANCE_SKILL_COOLDOWN);

        Voltraxis.getInstance().getVoltraxisGroggy().onGroggyReachedMax
                .addListener(this::voltraxisGroggy_onGroggyReachedMax);

    }

    @Override
    public void onDestroy() {
        Time.removeCoroutine(enhanceSkillStartCoroutineID);
        Time.removeCoroutine(enhanceSkillEndCoroutineID);
    }

    /**
     * Commence Voltraxis' enhance skill.
     */
    private void startEnhanceSkill() {

        // Add attack increment effect
        var effectInfo = new VoltraxisEffectManager.EffectInputInfo();
        effectInfo.index = VoltraxisData.EffectIndex.AttackIncrement;
        effectInfo.value = VoltraxisData.ENHANCE_ATTACK_INCREMENT;
        effectInfo.duration = VoltraxisData.ENHANCE_SKILL_DURATION;
        effectInfo.effectEndedCallback = null;
        Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(effectInfo);

        enhanceSkillStartCoroutineID = Time.addCoroutine(this::startEnhanceSkill, VoltraxisData.ENHANCE_SKILL_COOLDOWN);
        enhanceSkillEndCoroutineID = Time.addCoroutine(this::endEnhanceSkill, VoltraxisData.ENHANCE_SKILL_DURATION);
        onEnhanceSkillStarted.invoke(this, null);

    }

    /**
     * Handle Voltraxis' enhance skill's duration end.
     */
    private void endEnhanceSkill() {
        onEnhanceSkillEnded.invoke(this, null);
    }

    /**
     * Called when {@link VoltraxisGroggy#onGroggyReachedMax} is invoked.<br><br>
     * This function commences groggy skill as Voltraxis' groggy reaches max.
     *
     * @param sender Event caller {@link VoltraxisGroggy}.
     * @param e      Empty event argument.
     */
    private void voltraxisGroggy_onGroggyReachedMax(Object sender, Void e) {

        // Add attack increment effect
        var attackIncrementEffectInfo = new VoltraxisEffectManager.EffectInputInfo();
        attackIncrementEffectInfo.index = VoltraxisData.EffectIndex.AttackIncrement;
        attackIncrementEffectInfo.value = VoltraxisData.GROGGY_ATTACK_INCREMENT;
        attackIncrementEffectInfo.duration = VoltraxisData.GROGGY_DURATION;
        attackIncrementEffectInfo.effectEndedCallback = null;
        Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(attackIncrementEffectInfo);

        // Add skill cooldown reduction effect
        var skillCooldownEffectInfo = new VoltraxisEffectManager.EffectInputInfo();
        skillCooldownEffectInfo.index = VoltraxisData.EffectIndex.SkillCooldownDecrement;
        skillCooldownEffectInfo.value = VoltraxisData.GROGGY_BASIC_COOLDOWN_REDUCTION;
        skillCooldownEffectInfo.duration = VoltraxisData.GROGGY_DURATION;
        skillCooldownEffectInfo.effectEndedCallback = null;
        Voltraxis.getInstance().getVoltraxisEffectManager().addEffect(skillCooldownEffectInfo);

    }

}