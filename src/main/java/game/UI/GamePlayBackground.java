package game.UI;

import game.Level.LevelManager;
import game.Level.LevelType;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.UITween.Tween;
import utils.Vector2;


/**
 * Manages the visual background displayed during gameplay.
 * This component is responsible for instantiating the background sprites,
 * positioning them, and dynamically changing the background image and applying
 * effects (like a fade-in black screen) when a specific level type is loaded (e.g., Boss/Showdown).
 */
public class GamePlayBackground extends MonoBehaviour {

    private SpriteRenderer spriteRenderer;
    private SpriteRenderer blackBackground;
    private EventActionID gamePlayBackground_onLevelLoaded = null;

    private double TWEEN_DURATION = 3;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GamePlayBackground(GameObject owner) {
        super(owner);

        // Instantiate the main gameplay background sprite
        spriteRenderer = GameObjectManager.instantiate("GamePlayBackground")
                .addComponent(SpriteRenderer.class);

        // Instantiate a separate black background sprite for transition effects
        blackBackground = GameObjectManager.instantiate("BlackBackground")
                .addComponent(SpriteRenderer.class);

        // Set up the scene hierarchy and initial state
        spriteRenderer.getGameObject().setParent(owner);
        blackBackground.getGameObject().setParent(owner);

        spriteRenderer.setImage(ImageAsset.ImageIndex.GamePlayBackground_Normal.getImage());

        spriteRenderer.setPivot(new Vector2(0.5, 0.5));
        blackBackground.setPivot(new Vector2(0.5, 0.5));

        spriteRenderer.setRenderLayer(RenderLayer._2);
        blackBackground.setRenderLayer(RenderLayer.Overlay);

        // Center the background components on the screen
        owner.getTransform().setGlobalPosition(new Vector2((Main.STAGE_WIDTH) / 2, Main.STAGE_HEIGHT / 2));

        // Register event listener
        gamePlayBackground_onLevelLoaded = LevelManager.onLevelLoaded.addListener(this::gamePlayBackground_onLevelLoaded);
    }

    private void gamePlayBackground_onLevelLoaded(Object sender, LevelManager.OnLevelLoadedEventArgs e) {
        if (e.type == LevelType.Showdown) {
            // Switch main background image
            spriteRenderer.setImage(ImageAsset.ImageIndex.GamePlayBackground_Boss.getImage());
            // Set the black background image and make it visible (ready for fade-out)
            blackBackground.setImage(ImageAsset.ImageIndex.BlackBackground.getImage());
            blackBackgroundStartAnimation();
        }
    }

    @Override
    protected void onDestroy() {
        LevelManager.onLevelLoaded.removeListener(gamePlayBackground_onLevelLoaded);
    }

    /**
     * Initiates the fade-out animation for the black background layer.
     * This creates a visual transition effect, often used for dramatic scene changes.
     */
    public void blackBackgroundStartAnimation() {
        Tween.to(blackBackground.getGameObject())
                .fadeTo(0, TWEEN_DURATION)
                .play();
    }
}
