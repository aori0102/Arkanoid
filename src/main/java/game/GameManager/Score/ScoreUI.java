package game.GameManager.Score;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Text.TextUI;

public final class ScoreUI extends MonoBehaviour {

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
        ScoreManager.getInstance().onScoreChanged
                .addListener(this::scoreManager_onScoreChanged);
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
        var popUp = ScorePopUpPrefab.instantiate();
        popUp.setAmount(e);
        popUp.setEntryPoint(scoreText.getTransform().getGlobalPosition());
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