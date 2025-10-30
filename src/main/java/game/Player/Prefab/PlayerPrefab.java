package game.Player.Prefab;

import game.Player.*;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

/**
 * Prefab for the main Player object. Contains multiple core functioning
 * classes.
 * <ul>
 *     <li>{@link Player}</li>
 *     <li>{@link PlayerSkillsHandler}</li>
 *     <li>{@link PlayerPowerUpHandler}</li>
 *     <li>{@link PlayerPaddle}</li>
 *     <li>{@link PlayerHealth}</li>
 * </ul>
 */
public class PlayerPrefab implements IPlayerPrefab {

    @Override
    public GameObject instantiatePrefab() {

        var playerObject = GameObjectManager.instantiate("Player")
                .addComponent(Player.class)
                .addComponent(PlayerSkillsHandler.class)
                .addComponent(PlayerPowerUpHandler.class)
                .addComponent(PlayerHealth.class)
                .getGameObject();

        var player = playerObject.getComponent(Player.class);

        // Health bar
        var healthBarObject = new PlayerHealthBarPrefab().instantiatePrefab();
        healthBarObject.setParent(playerObject);

        // Health loss vignette
        var healthLossVignetteObject = new PlayerHealthLossVignettePrefab().instantiatePrefab();
        var healthLossVignette = healthLossVignetteObject.getComponent(PlayerHealthLossVignette.class);
        healthLossVignette.linkPlayer(player);
        healthLossVignetteObject.setParent(playerObject);

        //Player paddle
        var paddle = new PaddlePrefab().instantiatePrefab();
        player.linkPlayerPaddle(paddle.getComponent(PlayerPaddle.class));

        return playerObject;
    }

}