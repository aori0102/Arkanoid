package game.Player.Prefab;

import game.Player.*;
import game.Player.Paddle.PlayerPaddle;
import game.PlayerSkills.SkillIndex;
import game.PlayerSkills.Skills.DashSkill;
import game.PlayerSkills.Skills.InvincibleSkill;
import game.PlayerSkills.Skills.LaserBeamSkill;
import game.PlayerSkills.Skills.UpdraftSkill;
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

        // Player paddle
        var paddle = PrefabManager.instantiatePrefab(PrefabIndex.Player_Paddle);
        paddle.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        player.linkPlayerPaddle(paddle.getComponent(PlayerPaddle.class));

        instantiateSkillKit(playerObject.getComponent(PlayerSkillsHandler.class))
                .setParent(playerObject);

        return playerObject;
    }

    private GameObject instantiateSkillKit(PlayerSkillsHandler playerSkillsHandler) {

        var skillKitObject = GameObjectManager.instantiate("SkillKit");

        // Dash
        var dash = GameObjectManager.instantiate("Dash")
                .addComponent(DashSkill.class);
        playerSkillsHandler.assignSkill(SkillIndex.Dash, dash);
        dash.getGameObject().setParent(skillKitObject);

        // Updraft
        var updraft = GameObjectManager.instantiate("Updraft")
                .addComponent(UpdraftSkill.class);
        playerSkillsHandler.assignSkill(SkillIndex.Updraft, updraft);
        updraft.getGameObject().setParent(skillKitObject);

        // Invincible
        var invincible = GameObjectManager.instantiate("Invincible")
                .addComponent(InvincibleSkill.class);
        playerSkillsHandler.assignSkill(SkillIndex.Invincible, invincible);
        invincible.getGameObject().setParent(skillKitObject);

        // Laser beam
        var laserBeam = GameObjectManager.instantiate("LaserBeam")
                .addComponent(LaserBeamSkill.class);
        playerSkillsHandler.assignSkill(SkillIndex.LaserBeam, laserBeam);
        laserBeam.getGameObject().setParent(skillKitObject);

        return skillKitObject;

    }

}