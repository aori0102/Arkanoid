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

    public static OptionsManager getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

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

    public boolean isOptionsMenuShowed() {
        return isOptionsMenuShowed;
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param masterVolumeSlider .
     */
    public void linkMasterVolumeSlider(MasterVolumeSlider masterVolumeSlider) {
        this.masterVolumeSlider = masterVolumeSlider;
    }


    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param musicVolumeSlider .
     */
    public void linkMusicVolumeSlider(MusicVolumeSlider musicVolumeSlider) {
        this.musicVolumeSlider = musicVolumeSlider;
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param sFXVolumeSlider .
     */
    public void linkSFXVolumeSlider(SFXVolumeSlider sFXVolumeSlider) {
        this.sFXVolumeSlider = sFXVolumeSlider;
    }

    private MasterVolumeText masterVolumeText = null;

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param masterVolumeText .
     */
    public void linkMasterVolumeText(MasterVolumeText masterVolumeText) {
        this.masterVolumeText = masterVolumeText;
    }

    private MusicVolumeText musicVolumeText = null;

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param musicVolumeText .
     */
    public void linkMusicVolumeText(MusicVolumeText musicVolumeText) {
        this.musicVolumeText = musicVolumeText;
    }

    private SFXVolumeText sFXVolumeText = null;

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param sFXVolumeText .
     */
    public void linkSFXVolumeText(SFXVolumeText sFXVolumeText) {
        this.sFXVolumeText = sFXVolumeText;
    }

    private OptionsTitle optionsTitle = null;

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param optionsTitle .
     */
    public void linkOptionsTitle(OptionsTitle optionsTitle) {
        this.optionsTitle = optionsTitle;
    }

    private void movePositiveAnimation(GameObject gameObject){
        Tween.to(gameObject)
                .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0)
                .play();
    }

    private void moveNegativeAnimation(GameObject gameObject){
        Tween.to(gameObject)
                .moveX(-SLIDE_DISTANCE, SLIDE_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0)
                .play();
    }

}
