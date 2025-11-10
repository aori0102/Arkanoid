package game.UI.MainMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import javafx.stage.Stage;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class MainMenuManager extends MonoBehaviour {
    private static MainMenuManager instance;
    private StartButton startButton;
    private ContinueButton continueButton;
    private RecordButton recordButton;
    private OptionsButton optionsButton;
    private QuitButton quitButton;
    private GameTitle gameTitle;
    private final List<BaseButton> mainMenuButtons = new ArrayList<>();

    private static final double BUTTON_OFFSET = 300;
    private final double SLIDE_DURATION = 0.6;
    private final double SLIDE_DISTANCE = Main.STAGE_WIDTH;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MainMenuManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("MainMenuManager is a singleton");
        }
        instance = this;
        PrefabManager.instantiatePrefab(PrefabIndex.StartButton);
        PrefabManager.instantiatePrefab(PrefabIndex.RecordButton);
        PrefabManager.instantiatePrefab(PrefabIndex.OptionsButton);
        PrefabManager.instantiatePrefab(PrefabIndex.QuitButton);
        PrefabManager.instantiatePrefab(PrefabIndex.ContinueButton);
        PrefabManager.instantiatePrefab(PrefabIndex.GameTitle);

        startButton.getTransform().setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2, BUTTON_OFFSET));
        continueButton.getTransform().setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 100));
        recordButton.getTransform().setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 200));
        optionsButton.getTransform().setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 300));
        quitButton.getTransform().setGlobalPosition(new Vector2(-Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 400));

        GameObjectManager.instantiate("MainMenuController").addComponent(MainMenuController.class);
    }

    @Override
    public void onDestroy() {
        instance = null;
    }

    @Override
    public void start() {
        showUI();
    }

    public void showUI() {
        double i = 0;
        for (var button : mainMenuButtons) {
            Tween.to(button.getGameObject())
                    .moveX(SLIDE_DISTANCE, SLIDE_DURATION)
                    .ease(Ease.OUT_BACK)
                    .setDelay(0.0 + i)
                    .ignoreTimeScale(true)
                    .play();
            i += 0.1;
        }
        gameTitle.startAnimation();

    }

    public void hideUI() {
        for (var button : mainMenuButtons) {
            Tween.to(button.getGameObject())
                    .moveX(-SLIDE_DISTANCE, SLIDE_DURATION)
                    .ease(Ease.IN_BACK)
                    .setDelay(0.0)
                    .ignoreTimeScale(true)
                    .play();
        }
        gameTitle.exitAnimation();
    }

    public static MainMenuManager getInstance() {
        return instance;
    }

    public void linkStartButton(StartButton startButton) {
        this.startButton = startButton;
        mainMenuButtons.add(startButton);
    }

    public void linkContinueButton(ContinueButton continueButton) {
        this.continueButton = continueButton;
        mainMenuButtons.add(continueButton);
    }

    public void linkRecordButton(RecordButton recordButton) {
        this.recordButton = recordButton;
        mainMenuButtons.add(recordButton);
    }

    public void linkOptionsButton(OptionsButton optionsButton) {
        this.optionsButton = optionsButton;
        mainMenuButtons.add(optionsButton);
    }

    public void linkQuitButton(QuitButton quitButton) {
        this.quitButton = quitButton;
        mainMenuButtons.add(quitButton);
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param gameTitle .
     */
    public void linkGameTitle(GameTitle gameTitle) {
        this.gameTitle = gameTitle;
    }

    public StartButton getStartButton() {
        return startButton;
    }

    public ContinueButton getContinueButton() {
        return continueButton;
    }

    public RecordButton getRecordButton() {
        return recordButton;
    }

    public OptionsButton getOptionsButton() {
        return optionsButton;
    }

    public QuitButton getQuitButton() {
        return quitButton;
    }
}
