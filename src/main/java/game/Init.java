package game;

import game.Brick.Brick;
import game.Obstacle.Laser;
import game.Perks.CooldownPerk;
import game.PowerUp.PowerUpManager;
import game.PowerUp.powerUpDrop.DuplicateBall;
import game.PowerUp.powerUpDrop.TriplicateBall;
import game.object.Arrow;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Time;
import utils.Vector2;
import org.GameObjectManager;

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
        arrow.setParent(paddle);
        arrow.getTransform().setLocalPosition(new Vector2(0, 0));
        arrow.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Arrow.getImage());
        paddle.getComponent(Paddle.class).linkArrow(arrow.getComponent(Arrow.class));

        var brick = GameObjectManager.instantiate("brick");
        brick.addComponent(Brick.class);
        brick.getTransform().setGlobalPosition(new Vector2(300 , 300));
        brick.getTransform().setGlobalScale(new Vector2(2, 2));
        brick.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.GreenBrick.getImage());

        var duplicateBall = GameObjectManager.instantiate("duplicateBall");
        duplicateBall.addComponent(DuplicateBall.class);
        duplicateBall.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.DuplicateBall.getImage());

        var triplicateBall = GameObjectManager.instantiate("triplicateBall");
        triplicateBall.addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalPosition(new Vector2(300, 200));
        triplicateBall.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.DuplicateBall.getImage());


        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
        obstacleManager.addComponent(ObstacleManager.class);

    }

    public static void Init_Dui() {

//        var healthPerk = GameObjectManager.instantiate("healthPerk");
//        healthPerk.addComponent(HealthPerk.class);
//        healthPerk.getTransform().setGlobalPosition(new Vector2(400, 400));
//
        var coolDownPerk = GameObjectManager.instantiate("coolDownPerk");
        coolDownPerk.addComponent(CooldownPerk.class);
        coolDownPerk.getTransform().setGlobalPosition(new Vector2(300, 300));
//
//        var startButton = GameObjectManager.instantiate("StartButton");
//        startButton.addComponent(StartButton.class);
//        startButton.getTransform().setGlobalPosition(new Vector2(100,100));
    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

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


        var triplicateBall = GameObjectManager.instantiate("triplicateBall");
        triplicateBall.addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalPosition(new Vector2(300, 200));
        triplicateBall.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.DuplicateBall.getImage());
        Time.addCoroutine(()->PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class)),10);
        Time.addCoroutine(()->PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class)),20);
        Time.addCoroutine(()->PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class)),30);
        Time.addCoroutine(()->PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class)),40);
        Time.addCoroutine(()->PowerUpManager.instance.addPowerUp(triplicateBall.getComponent(TriplicateBall.class)),50);

    }

}
