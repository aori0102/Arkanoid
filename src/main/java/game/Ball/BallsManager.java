package game.Ball;

import game.Effect.StatusEffectInfo;
import game.Player.Player;
import game.Effect.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    private static final int MAX_BALL_EXISTED = 100;

    private static BallsManager instance;

    private final HashSet<Ball> ballSet = new HashSet<>();
    public int index = 1;
    private StatusEffect currentEffect = null;

    public EventHandler<Void> onBallCountChanged = new EventHandler<>(BallsManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallsManager(GameObject owner) {
        super(owner);
        instance = this;
    }

    public void addBall(Ball ball) {
        ballSet.add(ball);
        onBallCountChanged.invoke(this, null);
    }

    public void removeBall(Ball ball) {
        ballSet.remove(ball);
        onBallCountChanged.invoke(this, null);
        if (ballSet.isEmpty()) {
            spawnInitialBall();
        }
    }

    public HashSet<Ball> getBallSet() {
        return ballSet;
    }

    public String ballNameBuilder() {
        return "ball" + index;
    }

    public String ballVisualNameBuilder() {
        return "ball" + index++;
    }

    public void applyStatusPowerUpEffect(StatusEffect statusEffect) {
        this.currentEffect = statusEffect;
        if (currentEffect != null) {
            var statusInfo = new StatusEffectInfo();
            statusInfo.effect = currentEffect;
            statusInfo.duration = Double.MAX_VALUE;
            for (var ball : ballSet) {
                ball.getBallEffectController().inflictEffect(statusInfo);
            }
        }
    }

    public void spawnInitialBall() {

        var ball = PrefabManager.instantiatePrefab(PrefabIndex.Ball)
                .getComponent(Ball.class);
        Player.getInstance().getPlayerPaddle().isFired = false;

        ballSet.add(ball);
    }

    public int getBallCount() {
        return ballSet.size();
    }

    public StatusEffect getCurrentEffect() {
        return currentEffect;
    }

    public static BallsManager getInstance() {
        return instance;
    }

    public int getMaxBallExisted() {
        return MAX_BALL_EXISTED;
    }
}