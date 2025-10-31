package org.Scene.SceneBuilder;

import game.UI.Buttons.*;
import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuBackground;
import game.UI.MainMenu.MainMenuController;
import org.GameObject.GameObjectManager;
import org.Main;
import utils.Vector2;

public final class MenuSceneBuilder extends SceneBuilder {

    private static final double BUTTON_OFFSET = 300;

    @Override
    protected void build() {

        var mainMenuBackground = GameObjectManager.instantiate("MainMenuBackground");
        mainMenuBackground.addComponent(MainMenuBackground.class);
        mainMenuBackground.getTransform()
                .setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2));
        var gameTitle = GameObjectManager.instantiate("GameTitle");
        gameTitle.addComponent(GameTitle.class);
        gameTitle.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, 125));

        var startButton = GameObjectManager.instantiate("StartButton");
        startButton.addComponent(StartButton.class);
        startButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, BUTTON_OFFSET));

        var continueButton = GameObjectManager.instantiate("ContinueButton");
        continueButton.addComponent(ContinueButton.class);
        continueButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 100));

        var recordButton = GameObjectManager.instantiate("RecordButton");
        recordButton.addComponent(RecordButton.class);
        recordButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 200));

        var optionsButton = GameObjectManager.instantiate("OptionsButton");
        optionsButton.addComponent(OptionsButton.class);
        optionsButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 300));

        var quitButton = GameObjectManager.instantiate("QuitButton");
        quitButton.addComponent(QuitButton.class);
        quitButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, BUTTON_OFFSET + 400));

        var mainMenuController = GameObjectManager.instantiate("MainMenuController");
        mainMenuController.addComponent(MainMenuController.class);

    }

}
