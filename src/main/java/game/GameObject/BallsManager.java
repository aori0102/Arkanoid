package game.GameObject;

import game.Player.Player;
import game.Effect.StatusEffect;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    private static final int MAX_BALL_EXISTED = 30;

    private static BallsManager instance;

    private final HashSet<Ball> ballSet = new HashSet<>();
    public int index = 1;
    private StatusEffect currentEffect = StatusEffect.None;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BallsManager(GameObject owner) {
        super(owner);
        instance = this;
    }

    public void addBall(Ball ball){
        ballSet.add(ball);
    }

    public void removeBall(Ball ball){
        ballSet.remove(ball);

        if (ballSet.isEmpty()){
            spawnInitialBall();
        }
    }

    public HashSet<Ball> getBallSet(){
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
        var ball = GameObjectManager.instantiate("ball").addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball.getGameObject());
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        Player.getInstance().getPlayerPaddle().isFired = false;

        ballSet.add(ball);
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