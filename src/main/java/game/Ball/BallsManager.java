package game.Ball;

import game.Effect.StatusEffectInfo;
import game.Player.Player;
import game.Effect.StatusEffect;
import game.Score.ScoreManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    private static final int MAX_BALL_TO_MULTIPLY = 12;

    private static BallsManager instance = null;

    private final HashSet<Ball> ballSet = new HashSet<>();
    private final HashSet<Ball> touchedPaddleSet = new HashSet<>();

    public int index = 1;

    public EventHandler<Void> onBallCountChanged = new EventHandler<>(BallsManager.class);
    public EventHandler<Void> onAllBallDestroyed = new EventHandler<>(BallsManager.class);
    public EventHandler<Void> onAllBallTouchedPaddle = new EventHandler<>(BallsManager.class);

    private EventActionID ball_onAnyBallJustHitPaddle_ID = null;
    private EventActionID ball_onAnyBallHitBrick_ID = null;
    private EventActionID ball_onAnyBallDestroyed_ID = null;
    private EventActionID ball_onAnyBallSpawned_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallsManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("BallsManager is a singleton!");
        }
        instance = this;
    }

    public static BallsManager getInstance() {
        return instance;
    }

    @Override
    public void awake() {
        ball_onAnyBallJustHitPaddle_ID = Ball.onAnyBallHitPaddle
                .addListener(this::ball_onAnyBallJustHitPaddle);
        ball_onAnyBallHitBrick_ID = Ball.onAnyBallHitBrick
                .addListener(this::ball_onAnyBallHitBrick);
        ball_onAnyBallDestroyed_ID = Ball.onAnyBallDestroyed
                .addListener(this::ball_onAnyBallDestroyed);
        ball_onAnyBallSpawned_ID = Ball.onAnyBallSpawned
                .addListener(this::ball_onAnyBallSpawned);
    }

    @Override
    public void onDestroy() {
        Ball.onAnyBallHitPaddle.removeListener(ball_onAnyBallJustHitPaddle_ID);
        Ball.onAnyBallHitBrick.removeListener(ball_onAnyBallHitBrick_ID);
        Ball.onAnyBallDestroyed.removeListener(ball_onAnyBallDestroyed_ID);
        Ball.onAnyBallSpawned.removeListener(ball_onAnyBallSpawned_ID);
        instance = null;
    }

    /**
     *
     * @param ball
     */
    private void addBall(Ball ball) {
        ballSet.add(ball);
        onBallCountChanged.invoke(this, null);
    }

    /**
     *
     * @param ball
     */
    private void removeBall(Ball ball) {
        ballSet.remove(ball);
        onBallCountChanged.invoke(this, null);
        if (ballSet.isEmpty()) {
            onAllBallDestroyed.invoke(this, null);
        }
    }

    public boolean removeRandomBall() {

        if (ballSet.isEmpty()) {
            return false;
        }
        var ballToRemove = ballSet.iterator().next();
        ballSet.remove(ballToRemove);
        GameObjectManager.destroy(ballToRemove.getGameObject());

        return true;

    }

    /**
     * Called when {@link Ball#onAnyBallHitPaddle} is invoked.<br><br>
     * This function adds the ball to {@link #touchedPaddleSet} handling combo through {@link ScoreManager}.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallJustHitPaddle(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            touchedPaddleSet.add(ball);
            if (touchedPaddleSet.size() == ballSet.size()) {
                onAllBallTouchedPaddle.invoke(this, null);
                touchedPaddleSet.clear();
            }
        }
    }

    /**
     * Called when {@link Ball#onAnyBallHitBrick} is invoked.<br><br>
     * This function clears hit paddle mark on the ball that hits a brick.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallHitBrick(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            touchedPaddleSet.remove(ball);
        }
    }

    /**
     * Called when {@link Ball#onAnyBallDestroyed} is invoked.<br><br>
     * This function removes the destroyed ball from {@link #ballSet}.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallDestroyed(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            touchedPaddleSet.remove(ball);
            removeBall(ball);
            if (touchedPaddleSet.size() == ballSet.size()) {
                onAllBallTouchedPaddle.invoke(this, null);
                touchedPaddleSet.clear();
            }
        }
    }

    /**
     * Called when {@link Ball#onAnyBallSpawned} is invoked.<br><br>
     * This function adds the spawned {@link Ball} to {@link #ballSet}.
     *
     * @param sender Event caller {@link Ball}.
     * @param e      Empty event argument.
     */
    private void ball_onAnyBallSpawned(Object sender, Void e) {
        if (sender instanceof Ball ball) {
            addBall(ball);
        }
    }

    /**
     *
     * @return
     */
    public HashSet<Ball> getBallSet() {
        return ballSet;
    }

    /**
     *
     * @param statusEffect
     */
    public void applyStatusPowerUpEffect(StatusEffect statusEffect) {
        if (statusEffect != null) {
            var statusInfo = new StatusEffectInfo();
            statusInfo.effect = statusEffect;
            statusInfo.duration = Double.MAX_VALUE;
            for (var ball : ballSet) {
                ball.getBallEffectController().inflictEffect(statusInfo);
            }
        }
    }

    /**
     * @return
     */
    public void spawnInitialBall() {

        var ball = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                .getComponent(Ball.class);
        Player.getInstance().getPlayerPaddle().isFired = false;

        ballSet.add(ball);
    }

    /**
     *
     * @return
     */
    public int getBallCount() {
        return ballSet.size();
    }

    /**
     *
     * @return
     */
    public boolean canSpawnBallMultiplication() {
        return getBallCount() < MAX_BALL_TO_MULTIPLY;
    }

}