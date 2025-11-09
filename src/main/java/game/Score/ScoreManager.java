package game.Score;

import game.Brick.Brick;
import game.Ball.Ball;
import game.Ball.BallsManager;
import game.GameManager.LevelState;
import game.Level.LevelManager;
import game.MapGenerator.BrickMapManager;
import game.Player.Paddle.PlayerPaddle;
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

    private int touchedBall = 0;

    private EventActionID brick_onAnyBrickDestroyed_ID = null;
    private EventActionID ball_onAnyBallHitBrick_ID = null;
    private EventActionID ball_onAnyBallJustHitPaddle_ID = null;
    private EventActionID ball_onAnyBallDestroyed_ID = null;
    private EventActionID ballsManager_onBallCountChanged_ID = null;

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
        ball_onAnyBallJustHitPaddle_ID = Ball.onAnyBallJustHitPaddle.addListener(this::ball_onAnyBallHitPaddle);
        ball_onAnyBallDestroyed_ID = Ball.onAnyBallDestroyed.addListener(this::ball_onAnyBallDestroyed);
        ballsManager_onBallCountChanged_ID = BallsManager.getInstance().onBallCountChanged
                .addListener(this::ballsManager_onBallCountChanged);
        loadSave();
    }

    @Override
    public void onDestroy() {
        Brick.onAnyBrickDestroyed.removeListener(brick_onAnyBrickDestroyed_ID);
        Ball.onAnyBallHitBrick.removeListener(ball_onAnyBallHitBrick_ID);
        Ball.onAnyBallJustHitPaddle.removeListener(ball_onAnyBallJustHitPaddle_ID);
        Ball.onAnyBallDestroyed.removeListener(ball_onAnyBallDestroyed_ID);
        if (BallsManager.getInstance() != null) {
            BallsManager.getInstance().onBallCountChanged
                    .removeListener(ballsManager_onBallCountChanged_ID);
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
        } else if (levelState == LevelState.Playing) {
            if (sender instanceof Ball ball && ball.isHitPaddle()) {
                touchedBall--;
            }
        }
    }

    /**
     * Called when {@link BallsManager#onBallCountChanged} is invoked.<br><br>
     * This function resets combo when {@link BallsManager#getBallCount} reaches zero.
     *
     * @param sender Event caller {@link BallsManager}.
     * @param e      Empty event argument.
     */
    private void ballsManager_onBallCountChanged(Object sender, Void e) {
        int count = BallsManager.getInstance().getBallCount();
        if (count == 0 || count == touchedBall) {
            setCombo(0);
            touchedBall = 0;
        }
    }

    /**
     * Called when {@link Ball#onAnyBallJustHitPaddle} is invoked.<br><br>
     * This function handles combo reset when a {@link Ball} hits a {@link PlayerPaddle}.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallHitPaddle(Object sender, Void e) {
        touchedBall++;
        if (touchedBall == BallsManager.getInstance().getBallCount()) {
            setCombo(0);
            touchedBall = 0;
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