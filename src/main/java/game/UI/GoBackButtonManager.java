package game.UI;

import game.UI.Buttons.GoBackButton;
import game.UI.MainMenu.MainMenuManager;
import game.UI.Options.OptionsManager;
import javafx.scene.input.MouseEvent;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

public class GoBackButtonManager extends MonoBehaviour {
    private static GoBackButtonManager instance;

    private final double SLIDE_DURATION = 0.6;
    private final double SLIDE_DISTANCE = 150;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GoBackButtonManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new IllegalStateException("GoBackButtonManager already instantiated!");
        }

        instance = this;

        PrefabManager.instantiatePrefab(PrefabIndex.GoBackButton);

        goBackButton.getTransform().setGlobalPosition(new Vector2(-SLIDE_DISTANCE / 2, SLIDE_DISTANCE / 2));

        goBackButton.getButtonUI().onPointerClick.addListener(this::goBackButtonManager_onPointerClicked);

    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

    public void goBackButtonManager_onPointerClicked(Object sender, MouseEvent mouseEvent) {
        MainMenuManager.getInstance().showUI();
        OptionsManager.getInstance().hideUI();
        hideUI();
    }

    public void showUI() {
        Tween.to(goBackButton.getGameObject())
                .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                .setDelay(0)
                .ease(Ease.OUT_BACK)
                .play();
    }

    public void hideUI() {
        Tween.to(goBackButton.getGameObject())
                .moveX(-SLIDE_DISTANCE, SLIDE_DURATION)
                .setDelay(0)
                .ease(Ease.OUT_BACK)
                .play();
    }


    public static GoBackButtonManager getInstance() {
        return instance;
    }

    private GoBackButton goBackButton = null;

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param goBackButton .
     */
    public void linkGoBackButton(GoBackButton goBackButton) {
        this.goBackButton = goBackButton;
    }
}
