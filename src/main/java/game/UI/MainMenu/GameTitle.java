package game.UI.MainMenu;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Random;
import utils.Time;
import utils.Vector2;

public class GameTitle extends MonoBehaviour {
    private static final double FLUCTUATION_RANDOM_MAX = 5;
    private static final double randomTime = Random.range(0, FLUCTUATION_RANDOM_MAX);
    private static Vector2 oldPosition;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameTitle(GameObject owner) {
        super(owner);

        SpriteRenderer spriteRenderer = owner.addComponent(SpriteRenderer.class);
        spriteRenderer.setImage(ImageAsset.ImageIndex.GameTitle.getImage());
        spriteRenderer.getTransform().setGlobalScale(new Vector2(0.5,0.5));
        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
    }

    @Override
    public void start() {
        oldPosition = getTransform().getGlobalPosition();
    }

    public void update() {
        idleAnimation();
    }

    private void idleAnimation() {
        double GAME_TITLE_FLUCTUATION_RATE = 0.6;
        double MAX_GAME_TITLE_FLUCTUATION_DISTANCE = 5;
        var delta = Math.sin(GAME_TITLE_FLUCTUATION_RATE * Time.time * Math.PI + randomTime) * MAX_GAME_TITLE_FLUCTUATION_DISTANCE;
        var deltaVector = new Vector2(getTransform().getGlobalPosition().x, oldPosition.y + delta);
        gameObject.getTransform().setGlobalPosition(deltaVector);
    }
}
