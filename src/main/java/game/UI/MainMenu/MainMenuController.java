package game.UI.MainMenu;

import game.GameManager.GameManager;
import game.UI.Buttons.*;
import javafx.application.Platform;
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
        linkButtons();
        startButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);
        continueButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);
        recordButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);
        optionsButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);
        quitButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);

    }

    /**
     * Called when {@link BaseButton#onAnyMenuButtonClicked} is invoked.<br><br>
     * This function handles menu button click event.
     */
    private void baseButton_onAnyMenuButtonClicked(Object sender, MouseEvent e) {
        System.out.println("Alo");
        if (sender instanceof StartButton) {
            onStartButtonClick(sender, e);
        } else if (sender instanceof ContinueButton) {
            onContinueButtonClick(sender, e);
        } else if (sender instanceof RecordButton) {
            onRecordButtonClick(sender, e);
        } else if (sender instanceof OptionsButton) {
            onOptionsButtonClick(sender, e);
        } else if (sender instanceof QuitButton) {
            onQuitButtonClick(sender, e);
        }
    }

    private void onStartButtonClick(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.InGame);
    }

    private void onContinueButtonClick(Object sender, MouseEvent e) {
        GameManager.getInstance().continueGame();
    }

    private void onRecordButtonClick(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.Record);
    }

    private void onOptionsButtonClick(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.Options);
    }

    private void onQuitButtonClick(Object sender, MouseEvent e) {
        Platform.exit();
    }

    private void linkButtons() {
        try {
            startButton = GameObjectManager.find("StartButton").getComponent(StartButton.class);
            continueButton = GameObjectManager.find("ContinueButton").getComponent(ContinueButton.class);
            recordButton = GameObjectManager.find("RecordButton").getComponent(RecordButton.class);
            optionsButton = GameObjectManager.find("OptionsButton").getComponent(OptionsButton.class);
            quitButton = GameObjectManager.find("QuitButton").getComponent(QuitButton.class);
        } catch (Exception e) {
            System.err.println("[MainMenuController] Failed to link buttons: " + e.getMessage());
        }

        System.out.println("[MainMenuController] All buttons linked successfully.");
    }

}
