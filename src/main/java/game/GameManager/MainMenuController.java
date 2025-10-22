package game.GameManager;

import game.UI.*;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;

import javafx.scene.input.MouseEvent;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class MainMenuController extends MonoBehaviour {
    private StartButton startButton;
    private ContinueButton continueButton;
    private RecordButton recordButton;
    private OptionsButton optionsButton;
    private QuitButton quitButton;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public MainMenuController(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        startButton = GameObjectManager.find("StartButton").getComponent(StartButton.class);
        continueButton = GameObjectManager.find("ContinueButton").getComponent(ContinueButton.class);
        recordButton = GameObjectManager.find("RecordButton").getComponent(RecordButton.class);
        optionsButton = GameObjectManager.find("OptionsButton").getComponent(OptionsButton.class);
        quitButton = GameObjectManager.find("QuitButton").getComponent(QuitButton.class);

        startButton.getButtonUI().onPointerClick.addListener(this::onStartButtonClick);
        continueButton.getButtonUI().onPointerClick.addListener(this::onContinueButtonClick);
        recordButton.getButtonUI().onPointerClick.addListener(this::onRecordButtonClick);
        optionsButton.getButtonUI().onPointerClick.addListener(this::onOptionsButtonClick);
        quitButton.getButtonUI().onPointerClick.addListener(this::onQuitButtonClick);
    }

    private void onStartButtonClick(Object sender, MouseEvent e) {
        GameManager.instance.startNewGame();
    }

    private void onContinueButtonClick(Object sender, MouseEvent e) {
        GameManager.instance.continueGame();
    }

    private void onRecordButtonClick(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.Record);
    }

    private void onOptionsButtonClick(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.Options);
    }

    private void onQuitButtonClick(Object sender, MouseEvent e) {
        GameManager.instance.quitGame();
    }

    @Override
    protected void destroyComponent() {

    }
}
