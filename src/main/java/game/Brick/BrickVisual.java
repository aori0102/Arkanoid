package game.Brick;

import game.Effect.StatusEffect;
import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Annotation.LinkViaPrefab;

/**
 * Visual class for a brick.
 */
public final class BrickVisual extends MonoBehaviour {

    private final SpriteRenderer spriteRenderer = addComponent(SpriteRenderer.class);

    @LinkViaPrefab
    private Brick brick = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickVisual(GameObject owner) {
        super(owner);
    }

    @Override
    public void start() {
        brick.getBrickEffectController().onEffectInflicted
                .addListener(this::brickEffectController_onEffectInflicted);
        brick.getBrickEffectController().onEffectCleared
                .addListener(this::brickEffectController_onEffectCleared);
    }

    /**
     * Called when {@link BrickEffectController#onEffectInflicted} is invoked.<br><br>
     * This function changes the visual of the brick based on the effect inflicted.
     *
     * @param sender Event caller {@link BrickEffectController}.
     * @param e      Event argument indicating the effect that was inflicted.
     */
    private void brickEffectController_onEffectInflicted(Object sender, StatusEffect e) {
        spriteRenderer.setImage(switch (e) {
            case Burn -> ImageAsset.ImageIndex.OrangeBrick.getImage();
            case FrostBite -> ImageAsset.ImageIndex.CyanBrick.getImage();
            case Electrified -> ImageAsset.ImageIndex.PurpleBrick.getImage();
            default -> brick.getBrickType().imageIndex.getImage();
        });
        spriteRenderer.setSize(Brick.BRICK_SIZE);
    }

    /**
     * Called when {@link BrickEffectController#onEffectCleared} is invoked.<br><br>
     * This function changes the visual of the brick back to normal as the effect is removed.
     *
     * @param sender Event callers {@link BrickEffectController}.
     * @param e      Event argument indicating the effect that was removed.
     */
    private void brickEffectController_onEffectCleared(Object sender, StatusEffect e) {
        if (!brick.getBrickEffectController().hasAnyEffect()) {
            applyDefaultImage();
        }
    }

    /**
     * Set the image for this brick based on the {@link BrickType} provided via {@link Brick#getBrickType}.
     */
    public void applyDefaultImage() {
        spriteRenderer.setImage(brick.getBrickType().imageIndex.getImage());
        spriteRenderer.setSize(Brick.BRICK_SIZE);
    }

    /**
     * Set the overlay color for the brick.
     *
     * @param color The overlay color to be set.
     */
    public void setOverlayColor(Color color) {
        spriteRenderer.setOverlayColor(color);
    }

    /**
     * Link the central class of brick<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link BrickPrefab}
     * as part of component linking process.</i></b>
     *
     * @param brick The central class of brick.
     */
    public void linkBrick(Brick brick) {
        this.brick = brick;
    }

}