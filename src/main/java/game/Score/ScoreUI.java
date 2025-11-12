package game.Score;

import game.Level.LevelManager;
import org.Annotation.LinkViaPrefab;
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
    private static final String LEVEL_PREFIX = "Level ";

    private EventActionID scoreManager_onComboChanged_ID = null;
    private EventActionID scoreManager_onScoreChanged_ID = null;
    private EventActionID levelManager_onLevelLoaded_ID = null;

    @LinkViaPrefab
    private TextUI comboText = null;

    @LinkViaPrefab
    private TextUI scoreText = null;

    @LinkViaPrefab
    private TextUI levelText = null;

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
        levelManager_onLevelLoaded_ID = LevelManager.onAnyLevelLoaded
                .addListener(this::levelManager_onLevelLoaded);

        updateScoreText(ScoreManager.getInstance().getScore());
        updateComboText(ScoreManager.getInstance().getCombo());
    }

    @Override
    public void onDestroy() {
        if (ScoreManager.getInstance() != null) {
            ScoreManager.getInstance().onComboChanged
                    .removeListener(scoreManager_onComboChanged_ID);
            ScoreManager.getInstance().onScoreChanged
                    .removeListener(scoreManager_onScoreChanged_ID);
        }
        LevelManager.onAnyLevelLoaded.removeListener(levelManager_onLevelLoaded_ID);
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
        updateScoreText(e);
    }

    /**
     * Called when {@link ScoreManager#onComboChanged} is invoked.<br><br>
     * This function updates combo as combo changes, including pop up.
     *
     * @param sender Event caller {@link ScoreManager}.
     * @param e      Empty event argument.
     */
    private void scoreManager_onComboChanged(Object sender, Integer e) {
        if (comboText.getGameObject().isDestroyed()) {
            return;
        }
        updateComboText(e);
    }

    /**
     * Called when {@link LevelManager#onAnyLevelLoaded} is invoked.<br><br>
     * This function updates {@link #levelText} when a level is loaded.
     *
     * @param sender Event caller {@link LevelManager}.
     * @param e      Event argument containing information of the loaded level.
     */
    private void levelManager_onLevelLoaded(Object sender, LevelManager.OnLevelLoadedEventArgs e) {
        levelText.setText(LEVEL_PREFIX + (e.index + 1));
    }

    /**
     * Update the text corresponding to the combo.
     *
     * @param combo Combo.
     */
    private void updateComboText(int combo) {
        comboText.setText(COMBO_PREFIX + combo);
        if (combo > 0) {
            comboText.getTransform().setLocalScale(Vector2.one().multiply(COMBO_POP_UP_SIZE));
        }
    }

    /**
     * Update the text corresponding to the score.
     *
     * @param score Score.
     */
    private void updateScoreText(int score) {
        scoreText.setText(String.valueOf(score));
        var popUp = PrefabManager.instantiatePrefab(PrefabIndex.Scoreboard_ScoreUI_PopUp)
                .getComponent(ScorePopUp.class);
        popUp.setAmount(score);
        popUp.setEntryPoint(scoreText.getTransform().getGlobalPosition());
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

    /**
     * Link level text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link ScoreUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param levelText The level text.
     */
    public void linkLevelText(TextUI levelText) {
        this.levelText = levelText;
    }

}