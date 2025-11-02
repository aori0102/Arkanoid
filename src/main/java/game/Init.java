package game;

import game.GameManager.GameManager;
import game.Perks.Index.PerkManager;
import game.UI.Buttons.*;
import game.UI.MainMenu.GameTitle;
import game.UI.MainMenu.MainMenuBackground;
import game.UI.MainMenu.MainMenuController;
import game.UI.UIManager;
import org.Audio.AudioManager;
import org.Main;
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

    public static void initManager() {

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

        SceneManager.loadScene(SceneKey.Menu);
    }

    public static void initGame() {

        SceneManager.loadScene(SceneKey.InGame);

    }

    public static void initRecord() {

    }

}
