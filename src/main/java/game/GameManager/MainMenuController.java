package game.GameManager;

import game.Player.Player;
import game.UI.*;
import game.Voltraxis.Voltraxis;
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
        BaseButton.onAnyMenuButtonClicked.addListener(this::baseButton_onAnyMenuButtonClicked);
    }

    /**
     * Called when {@link BaseButton#onAnyMenuButtonClicked} is invoked.<br><br>
     * This function handles menu button click event.
     */
    private void baseButton_onAnyMenuButtonClicked(Object sender, MouseEvent e) {
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
        GameManager.getInstance().startNewGame();
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
        GameManager.getInstance().quitGame();
    }

}
