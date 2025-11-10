package game.Score;

import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Text.TextUI;
import utils.Time;
import utils.Vector2;

public final class ScoreUI extends MonoBehaviour {

    private static final double COMBO_SHRINK_RATE = 8.242;
    private static final double COMBO_POP_UP_SIZE = 2.3;
    private static final String COMBO_PREFIX = "x";

    private EventActionID scoreManager_onComboChanged_ID = null;
    private EventActionID scoreManager_onScoreChanged_ID = null;

    private TextUI comboText = null;
    private TextUI scoreText = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ScoreUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        System.out.println("Starting ScoreUI");
        scoreManager_onScoreChanged_ID = ScoreManager.getInstance().onScoreChanged
                .addListener(this::scoreManager_onScoreChanged);
        scoreManager_onComboChanged_ID = ScoreManager.getInstance().onComboChanged
                .addListener(this::scoreManager_onComboChanged);
    }

    @Override
    public void onDestroy() {
        if (ScoreManager.getInstance() != null) {
            ScoreManager.getInstance().onComboChanged
                    .removeListener(scoreManager_onComboChanged_ID);
            ScoreManager.getInstance().onScoreChanged
                    .removeListener(scoreManager_onScoreChanged_ID);
        }
    }

    @Override
    public void update() {
        var scale = Vector2.lerp(comboText.getTransform().getLocalScale(), Vector2.one(), Time.getDeltaTime() * COMBO_SHRINK_RATE);
        comboText.getTransform().setLocalScale(scale);
    }

    /**
     * Called when {@link ScoreManager#onScoreChanged} is invoked.<br><br>
     * This function updates the UI as score changes.
     *
     * @param sender Event caller {@link ScoreManager}.
     * @param e      Event argument indicating the new score.
     */
    private void scoreManager_onScoreChanged(Object sender, Integer e) {
        scoreText.setText(String.valueOf(e));
        var popUp = PrefabManager.instantiatePrefab(PrefabIndex.Scoreboard_ScoreUI_PopUp)
                .getComponent(ScorePopUp.class);
        popUp.setAmount(e);
        popUp.setEntryPoint(scoreText.getTransform().getGlobalPosition());
    }

    /**
     * Called when {@link ScoreManager#onComboChanged} is invoked.<br><br>
     * This function updates combo as combo changes, including pop up.
     *
     * @param sender Event caller {@link ScoreManager}.
     * @param e      Empty event argument.
     */
    private void scoreManager_onComboChanged(Object sender, Integer e) {
        if (e > 0) {
            comboText.getTransform().setLocalScale(Vector2.one().multiply(COMBO_POP_UP_SIZE));
        }
        updateComboText(e);
    }

    private void updateComboText(int combo) {
        comboText.setText(COMBO_PREFIX + combo);
    }

    /**
     * Link combo text.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link ScoreManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param comboText The combo text to be linked.
     */
    public void linkComboText(TextUI comboText) {
        this.comboText = comboText;
    }

    /**
     * Link scoreboard's score text to this manager.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link ScoreManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param scoreText The score text to link.
     */
    public void linkScoreText(TextUI scoreText) {
        this.scoreText = scoreText;
    }

}