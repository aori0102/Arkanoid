package game;

import game.GameObject.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
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
import utils.Vector2;
import org.GameObject.GameObjectManager;

public class Init {

    public static void Init_Kine() {
    }

    public static void Init_Dui() {
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        //VoltraxisPrefab.instantiate();

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

    public static void initMenu() {

    }

    public static void initGame() {
        GameObjectManager.instantiate("BrickMapManager").addComponent(BrickMapManager.class);
        BrickMapManager.getInstance().generateMap();
        new PlayerPrefab().instantiatePrefab();

        var ball = GameObjectManager.instantiate("ball");
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball);
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        var ballsManager = GameObjectManager.instantiate("ballManager");
        ballsManager.addComponent(BallsManager.class);
        BallsManager.instance.addBall(ball.getComponent(Ball.class));

        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
        obstacleManager.addComponent(ObstacleManager.class);

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);
        PowerUpManager.getInstance().linkPlayerPowerUp(Player.getInstance().getComponent(PlayerPowerUpHandler.class));

//        VoltraxisPrefab.instantiate();
    }

    public static void initRecord() {

    }

}
