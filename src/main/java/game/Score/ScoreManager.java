package game.Score;

import game.Brick.Brick;
import game.Ball.Ball;
import game.Ball.BallsManager;
import game.GameManager.LevelState;
import game.Level.LevelManager;
import game.MapGenerator.BrickMapManager;
import game.PlayerData.DataManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class ScoreManager extends MonoBehaviour {

    private static final int BALL_SCORE_WHEN_CLEARED = 12;

    private static ScoreManager instance = null;

    /**
     * <b>Read-only. Write via {@link #setScore}.</b>
     */
    private int _score = 0;

    /**
     * Setter for read-only field {@link #_score}
     *
     * @param score The value to set.
     */
    private void setScore(int score) {
        this._score = score;
        onScoreChanged.invoke(this, score);
    }

    /**
     * <b>Read-only. Write via {@link #setCombo}.</b>
     */
    private int _combo = 0;

    /**
     * Setter for read-only field {@link #_combo}
     *
     * @param combo The value to set.
     */
    private void setCombo(int combo) {
        this._combo = combo;
        onComboChanged.invoke(this, combo);
    }

    private EventActionID brick_onAnyBrickDestroyed_ID = null;
    private EventActionID ball_onAnyBallHitBrick_ID = null;
    private EventActionID ball_onAnyBallDestroyed_ID = null;
    private EventActionID ballsManager_onAllBallDestroyed_ID = null;

    public EventHandler<Integer> onScoreChanged = new EventHandler<>(ScoreManager.class);
    public EventHandler<Integer> onComboChanged = new EventHandler<>(ScoreManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ScoreManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("ScoreManager is a singleton");
        }
        instance = this;
    }

    @Override
    public void start() {
        brick_onAnyBrickDestroyed_ID = Brick.onAnyBrickDestroyed.addListener(this::brick_onAnyBrickDestroyed);
        ball_onAnyBallHitBrick_ID = Ball.onAnyBallHitBrick.addListener(this::ball_onAnyBallHitBrick);
        ball_onAnyBallDestroyed_ID = Ball.onAnyBallDestroyed.addListener(this::ball_onAnyBallDestroyed);
        ballsManager_onAllBallDestroyed_ID = BallsManager.getInstance().onAllBallDestroyed
                .addListener(this::ballsManager_onAllBallDestroyed);
        loadSave();
    }

    @Override
    public void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
        Ball.onAnyBallHitBrick.removeListener(ball_onAnyBallHitBrick_ID);
        Ball.onAnyBallDestroyed.removeListener(ball_onAnyBallDestroyed_ID);
        if (BallsManager.getInstance() != null) {
            BallsManager.getInstance().onAllBallDestroyed
                    .removeListener(ballsManager_onAllBallDestroyed_ID);
        }
        instance = null;
    }

    /**
     * Called when {@link Ball#onAnyBallDestroyed} is invoked.<br><br>
     * This function handles when a {@link Ball} is destroyed to reset combo.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallDestroyed(Object sender, Void e) {
        var levelState = LevelManager.getInstance().getLevelState();
        if (levelState == LevelState.ConcludingLevel) {
            setScore(_score + _combo + BALL_SCORE_WHEN_CLEARED);
        }
    }

    /**
     * Called when {@link Ball#onAnyBallHitBrick} is invoked.<br><br>
     * This function increment the combo as any {@link Ball} hits a {@link Brick}.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallHitBrick(Object sender, Void e) {
        setCombo(_combo + 1);
    }

    /**
     * Called when {@link game.Brick.Brick#onAnyBrickDestroyed} is invoked.<br><br>
     * This function handles score after a brick is destroyed.
     *
     * @param sender Event caller {@link BrickMapManager}.
     * @param e      Event argument indicating the type of brick.
     */
    private void brick_onAnyBrickDestroyed(Object sender, Brick.OnBrickDestroyedEventArgs e) {
        setScore(_score + e.brickType.score + _combo);
    }

    /**
     * Called when {@link BallsManager#onAllBallDestroyed} is invoked.<br><br>
     * This function resets the combo when all ball is destroyed.
     *
     * @param sender Event caller {@link BallsManager}.
     * @param e      Empty event argument.
     */
    private void ballsManager_onAllBallDestroyed(Object sender, Void e) {
        setCombo(0);
    }

    public static ScoreManager getInstance() {
        return instance;
    }

    private void loadSave() {
        var save = DataManager.getInstance().getSave();
        setScore(save.getScore());
        setCombo(save.getCombo());
    }

    public int getScore() {
        return _score;
    }

    public int getCombo() {
        return _combo;
    }

}