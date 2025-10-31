package game;

import game.GameManager.GameManager;
import game.Perks.Index.PerkManager;
import game.UI.Buttons.*;
import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuBackground;
import game.UI.MainMenu.MainMenuController;
import game.UI.UIManager;
import org.Main;
import game.GameManager.GameManager;
import game.GameManager.Score.ScoreManagerPrefab;
import game.GameObject.BallsManager;
import game.GameObject.Border.Border;
import game.GameObject.Border.BorderType;
import game.GameObject.Shield;
import game.MapGenerator.BrickMapManager;
import game.Obstacle.Index.ObstacleManager;
import game.Player.Prefab.PlayerPrefab;
import game.PowerUp.Index.PowerUpManager;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import org.Scene.SceneKey;
import org.Scene.SceneManager;
import utils.Vector2;
import org.GameObject.GameObjectManager;

public class Init {

    private static final double BUTTON_OFFSET = 300;

    public static void initManager(){
        GameObjectManager.instantiate("PerkManager").addComponent(PerkManager.class);
        GameObjectManager.instantiate("UIManager").addComponent(UIManager.class);
        GameObjectManager.instantiate("GameManager").addComponent(GameManager.class);
    }
    public static void Init_Kine() {
        var shield = GameObjectManager.instantiate("Shield").addComponent(Shield.class);
    }

    public static void Init_Dui() {
//        PerkManager.getInstance().instantiatePerks();

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        //VoltraxisPrefab.instantiate();

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

    public static void initRecord() {

    }

}
