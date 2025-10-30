package game;

import game.GameManager.GameManager;
import game.Perks.Index.PerkManager;
import game.UI.Buttons.*;
import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuBackground;
import game.UI.MainMenu.MainMenuController;
import game.UI.UIManager;
import org.Main;
import utils.Vector2;
import org.GameObject.GameObjectManager;

public class Init {
    private static final double WIDTH = 1200;
    private static final double BUTTON_OFFSET = 300;

    public static void initManager(){
        GameObjectManager.instantiate("PerkManager").addComponent(PerkManager.class);
        GameObjectManager.instantiate("UIManager").addComponent(UIManager.class);
        GameObjectManager.instantiate("GameManager").addComponent(GameManager.class);
    }
    public static void Init_Kine() {
    }

    public static void Init_Dui() {
//        PerkManager.getInstance().instantiatePerks();

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {


    }

    public static void initMenu() {
        var mainMenuBackground = GameObjectManager.instantiate("MainMenuBackground");
        mainMenuBackground.addComponent(MainMenuBackground.class);
        mainMenuBackground.getTransform()
                .setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2,Main.STAGE_HEIGHT/2));
        var gameTitle = GameObjectManager.instantiate("GameTitle");
        gameTitle.addComponent(GameTitle.class);
        gameTitle.getTransform().setGlobalPosition(new Vector2(WIDTH/2, 125));

        var startButton = GameObjectManager.instantiate("StartButton");
        startButton.addComponent(StartButton.class);
        startButton.getTransform().setGlobalPosition(new Vector2(WIDTH/2,BUTTON_OFFSET));

        var continueButton = GameObjectManager.instantiate("ContinueButton");
        continueButton.addComponent(ContinueButton.class);
        continueButton.getTransform().setGlobalPosition(new Vector2(WIDTH/2,BUTTON_OFFSET + 100));

        var recordButton = GameObjectManager.instantiate("RecordButton");
        recordButton.addComponent(RecordButton.class);
        recordButton.getTransform().setGlobalPosition(new Vector2(WIDTH/2,BUTTON_OFFSET + 200));

        var optionsButton = GameObjectManager.instantiate("OptionsButton");
        optionsButton.addComponent(OptionsButton.class);
        optionsButton.getTransform().setGlobalPosition(new Vector2(WIDTH/2,BUTTON_OFFSET + 300));

        var quitButton = GameObjectManager.instantiate("QuitButton");
        quitButton.addComponent(QuitButton.class);
        quitButton.getTransform().setGlobalPosition(new Vector2(WIDTH/2,BUTTON_OFFSET + 400));

        var mainMenuController = GameObjectManager.instantiate("MainMenuController");
        mainMenuController.addComponent(MainMenuController.class);
    }

    public static void initGame() {
//        GameObjectManager.instantiate("BrickMapManager").addComponent(BrickMapManager.class);
//        BrickMapManager.getInstance().generateMap();
//        new PlayerPrefab().instantiatePrefab();
//
//        var ball = GameObjectManager.instantiate("ball");
//        ball.addComponent(Ball.class);
//        ball.addComponent(BoxCollider.class);
//        ball.getTransform().setGlobalPosition(new Vector2(584, 530));
//        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
//
//        var ballVisual = GameObjectManager.instantiate("ballVisual");
//        ballVisual.setParent(ball);
//        ballVisual.addComponent(SpriteRenderer.class).setImage(ImageAsset.ImageIndex.Ball.getImage());
//        ballVisual.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
//
//        var ballsManager = GameObjectManager.instantiate("ballManager");
//        ballsManager.addComponent(BallsManager.class);
//        BallsManager.instance.addBall(ball.getComponent(Ball.class));
//
//        var obstacleManager = GameObjectManager.instantiate("obstacleManager");
//        obstacleManager.addComponent(ObstacleManager.class);
//
//        var powerUpManager = GameObjectManager.instantiate("powerUpManager");
//        powerUpManager.addComponent(PowerUpManager.class);
//        PowerUpManager.getInstance().linkPlayerPowerUp(Player.getInstance().getComponent(PlayerPowerUpHandler.class));

//        VoltraxisPrefab.instantiate();
    }

    public static void initRecord() {

    }

}
