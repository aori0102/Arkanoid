package game.UI.Prefab.GameOverMenu;

import game.GameOver.GameOverManager;
import game.UI.Buttons.GameOverMenuButton;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class GameOverMenuButtonPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        GameOverMenuButton gameOverMenuButton = GameObjectManager.instantiate("GameOverMenuButton")
                .addComponent(GameOverMenuButton.class);

        GameOverManager.getInstance().linkGameOverMenuButton(gameOverMenuButton);

        return gameOverMenuButton.getGameObject();
    }
}
