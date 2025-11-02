package org.Scene.SceneBuilder;

import game.GameManager.GameManager;
import game.GameManager.Score.ScoreManagerPrefab;
import game.GameObject.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.GameObject.Shield;
import game.MapGenerator.BrickMapManager;
import game.Obstacle.Index.ObstacleManager;
import game.Perks.Index.PerkManager;
import game.Player.Prefab.PlayerPrefab;
import game.PowerUp.Index.PowerUpManager;
import game.UI.UIManager;
import org.GameObject.GameObjectManager;
import utils.Vector2;

public final class InGameSceneBuilder extends SceneBuilder {

    @Override
    protected void build() {
        var shield = GameObjectManager.instantiate("Shield").addComponent(Shield.class);

        GameObjectManager.instantiate("PerkManager").addComponent(PerkManager.class);
        GameObjectManager.instantiate("UIManager").addComponent(UIManager.class);
        GameObjectManager.instantiate("GameManager").addComponent(GameManager.class);
        GameObjectManager.instantiate("BrickMapManager").addComponent(BrickMapManager.class);
        ScoreManagerPrefab.instantiate();

        new PlayerPrefab().instantiatePrefab();

        var ballsManager = GameObjectManager.instantiate("ballManager");
        ballsManager.addComponent(BallsManager.class);
        BallsManager.getInstance().spawnInitialBall();

        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
        obstacleManager.addComponent(ObstacleManager.class);

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);

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

    }

}
