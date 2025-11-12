package game.UI;

import game.GameOver.GameOverManager;
import game.UI.Buttons.GameOverMenuButton;
import game.UI.Buttons.RestartButton;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Scene.SceneKey;
import org.Scene.SceneManager;

public class GameOverMenuController extends MonoBehaviour {
    private RestartButton restartButton;
    private GameOverMenuButton gameOverMenuButton;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameOverMenuController(GameObject owner) {
        super(owner);

        restartButton = GameOverManager.getInstance().getRestartButton();
        gameOverMenuButton = GameOverManager.getInstance().getGameOverMenuButton();

        restartButton.getButtonUI().onPointerClick
                .addListener(this::restartButton_onPointerClicked);
        gameOverMenuButton.getButtonUI().onPointerClick
                .addListener(this::gameOverMenuButton_onPointerClicked);
    }

    private void restartButton_onPointerClicked(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.InGame);
    }

    private void gameOverMenuButton_onPointerClicked(Object sender, MouseEvent e) {
        SceneManager.loadScene(SceneKey.Menu);
    }


}
