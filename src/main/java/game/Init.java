package game;

import game.Brick.Brick;
import game.GameObject.BallsManager;
import game.Obstacle.Object.Laser;
import game.Obstacle.Index.ObstacleManager;
import game.Perks.Index.PerkManager;
import game.Perks.Object.AttackPerk;
import game.Perks.Object.CooldownPerk;
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

    }

    public static void Init_Dui() {

//        var healthPerk = GameObjectManager.instantiate("healthPerk");
//        healthPerk.addComponent(HealthPerk.class);
//        healthPerk.getTransform().setGlobalPosition(new Vector2(400, 400));
//
//
//
            var perkManager = GameObjectManager.instantiate("PerkManager");
            perkManager.addComponent(PerkManager.class);
            PerkManager.instance.instantiatePerks();


//        var renderer = GameObjectManager.instantiate("SpriteRenderer").addComponent(SpriteRenderer.class);
//        renderer.setImage(ImageAsset.ImageIndex.Perks.getImage());
//        renderer.setSpriteClip(new Vector2(800, 2), new Vector2(396, 576));
//        renderer.setSize(new Vector2(396, 576));

    }

    public static void Init_Duc() {
    }

    public static void Init_Aori() {


    }

    public static void initMenu() {

    }

    public static void initGame() {

    }

    public static void initRecord() {

    }

}
