package game.GameObject;

import game.PowerUp.StatusEffect;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    public static BallsManager instance;
    private HashSet<Ball> ballSet = new HashSet<>();
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

    private void resetBallStatus() {
        for (var ball : ballSet) {
            ball.addEffect(StatusEffect.None);
        }
    }

    public StatusEffect getCurrentEffect() {
        return currentEffect;
    }

    @Override
    protected void destroyComponent() {

    }
}
