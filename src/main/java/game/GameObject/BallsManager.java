package game.GameObject;

import game.PowerUp.StatusEffect;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;

import javafx.scene.image.Image;
import java.util.HashSet;

public class BallsManager extends MonoBehaviour {

    public static BallsManager instance;
    private HashSet<Ball> ballSet = new HashSet<>();
    public int index = 1;

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
        switch(statusEffect) {
            case Burn -> {
                changeBallVisual(ImageAsset.ImageIndex.FireBall.getImage());
            }

            case FrostBite -> {
                changeBallVisual(ImageAsset.ImageIndex.BlizzardBall.getImage());
            }
        }
    }

    public void resetBallVisual() {
        changeBallVisual(ImageAsset.ImageIndex.Ball.getImage());
    }

    private void changeBallVisual(Image image) {
        for (var ball : ballSet) {
            if (ball.getBallVisual() != null) {
                ball.getBallVisual().setImage(image);
            }
        }
    }


    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
