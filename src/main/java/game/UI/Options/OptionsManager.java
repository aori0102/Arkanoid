package game.UI.Options;

import game.UI.GoBackButtonManager;
import game.UI.Options.Slider.MasterVolumeSlider;
import game.UI.Options.Slider.MusicVolumeSlider;
import game.UI.Options.Slider.SFXVolumeSlider;
import game.UI.Options.Text.MasterVolumeText;
import game.UI.Options.Text.MusicVolumeText;
import game.UI.Options.Text.OptionsTitle;
import game.UI.Options.Text.SFXVolumeText;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

/**
 * Manages the display, animation, and state of the Options Menu UI.
 * <p>
 * This Singleton class controls the slide-in and slide-out animations of volume sliders,
 * volume labels, and the options title.
 */
public class OptionsManager extends MonoBehaviour {
    private static OptionsManager instance;
    private MasterVolumeSlider masterVolumeSlider = null;
    private MusicVolumeSlider musicVolumeSlider = null;
    private SFXVolumeSlider sFXVolumeSlider = null;
    private boolean isOptionsMenuShowed = false;

    private final double SLIDE_DISTANCE = -Main.STAGE_WIDTH;
    private final double SLIDE_DURATION = 0.8;
    private final double SLIDER_OFFSET = 100;
    private final double TEXT_OFFSET = 45;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public OptionsManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("OptionsManager is a singleton");
        }
        instance = this;

        PrefabManager.instantiatePrefab(PrefabIndex.MasterVolumeSlider);
        PrefabManager.instantiatePrefab(PrefabIndex.MusicVolumeSlider);
        PrefabManager.instantiatePrefab(PrefabIndex.SFXVolumeSlider);
        PrefabManager.instantiatePrefab(PrefabIndex.MasterVolumeText);
        PrefabManager.instantiatePrefab(PrefabIndex.MusicVolumeText);
        PrefabManager.instantiatePrefab(PrefabIndex.SFXVolumeText);
        PrefabManager.instantiatePrefab(PrefabIndex.OptionsTitle);

        masterVolumeSlider.getTransform()
                .setGlobalPosition(new Vector2(3 * Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2 - SLIDER_OFFSET));
        musicVolumeSlider.getTransform()
                .setGlobalPosition(new Vector2(3 * Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2));
        sFXVolumeSlider.getTransform()
                .setGlobalPosition(new Vector2(3 * Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2 + SLIDER_OFFSET));

        masterVolumeText.getTransform()
                .setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2,
                        Main.STAGE_HEIGHT / 2 - SLIDER_OFFSET - TEXT_OFFSET));
        musicVolumeText.getTransform()
                .setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2,
                        Main.STAGE_HEIGHT / 2 - TEXT_OFFSET));
        sFXVolumeText.getTransform()
                .setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2,
                        Main.STAGE_HEIGHT / 2 + SLIDER_OFFSET - TEXT_OFFSET));

    }

    /**
     * Retrieves the Singleton instance of the OptionsManager.
     *
     * @return The single active instance.
     */
    public static OptionsManager getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

    /**
     * Animates the Options UI elements onto the screen.
     * Sliders slide in from the right (positive move), and text labels slide in from the left (negative move).
     */
    public void showUI() {
        movePositiveAnimation(masterVolumeSlider.getGameObject());
        movePositiveAnimation(musicVolumeSlider.getGameObject());
        movePositiveAnimation(sFXVolumeSlider.getGameObject());
        moveNegativeAnimation(masterVolumeText.getGameObject());
        moveNegativeAnimation(musicVolumeText.getGameObject());
        moveNegativeAnimation(sFXVolumeText.getGameObject());

        optionsTitle.startAnimation();

        GoBackButtonManager.getInstance().showUI();
        isOptionsMenuShowed = true;
    }

    /**
     * Animates the Options UI elements off the screen.
     * Sliders slide out to the right (negative move), and text labels slide out to the left (positive move).
     */
    public void hideUI() {
        moveNegativeAnimation(masterVolumeSlider.getGameObject());
        moveNegativeAnimation(musicVolumeSlider.getGameObject());
        moveNegativeAnimation(sFXVolumeSlider.getGameObject());
        movePositiveAnimation(masterVolumeText.getGameObject());
        movePositiveAnimation(sFXVolumeText.getGameObject());
        movePositiveAnimation(musicVolumeText.getGameObject());

        optionsTitle.exitAnimation();

        GoBackButtonManager.getInstance().hideUI();
        isOptionsMenuShowed = false;
    }

    /**
     * Checks if the Options Menu UI is currently visible.
     *
     * @return true if the menu is visible, false otherwise.
     */
    public boolean isOptionsMenuShowed() {
        return isOptionsMenuShowed;
    }

    /**
     * Links the {@link MasterVolumeSlider} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param masterVolumeSlider The MasterVolumeSlider component.
     */
    public void linkMasterVolumeSlider(MasterVolumeSlider masterVolumeSlider) {
        this.masterVolumeSlider = masterVolumeSlider;
    }


    /**
     * Links the {@link MusicVolumeSlider} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param musicVolumeSlider The MusicVolumeSlider component.
     */
    public void linkMusicVolumeSlider(MusicVolumeSlider musicVolumeSlider) {
        this.musicVolumeSlider = musicVolumeSlider;
    }

    /**
     * Links the {@link SFXVolumeSlider} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param sFXVolumeSlider The SFXVolumeSlider component.
     */
    public void linkSFXVolumeSlider(SFXVolumeSlider sFXVolumeSlider) {
        this.sFXVolumeSlider = sFXVolumeSlider;
    }

    private MasterVolumeText masterVolumeText = null;

    /**
     * Links the {@link MasterVolumeText} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param masterVolumeText The MasterVolumeText component.
     */
    public void linkMasterVolumeText(MasterVolumeText masterVolumeText) {
        this.masterVolumeText = masterVolumeText;
    }

    private MusicVolumeText musicVolumeText = null;

    /**
     * Links the {@link MusicVolumeText} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param musicVolumeText The MusicVolumeText component.
     */
    public void linkMusicVolumeText(MusicVolumeText musicVolumeText) {
        this.musicVolumeText = musicVolumeText;
    }

    private SFXVolumeText sFXVolumeText = null;

    /**
     * Links the {@link SFXVolumeText} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param sFXVolumeText The SFXVolumeText component.
     */
    public void linkSFXVolumeText(SFXVolumeText sFXVolumeText) {
        this.sFXVolumeText = sFXVolumeText;
    }

    private OptionsTitle optionsTitle = null;

    /**
     * Links the {@link OptionsTitle} component.
     * <p>NOTE: Only use within the prefab linking process.</p>
     *
     * @param optionsTitle The OptionsTitle component.
     */
    public void linkOptionsTitle(OptionsTitle optionsTitle) {
        this.optionsTitle = optionsTitle;
    }

    /**
     * Creates a Tween animation to move the GameObject to its final on-screen position (from right to center).
     * The movement is equivalent to adding the absolute slide distance to the current X position.
     *
     * @param gameObject The GameObject to animate.
     */
    private void movePositiveAnimation(GameObject gameObject) {
        Tween.to(gameObject)
                .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0)
                .play();
    }

    /**
     * Creates a Tween animation to move the GameObject to its final on-screen position (from left to center).
     * The movement is equivalent to subtracting the absolute slide distance from the current X position.
     *
     * @param gameObject The GameObject to animate.
     */
    private void moveNegativeAnimation(GameObject gameObject) {
        Tween.to(gameObject)
                .moveX(-SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0)
                .play();
    }

}
