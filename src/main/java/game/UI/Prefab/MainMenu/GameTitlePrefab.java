package game.UI.Prefab.MainMenu;

import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuManager;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class GameTitlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        GameTitle gameTitle = GameObjectManager.instantiate("GameTitle")
                .addComponent(GameTitle.class);

        MainMenuManager.getInstance().linkGameTitle(gameTitle);
        return gameTitle.getGameObject();
    }
}
