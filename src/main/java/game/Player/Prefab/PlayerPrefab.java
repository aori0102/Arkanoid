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

        return GameObjectManager.instantiate("Player")
                .addComponent(Player.class)
                .addComponent(PlayerSkillsHandler.class)
                .addComponent(PlayerPowerUpHandler.class)
                .addComponent(PlayerPaddle.class)
                .addComponent(PlayerHealth.class)
                .getGameObject();

    }

}