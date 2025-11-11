package game.PlayerSkills;

import game.Player.Player;
import game.PlayerSkills.Skills.Skill;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;
import utils.Vector2;

public final class SkillUI extends MonoBehaviour {

    private static final double CHARGING_NOB_OFFSET_RADIUS = 32.0;
    private static final double READY_OVERLAY_FADE_RATE = 9.332;
    private static final double ICON_SHRINKING_RATE = 5.119;
    private static final Vector2 ICON_POP_UP_SIZE = Vector2.one().multiply(1.4);
    private static final Vector2 SKILL_ICON_SIZE = new Vector2(64.0, 64.0);

    @LinkViaPrefab
    private SpriteRenderer readyOverlay = null;

    @LinkViaPrefab
    private SpriteRenderer notReadyOverlay = null;

    @LinkViaPrefab
    private TextUI chargeLabel = null;

    @LinkViaPrefab
    private SpriteRenderer chargingNob = null;

    @LinkViaPrefab
    private SpriteRenderer chargingRing = null;

    @LinkViaPrefab
    private SpriteRenderer skillIcon = null;

    @LinkViaPrefab
    private TextUI keybindLabel = null;

    private SkillIndex skillIndex = null;
    private Skill skill = null;
    private double readyOverlayOpacity = 0.0;

    private EventActionID skillData_onChargingRatioChanged_ID = null;
    private EventActionID skillData_onChargeAmountChanged_ID = null;
    private EventActionID skillData_onChargeReachesZero_ID = null;
    private EventActionID skillData_onNewChargeObtained_ID = null;
    private EventActionID skill_onSkillUsed_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public SkillUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        skill = Player.getInstance().getPlayerSkillsHandler()
                .getSkillData(skillIndex);
        skillData_onChargingRatioChanged_ID = skill.onChargingRatioChanged
                .addListener(this::skillData_onChargingRatioChanged);
        skillData_onChargeAmountChanged_ID = skill.onChargeAmountChanged
                .addListener(this::skillData_onChargeAmountChanged);
        skillData_onChargeReachesZero_ID = skill.onChargeReachesZero
                .addListener(this::skillData_onChargeReachesZero);
        skillData_onNewChargeObtained_ID = skill.onNewChargeObtained
                .addListener(this::skillData_onNewChargeObtained);
        skill_onSkillUsed_ID = skill.onSkillUsed
                .addListener(this::skill_onSkillUsed);
        presetSkillData();
    }

    @Override
    public void update() {
        readyOverlayOpacity = MathUtils.lerp(readyOverlayOpacity, 0.0, Time.getDeltaTime() * READY_OVERLAY_FADE_RATE);
        readyOverlay.setOpacity(readyOverlayOpacity);

        skillIcon.getTransform().setGlobalScale(
                Vector2.lerp(
                        skillIcon.getTransform().getGlobalScale(),
                        Vector2.one(),
                        Time.getDeltaTime() * ICON_SHRINKING_RATE
                )
        );
    }

    @Override
    public void onDestroy() {
        skill.onNewChargeObtained.removeListener(skillData_onNewChargeObtained_ID);
        skill.onChargeReachesZero.removeListener(skillData_onChargeReachesZero_ID);
        skill.onChargingRatioChanged.removeListener(skillData_onChargingRatioChanged_ID);
        skill.onChargeAmountChanged.removeListener(skillData_onChargeAmountChanged_ID);
        skill.onSkillUsed.removeListener(skill_onSkillUsed_ID);
    }

    private void presetSkillData() {
        chargeLabel.setText(String.valueOf(skill.getSkillCharge()));
        chargingRing.setFillAmount(skill.getChargingRatio());
        chargingNob.getGameObject().setActive(!skill.maxCharge());
        notReadyOverlay.getGameObject().setActive(!skill.hasCharge());
        readyOverlay.setOpacity(readyOverlayOpacity);
    }

    /**
     * Called when {@link Skill#onChargingRatioChanged} is invoked.<br><br>
     * This function updates {@link #chargingRing} and {@link #chargingNob} to match with the new ratio.
     *
     * @param sender Event caller {@link Skill}.
     * @param e      Empty event argument.
     */
    private void skillData_onChargingRatioChanged(Object sender, Void e) {
        var ratio = skill.getChargingRatio();
        chargingRing.setFillAmount(ratio);
        chargingNob.getGameObject().setActive(true);
        chargingNob.getTransform().setLocalPosition(new Vector2(
                Vector2.up().rotateBy(2.0 * Math.PI * (1.0 - ratio))
        ).multiply(CHARGING_NOB_OFFSET_RADIUS));
    }

    /**
     * Called when {@link Skill#onChargeAmountChanged} is invoked.<br><br>
     * This function changes {@link #chargeLabel}.
     *
     * @param sender Event caller {@link Skill}
     * @param e      Empty event argument.
     */
    private void skillData_onChargeAmountChanged(Object sender, Void e) {
        chargeLabel.setText(String.valueOf(skill.getSkillCharge()));
        notReadyOverlay.getGameObject().setActive(!skill.hasCharge());
    }

    /**
     * Called when {@link Skill#onChargeReachesZero} is invoked.<br><br>
     * This function shows a black dim overlay as there is no more skill charge.
     *
     * @param sender Event caller {@link Skill}.
     * @param e      Empty event argument
     */
    private void skillData_onChargeReachesZero(Object sender, Void e) {
        notReadyOverlay.getGameObject().setActive(true);
        readyOverlay.getGameObject().setActive(false);
    }

    /**
     * Called when {@link Skill#onNewChargeObtained} is invoked.<br><br>
     * This function displays a bright overlay when a new charge is obtained.
     *
     * @param sender Event caller {@link Skill}.
     * @param e      Empty event argument.
     */
    private void skillData_onNewChargeObtained(Object sender, Void e) {
        chargingNob.getGameObject().setActive(false);

        readyOverlay.getGameObject().setActive(true);
        readyOverlayOpacity = 1.0;
    }

    /**
     * Called when {@link Skill#onSkillUsed} is invoked.<br><br>
     * This function does a little pop-up on the icon when the skill is used.
     *
     * @param sender Event caller {@link Skill}.
     * @param e      Empty event argument.
     */
    private void skill_onSkillUsed(Object sender, Void e) {
        skillIcon.getTransform().setGlobalScale(ICON_POP_UP_SIZE);
    }

    /**
     * Link the overlay that displays when skill is charged.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param readyOverlay Renderer of the ready overlay.
     */
    public void linkReadyOverlay(SpriteRenderer readyOverlay) {
        this.readyOverlay = readyOverlay;
    }

    /**
     * Link the overlay that displays when a skill has no charge.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param notReadyOverlay Renderer of not ready overlay.
     */
    public void linkNotReadyOverlay(SpriteRenderer notReadyOverlay) {
        this.notReadyOverlay = notReadyOverlay;
    }

    /**
     * Link the text that represents the number of the skill's charge.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param chargeLabel Text of skill charge.
     */
    public void linkChargeLabel(TextUI chargeLabel) {
        this.chargeLabel = chargeLabel;
    }

    /**
     * Link the nob that follows the charging ring.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param chargingNob Renderer of the charging nob.
     */
    public void linkChargingNob(SpriteRenderer chargingNob) {
        this.chargingNob = chargingNob;
    }

    /**
     * Link the charging ring.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param chargingRing Renderer of the charging ring.
     */
    public void linkChargingRing(SpriteRenderer chargingRing) {
        this.chargingRing = chargingRing;
    }

    /**
     * Link the skill icon.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param skillIcon Renderer of the skill icon.
     */
    public void linkSkillIcon(SpriteRenderer skillIcon) {
        this.skillIcon = skillIcon;
    }

    /**
     * Link keybind text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link SkillUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param keybindLabel The text of the keybind tooltip.
     */
    public void linkKeybindLabel(TextUI keybindLabel) {
        this.keybindLabel = keybindLabel;
    }

    public void setSkillIndex(SkillIndex skillIndex) {
        this.skillIndex = skillIndex;
        skillIcon.setImage(skillIndex.skillIcon.getImage());
        skillIcon.setSize(SKILL_ICON_SIZE);
        keybindLabel.setText(skillIndex.keybind);
    }

}