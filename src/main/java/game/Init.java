package game;

import game.Brick.Brick;
import game.Brick.BrickEvent.BrickManager;
import game.Brick.BrickFactory;
import game.GameObject.BallsManager;
import game.Obstacle.Object.Laser;
import game.Obstacle.Index.ObstacleManager;
import game.Perks.Object.CooldownPerk;
import game.Player.PlayerSkillsHandler;
import game.PowerUp.BlizzardBall;
import game.PowerUp.FireBall;
import game.PowerUp.Index.PowerUpManager;
import game.PowerUp.DuplicateBall;
import game.PowerUp.TriplicateBall;
import game.UI.StartButton;
import game.Voltraxis.VoltraxisPrefab;
import game.GameObject.Arrow;
import game.GameObject.Ball;
import game.GameObject.Paddle;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import org.InputAction.ActionMap;
import org.InputAction.PlayerInput;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;
import org.GameObject.GameObjectManager;

public class Init {

    public static void Init_Kine() {

        var actionMap = GameObjectManager.instantiate("ActionMap");
        actionMap.addComponent(ActionMap.class);

        var paddle = GameObjectManager.instantiate("paddle");
        GameObjectManager.instantiate("paddle");
        paddle.addComponent(Paddle.class);
        paddle.getComponent(Paddle.class).linkActionMap(actionMap.getComponent(ActionMap.class));
        paddle.getComponent(Paddle.class).linkPlayerInput(actionMap.getComponent(PlayerInput.class));
        paddle.addComponent(BoxCollider.class);
        paddle.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        paddle.getTransform().setGlobalPosition(new Vector2(600, 700));

        var paddleVisual = GameObjectManager.instantiate("paddleVisual");
        paddleVisual.setParent(paddle);
        paddleVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Paddle.getImage());
        paddleVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        var ball = GameObjectManager.instantiate("ball");
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getComponent(Ball.class).setPaddle(paddle.getComponent(Paddle.class));
        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.setParent(ball);
        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        var laser = GameObjectManager.instantiate("laser");
        laser.addComponent(BoxCollider.class);
        laser.addComponent(Laser.class);
        laser.getComponent(Laser.class).getGameObject().setActive(false);
        laser.getTransform().setGlobalPosition(new Vector2(300, 300));

        var laserVisual = GameObjectManager.instantiate("laserVisual");
        laserVisual.setParent(laser);
        laserVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Laser.getImage());
        laserVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

        var arrow = GameObjectManager.instantiate("arrow");
        arrow.addComponent(Arrow.class);
        arrow.setParent(paddle);
        arrow.getTransform().setLocalPosition(new Vector2(0, 0));
        arrow.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Arrow.getImage());
        arrow.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
        paddle.getComponent(Paddle.class).linkArrow(arrow.getComponent(Arrow.class));

        var brick = GameObjectManager.instantiate("brick");
        brick.addComponent(Brick.class);
        brick.getTransform().setGlobalPosition(new Vector2(300, 300));
        brick.getTransform().setGlobalScale(new Vector2(2, 2));
        brick.getComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.GreenBrick.getImage());

        var duplicateBall = GameObjectManager.instantiate("duplicateBall");
        duplicateBall.addComponent(DuplicateBall.class);
        duplicateBall.getTransform().setGlobalPosition(new Vector2(100, 100));
        duplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        var triplicateBall = GameObjectManager.instantiate("triplicateBall");
        triplicateBall.addComponent(TriplicateBall.class);
        triplicateBall.getTransform().setGlobalPosition(new Vector2(300, 0));
        triplicateBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        var fireBall = GameObjectManager.instantiate("fireBall");
        fireBall.addComponent(FireBall.class);
        fireBall.getTransform().setGlobalPosition(new Vector2(400, 100));
        fireBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));

        var blizzardBall = GameObjectManager.instantiate("blizzardBall");
        blizzardBall.addComponent(BlizzardBall.class);
        blizzardBall.getTransform().setGlobalPosition(new Vector2(500, 100));
        blizzardBall.getTransform().setGlobalScale(new Vector2(0.5, 0.5));


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
        PowerUpManager.instance.addPowerUp(fireBall.getComponent(FireBall.class));
        PowerUpManager.instance.addPowerUp(blizzardBall.getComponent(BlizzardBall.class));

        var playerSkillsHandler = GameObjectManager.instantiate("PlayerSkillsHandler");
        playerSkillsHandler.addComponent(PlayerSkillsHandler.class).linkPaddle(paddle.getComponent(Paddle.class));
        playerSkillsHandler.getComponent(PlayerSkillsHandler.class).linkActionMap(actionMap.getComponent(ActionMap.class));


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

//        var healthPerk = GameObjectManager.instantiate("healthPerk");
//        healthPerk.addComponent(HealthPerk.class);
//        healthPerk.getTransform().setGlobalPosition(new Vector2(400, 400));
//
         /*
        var coolDownPerk = GameObjectManager.instantiate("coolDownPerk");
        coolDownPerk.addComponent(CooldownPerk.class);
        coolDownPerk.getTransform().setGlobalPosition(new Vector2(300, 300));


        var startButton = GameObjectManager.instantiate("StartButton");
        startButton.addComponent(StartButton.class);
        startButton.getTransform().setGlobalPosition(new Vector2(100,100));
         */
    }

    public static void Init_Duc() {
    }

    public static void Init_Aori() {

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

    public static void initMenu() {

    }

    public static void initGame() {

    }

    public static void initRecord() {

    }

}
