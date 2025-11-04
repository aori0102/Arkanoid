package org.Scene.SceneBuilder;

import game.Ball.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.Obstacle.Index.ObstacleManager;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.Index.PowerUpManager;
import game.UI.Buttons.MenuButton;
import game.UI.Buttons.PauseButton;
import game.UI.Buttons.ResumeButton;
import game.UI.PauseMenu.PauseMenuController;
import game.UI.PauseMenu.PauseMenuManager;
import game.UI.UIManager;
import org.GameObject.GameObjectManager;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public final class InGameSceneBuilder extends SceneBuilder {

    @Override
    protected void build() {

        PrefabManager.instantiatePrefab(PrefabIndex.Manager_UIManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_PerkManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_GameManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_BrickMapManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_RankManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ScoreManager);
        PrefabManager.instantiatePrefab(PrefabIndex.Manager_ObstacleManager);
        ScoreManagerPrefab.instantiate();

        PrefabManager.instantiatePrefab(PrefabIndex.Player);

        PrefabManager.instantiatePrefab(PrefabIndex.PlayerInfoBoard);

        BallsManager.getInstance().spawnInitialBall();

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);
        PowerUpManager.getInstance().linkPlayerPowerUp(Player.getInstance().getComponent(PlayerPowerUpHandler.class));

        //new VoltraxisPrefab().instantiatePrefab();

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.addComponent(Border.class).setBorderType(BorderType.BorderLeft);
        borderLeft.getTransform().setLocalPosition(new Vector2(250.0, 0));

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.addComponent(Border.class).setBorderType(BorderType.BorderRight);
        borderRight.getTransform().setLocalPosition(new Vector2(950.0, 0));

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.addComponent(Border.class).setBorderType(BorderType.BorderTop);
        borderTop.getTransform().setLocalPosition(new Vector2(0, 5.0));

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.addComponent(Border.class).setBorderType(BorderType.BorderBottom);
        borderBottom.getTransform().setLocalPosition(new Vector2(1190, 750));

        var pausebutton = GameObjectManager.instantiate("PauseButton").addComponent(PauseButton.class);
        var resumeButton = GameObjectManager.instantiate("ResumeButton").addComponent(ResumeButton.class);
        var menuButton = GameObjectManager.instantiate("MenuButton").addComponent(MenuButton.class);

        pausebutton.getTransform().setGlobalPosition(new Vector2(50,50));
        resumeButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2,400));
        menuButton.getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2,500));

        GameObjectManager.instantiate("PauseMenuManager").addComponent(PauseMenuManager.class);
        PauseMenuManager.getInstance().addPauseMenuButton(menuButton);
        PauseMenuManager.getInstance().addPauseMenuButton(resumeButton);
        PauseMenuManager.getInstance().addPauseMenuButton(pausebutton);
        GameObjectManager.instantiate("PauseMenuController").addComponent(PauseMenuController.class);

    }

}
