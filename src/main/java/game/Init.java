package game;

import game.GameManager.GameManager;
import game.GameManager.Score.ScoreManager;
import game.GameManager.Score.ScoreManagerPrefab;
import game.GameObject.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.GameObject.Shield;
import game.MapGenerator.BrickMapManager;
import game.Obstacle.Index.ObstacleManager;
import game.Player.Prefab.PlayerPrefab;
import game.PowerUp.Index.PowerUpManager;
import game.GameObject.Ball;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Vector2;
import org.GameObject.GameObjectManager;

public class Init {

    public static void Init_Kine() {
        var shield = GameObjectManager.instantiate("Shield").addComponent(Shield.class);
    }

    public static void Init_Dui() {
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        //VoltraxisPrefab.instantiate();

    }

    public static void initMenu() {

    }

    public static void initGame() {

        SceneManager.loadScene(SceneKey.InGame);

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
        PowerUpManager.getInstance().linkPlayerPowerUp(Player.getInstance().getComponent(PlayerPowerUpHandler.class));

        //new VoltraxisPrefab().instantiatePrefab();

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.addComponent(Border.class).setBorderType(BorderType.BorderLeft);


        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.addComponent(Border.class).setBorderType(BorderType.BorderRight);
        borderRight.getTransform().setLocalPosition(new Vector2(1190.0, 0));

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.addComponent(Border.class).setBorderType(BorderType.BorderTop);
        borderTop.getTransform().setLocalPosition(new Vector2(0, 5.0));

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.addComponent(Border.class).setBorderType(BorderType.BorderBottom);
        borderBottom.getTransform().setLocalPosition(new Vector2(1190, 750));

    }

    public static void initRecord() {

    }

}
