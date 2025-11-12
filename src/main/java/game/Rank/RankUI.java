package game.Rank;

import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;
import utils.Vector2;

/**
 * UI class that handles rank UI components on events called from {@link RankManager}.
 */
public final class RankUI extends MonoBehaviour {

    private static final double FILL_RATE = 4.889;

    private static final double RANK_POP_UP_SIZE = 1.34;
    private static final double RANK_SHRINK_RATE = 8.923;
    private static final String RANK_PREFIX = "Rank ";

    @LinkViaPrefab
    private SpriteRenderer fillRenderer = null;

    @LinkViaPrefab
    private SpriteRenderer rankUpRenderer = null;

    @LinkViaPrefab
    private TextUI rankText = null;

    private double fillRatio = 0.0;
    private double targetFillRatio = 0.0;

    private EventActionID rankManager_onExpChanged_ID = null;
    private EventActionID rankManager_onRankChanged_ID = null;
    private EventActionID rankManager_onAccumulatedRankGained_ID = null;
    private EventActionID rankManager_onAccumulatedRankEmptied_ID = null;

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
        rankManager_onAccumulatedRankGained_ID = RankManager.getInstance().onAccumulatedRankGained
                .addListener(this::rankManager_onAccumulatedRankGained);
        rankManager_onAccumulatedRankEmptied_ID = RankManager.getInstance().onAccumulatedRankEmptied
                .addListener(this::rankManager_onAccumulatedRankEmptied);

        updateRankText(RankManager.getInstance().getRank());
    }

    @Override
    public void update() {
        updateFillOpacity();
        updateRankTextScaling();
    }

    @Override
    public void onDestroy() {
        if (RankManager.getInstance() != null) {
            RankManager.getInstance().onExpChanged
                    .removeListener(rankManager_onExpChanged_ID);
            RankManager.getInstance().onRankChanged
                    .removeListener(rankManager_onRankChanged_ID);
            RankManager.getInstance().onAccumulatedRankEmptied
                    .removeListener(rankManager_onAccumulatedRankGained_ID);
            RankManager.getInstance().onAccumulatedRankGained
                    .removeListener(rankManager_onAccumulatedRankEmptied_ID);
        }
    }

    /**
     * Update the filling of {@link #fillRenderer} (The EXP fill bar)
     */
    private void updateFillOpacity() {
        fillRatio = MathUtils.lerp(fillRatio, targetFillRatio, Time.getDeltaTime() * FILL_RATE);
        fillRenderer.setFillAmount(fillRatio);
    }

    /**
     * Update {@link #rankText} scaling, achieving an enlarge-then-shrink effect.
     */
    private void updateRankTextScaling() {
        var scale = Vector2.lerp(rankText.getTransform().getLocalScale(), Vector2.one(), Time.getDeltaTime() * RANK_SHRINK_RATE);
        rankText.getTransform().setLocalScale(scale);
    }

    private void updateRankText(int rank) {
        rankText.setText(RANK_PREFIX + rank);
        rankText.getTransform().setLocalScale(Vector2.one().multiply(RANK_POP_UP_SIZE));
    }

    /**
     * Called when {@link RankManager#onRankChanged} is invoked.<br><br>
     * This function updates and display rank up UI.
     *
     * @param sender Event caller {@link RankManager}.
     * @param e      Event argument indicating the new rank.
     */
    private void rankManager_onRankChanged(Object sender, Integer e) {
        updateRankText(e);
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

    /**
     * Called when {@link RankManager#onAccumulatedRankGained} is invoked.<br><br>
     * This function shows the rank up icon.
     *
     * @param sender Event caller {@link RankManager}.
     * @param e      Empty event argument.
     */
    private void rankManager_onAccumulatedRankGained(Object sender, Void e) {
        rankUpRenderer.getGameObject().setActive(true);
    }

    /**
     * Called when {@link RankManager#onAccumulatedRankEmptied} is invoked.<br><br>
     * This function hides the rank up icon.
     *
     * @param sender Event caller {@link RankManager}.
     * @param e      Empty event argument.
     */
    private void rankManager_onAccumulatedRankEmptied(Object sender, Void e) {
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