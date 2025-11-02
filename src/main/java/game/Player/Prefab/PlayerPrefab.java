package game.Player.Prefab;

import game.Player.*;
import game.Player.Paddle.PlayerPaddle;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

/**
 * Prefab for the main Player object. Contains multiple core functioning
 * classes.
 * <ul>
 *     <li>{@link Player}</li>
 *     <li>{@link PlayerSkillsHandler}</li>
 *     <li>{@link PlayerPowerUpHandler}</li>
 *     <li>{@link PlayerPaddle}</li>
 *     <li>{@link PlayerLives}</li>
 * </ul>
 */
public class PlayerPrefab extends Prefab {

    @Override
    public GameObject instantiatePrefab() {

        var playerObject = GameObjectManager.instantiate("Player")
                .addComponent(Player.class)
                .addComponent(PlayerSkillsHandler.class)
                .addComponent(PlayerPowerUpHandler.class)
                .addComponent(PlayerLives.class)
                .getGameObject();

        var player = playerObject.getComponent(Player.class);

        // Health bar
        var healthBarObject = PrefabManager.instantiatePrefab(PrefabIndex.Player_HealthBar);
        healthBarObject.setParent(playerObject);

        // Health loss vignette
        var healthLossVignetteObject = PrefabManager.instantiatePrefab(PrefabIndex.Player_HealthLossVignette);
        healthLossVignetteObject.setParent(playerObject);

        //Player paddle
        var paddle = PrefabManager.instantiatePrefab(PrefabIndex.Player_Paddle);
        paddle.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        player.linkPlayerPaddle(paddle.getComponent(PlayerPaddle.class));

        return playerObject;
    }

}