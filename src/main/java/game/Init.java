package game;

import game.Obstacle.Laser;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.PowerUpManager;
import game.PowerUp.powerUpDrop.DuplicateBall;
import game.PowerUp.powerUpDrop.TriplicateBall;
import game.object.Arrow;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Vector2;
import game.Voltraxis.VoltraxisPrefab;
import org.GameObjectManager;
import org.UI.ContinueButton;
import org.UI.StartButton;
import utils.Vector2;


import java.sql.Statement;

public class Init {

    public static void Init_Kine() {
        var paddle = GameObjectManager.instantiate("paddle");
        paddle.addComponent(Paddle.class);
        paddle.addComponent(PlayerInput.class);
        paddle.addComponent(ActionMap.class);
        paddle.addComponent(BoxCollider.class);
        paddle.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        paddle.getTransform().setGlobalPosition(new Vector2(600, 550));

        var paddleVisual = GameObjectManager.instantiate("paddleVisual");
        paddleVisual.setParent(paddle);
        paddleVisual.getTransform().setLocalPosition(new Vector2(-47, 0));
        paddleVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Paddle.getImage());

        var ball = GameObjectManager.instantiate("ball");
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getComponent(Ball.class).setPaddle(paddle.getComponent(Paddle.class));
        ball.getTransform().setGlobalPosition(new Vector2(584,530 ));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball);
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());

        var laser = GameObjectManager.instantiate("laser");
        laser.addComponent(BoxCollider.class);
        laser.addComponent(Laser.class);
        laser.getComponent(Laser.class).getGameObject().setActive(false);
        laser.getTransform().setGlobalPosition(new Vector2(300, 300));

        var laserVisual = GameObjectManager.instantiate("laserVisual");
        laserVisual.setParent(laser);
        laserVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Laser.getImage());
        laserVisual.getTransform().setLocalPosition(new Vector2(-15, -45));

        var arrow = GameObjectManager.instantiate("arrow");
        arrow.addComponent(Arrow.class);

        var duplicateBall = GameObjectManager.instantiate("duplicateBall");
        duplicateBall.addComponent(DuplicateBall.class);
        duplicateBall.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.DuplicateBall.getImage());

        var triplicateBall = GameObjectManager.instantiate("triplicateBall");
        triplicateBall.addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalPosition(new Vector2(300, 200));
        triplicateBall.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.DuplicateBall.getImage());


        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
        obstacleManager.addComponent(ObstacleManager.class);


        ObstacleManager.instance.setPaddle(paddle.getComponent(Paddle.class));
        ObstacleManager.instance.addObstacle(laser.getComponent(Laser.class));

        var ballsManager = GameObjectManager.instantiate("ballManager");
        ballsManager.addComponent(BallsManager.class);

        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
        powerUpManager.addComponent(PowerUpManager.class);
        PowerUpManager.instance.addPowerUp(duplicateBall.getComponent(DuplicateBall.class));
        PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class));


        var player = GameObjectManager.instantiate("player");
        player.addComponent(Player.class);

        PowerUpManager.instance.linkPlayerPowerUp(Player.instance.getComponent(PlayerPowerUpHandler.class));
        duplicateBall.getComponent(DuplicateBall.class).linkPlayerPowerUp(Player.instance.getComponent(PlayerPowerUpHandler.class));
        duplicateBall.getComponent(DuplicateBall.class).linkPaddle(paddle.getComponent(Paddle.class));

        triplicateBall.getComponent(TriplicateBall.class).linkPlayerPowerUp(Player.instance.getComponent(PlayerPowerUpHandler.class));
        triplicateBall.getComponent(TriplicateBall.class).linkPaddle(paddle.getComponent(Paddle.class));

        Player.instance.linkPaddle(paddle.getComponent(Paddle.class));
        BallsManager.instance.addBall(ball.getComponent(Ball.class));
    }

    public static void Init_Dui() {
        var startButton = GameObjectManager.instantiate("startButton");
        startButton.addComponent(StartButton.class);
        startButton.getTransform().setGlobalPosition(new Vector2(200, 200));
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {
        System.out.println("Making instance of prefab");
        VoltraxisPrefab.instantiate();
        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.getTransform().setLocalPosition(new Vector2(5.0, 0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.setParent(borderLeft);
        //borderLeftVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.getTransform().setLocalPosition(new Vector2(1190.0, 0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.setParent(borderRight);
        //borderRightVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.getTransform().setLocalPosition(new Vector2(0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.setParent(borderTop);
        //borderTopVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.getTransform().setLocalPosition(new Vector2(1190, 750));
        borderBottom.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.setParent(borderBottom);
        //borderBottomVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");
    }

}
