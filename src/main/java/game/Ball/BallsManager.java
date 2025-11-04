package game.Ball;

import game.Player.Player;
import game.Effect.StatusEffect;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    private static final int MAX_BALL_EXISTED = 100;

    private static BallsManager instance;

    private final HashSet<Ball> ballSet = new HashSet<>();
    public int index = 1;
    private StatusEffect currentEffect = StatusEffect.None;

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
        for (var ball : ballSet) {
            ball.addEffect(currentEffect);
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