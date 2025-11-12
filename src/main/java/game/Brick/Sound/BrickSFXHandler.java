package game.Brick.Sound;

import game.Brick.Brick;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class BrickSFXHandler extends MonoBehaviour {

    private static BrickSFXHandler instance = null;

    private EventActionID brick_onAnyBrickHit_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickSFXHandler(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("BrickSFXHandler is a singleton!");
        }
        instance = this;
    }

    @Override
    protected void onDestroy() {
        Brick.onAnyBrickHit.removeListener(brick_onAnyBrickHit_ID);
        instance = null;
    }

    @Override
    public void start() {
        brick_onAnyBrickHit_ID = Brick.onAnyBrickHit.addListener(this::brick_onAnyBrickHit);
    }

    /**
     * Called when {@link Brick#onAnyBrickHit} is invoked.<br><br>
     * This function plays a sound when a {@link Brick} is hit.
     *
     * @param sender Event caller {@link Brick}.
     * @param e      Empty event argument.
     */
    private void brick_onAnyBrickHit(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnBrickHit);
    }

}