package game.UI.Prefab.OptionsMenu;

import game.UI.Options.OptionsManager;
import game.UI.Options.Text.OptionsTitle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

import javax.swing.text.html.Option;

public class OptionsTitlePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        OptionsTitle optionsTitle = GameObjectManager.instantiate("OptionsTitle")
                .addComponent(OptionsTitle.class);

        OptionsManager.getInstance().linkOptionsTitle(optionsTitle);

        return optionsTitle.getGameObject();
    }
}
