package game;

import game.Obstacle.Laser;
import game.Perks.CooldownPerk;
import game.Perks.HealthPerk;
import game.Player.Player;
import game.Player.PlayerPowerUpHandler;
import game.PowerUp.PowerUpManager;
import game.PowerUp.powerUpDrop.DuplicateBall;
import game.PowerUp.powerUpDrop.TriplicateBall;
import game.Voltraxis.VoltraxisHealthBar;
import game.object.Arrow;
import game.object.Ball;
import game.object.Paddle;
import org.*;
import org.w3c.dom.html.HTMLBRElement;
import utils.Vector2;
import game.Voltraxis.VoltraxisPrefab;
import org.GameObjectManager;
import org.UI.ContinueButton;
import org.UI.StartButton;
import utils.Vector2;


import java.sql.Statement;

public class Init {

    public static void Init_Kine() {

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

    }

}
