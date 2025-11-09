package org.Scene.SceneBuilder;

import game.UI.Buttons.*;
import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuBackground;
import game.UI.MainMenu.MainMenuController;
import game.UI.MainMenu.MainMenuManager;
import org.Audio.AudioManager;
import org.GameObject.GameObjectManager;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public final class MenuSceneBuilder extends SceneBuilder {


    @Override
    protected void build() {
        GameObjectManager.instantiate("MainMenuManager")
                .addComponent(MainMenuManager.class);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_OptionsManager);

        PrefabManager.instantiatePrefab(PrefabIndex.Manager_GoBackButtonManager);


        var mainMenuBackground = GameObjectManager.instantiate("MainMenuBackground");
        mainMenuBackground.addComponent(MainMenuBackground.class);
        mainMenuBackground.getTransform()
                .setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2));

//        var gameTitle = GameObjectManager.instantiate("GameTitle");
//        gameTitle.addComponent(GameTitle.class);


    }

}
