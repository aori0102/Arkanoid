package game.Rank;

import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;
import utils.Vector2;

public final class RankUI extends MonoBehaviour {

    private static final double FILL_RATE = 4.889;

    private static final double RANK_POP_UP_SIZE = 1.34;
    private static final double RANK_UP_DISPLAY_TIME = 2.33;
    private static final double RANK_SHRINK_RATE = 8.923;
    private static final String RANK_PREFIX = "Rank ";

    private SpriteRenderer fillRenderer = null;
    private SpriteRenderer rankUpRenderer = null;
    private TextUI rankText = null;

    private double fillRatio = 0.0;
    private double targetFillRatio = 0.0;

    private EventActionID rankManager_onExpChanged_ID = null;
    private EventActionID rankManager_onRankChanged_ID = null;

    private Time.CoroutineID hideRankUpImage_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public RankUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        rankUpRenderer.getGameObject().setActive(false);
    }

    @Override
    public void start() {
        rankManager_onExpChanged_ID = RankManager.getInstance().onExpChanged
                .addListener(this::rankManager_onExpChanged);
        rankManager_onRankChanged_ID = RankManager.getInstance().onRankChanged
                .addListener(this::rankManager_onRankChanged);
    }

    @Override
    public void update() {
        updateFill();
        updateRankText();
    }

    @Override
    public void onDestroy() {
        if (RankManager.getInstance() != null) {
            RankManager.getInstance().onExpChanged
                    .removeListener(rankManager_onExpChanged_ID);
            RankManager.getInstance().onRankChanged
                    .removeListener(rankManager_onRankChanged_ID);
        }

        Time.removeCoroutine(hideRankUpImage_ID);
    }

    private void updateFill() {
        fillRatio = MathUtils.lerp(fillRatio, targetFillRatio, Time.getDeltaTime() * FILL_RATE);
        fillRenderer.setFillAmount(fillRatio);
    }

    private void updateRankText() {
        var scale = Vector2.lerp(rankText.getTransform().getLocalScale(), Vector2.one(), Time.getDeltaTime() * RANK_SHRINK_RATE);
        rankText.getTransform().setLocalScale(scale);
    }

    /**
     * Called when {@link RankManager#onRankChanged} is invoked.<br><br>
     * This function updates and display rank up UI.
     *
     * @param sender Event caller {@link RankManager}.
     * @param e      Event argument indicating the new rank.
     */
    private void rankManager_onRankChanged(Object sender, Integer e) {
        rankText.setText(RANK_PREFIX + e);
        rankText.getTransform().setLocalScale(Vector2.one().multiply(RANK_POP_UP_SIZE));

        rankUpRenderer.getGameObject().setActive(true);
        if (hideRankUpImage_ID != null) {
            Time.removeCoroutine(hideRankUpImage_ID);
        }
        hideRankUpImage_ID = Time.addCoroutine(this::hideRankUpImage, Time.getTime() + RANK_UP_DISPLAY_TIME);
    }

    /**
     * Called when {@link RankManager#onExpChanged} is invoked.<br><br>
     * This function updates the experience bar to match with the current experience.
     *
     * @param sender Event caller {@link RankManager}.
     * @param e      Empty event argument.
     */
    private void rankManager_onExpChanged(Object sender, RankManager.OnExpChangedEventArgs e) {
        targetFillRatio = e.expRatio;
    }

    private void hideRankUpImage() {
        rankUpRenderer.getGameObject().setActive(false);
    }

    /**
     * Link fill bar renderer.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link RankUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param fillRenderer Fill bar to be linked.
     */
    public void linkFillRenderer(SpriteRenderer fillRenderer) {
        this.fillRenderer = fillRenderer;
    }

    /**
     * Link rank text.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link RankUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param rankText The rank text to link.
     */
    public void linkRankText(TextUI rankText) {
        this.rankText = rankText;
    }

    /**
     * Link the rank up icon.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link RankUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param rankUpRenderer The rank up icon to link.
     */
    public void linkRankUpRenderer(SpriteRenderer rankUpRenderer) {
        this.rankUpRenderer = rankUpRenderer;
    }

}