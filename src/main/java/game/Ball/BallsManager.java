package game.Ball;

import game.Effect.StatusEffectInfo;
import game.Player.Player;
import game.Effect.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    private static final int MAX_BALL_TO_MULTIPLY = 12;

    private static BallsManager instance;

    private final HashSet<Ball> ballSet = new HashSet<>();
    public int index = 1;

    public EventHandler<Void> onBallCountChanged = new EventHandler<>(BallsManager.class);
    public EventHandler<Void> onAllBallDestroyed = new EventHandler<>(BallsManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallsManager(GameObject owner) {
        super(owner);
        instance = this;
    }

    public static BallsManager getInstance() {
        return instance;
    }

    /**
     *
     * @param ball
     */
    public void addBall(Ball ball) {
        ballSet.add(ball);
        onBallCountChanged.invoke(this, null);
    }

    /**
     *
     * @param ball
     */
    public void removeBall(Ball ball) {
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