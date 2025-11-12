package game.Player.Prefab;

import game.Player.PlayerStatUI;
import game.Player.PlayerStatUIHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

public final class PlayerStatUIHandlerPrefab extends Prefab {

    private static final Vector2 STAT_STARTING_POSITION = new Vector2(1075.0, 255.0);
    private static final Vector2 STAT_OFFSET = new Vector2(0.0, 50.0);

    private static final String ATTACK_LABEL = "ATK";
    private static final String DEFENSE_LABEL = "DEF";
    private static final String MAX_HEALTH_LABEL = "HP";
    private static final String MOVEMENT_SPEED_LABEL = "SPD";

    @Override
    public GameObject instantiatePrefab() {

        var playerStatUIHandlerObject = GameObjectManager.instantiate("PlayerStatUIHandler")
                .addComponent(PlayerStatUIHandler.class)
                .getGameObject();
        var playerStatUIHandler = playerStatUIHandlerObject.getComponent(PlayerStatUIHandler.class);

        // Attack
        var infoPosition = STAT_STARTING_POSITION;
        var attackStatUI = PrefabManager.instantiatePrefab(PrefabIndex.PlayerStatUI)
                .getComponent(PlayerStatUI.class);
        attackStatUI.setLabel(ATTACK_LABEL);
        attackStatUI.getTransform().setGlobalPosition(infoPosition);
        attackStatUI.getGameObject().setParent(playerStatUIHandlerObject);

        // Defense
        infoPosition = infoPosition.add(STAT_OFFSET);
        var defenseStatUI = PrefabManager.instantiatePrefab(PrefabIndex.PlayerStatUI)
                .getComponent(PlayerStatUI.class);
        defenseStatUI.setLabel(DEFENSE_LABEL);
        defenseStatUI.getTransform().setGlobalPosition(infoPosition);
        defenseStatUI.getGameObject().setParent(playerStatUIHandlerObject);

        // Max health
        infoPosition = infoPosition.add(STAT_OFFSET);
        var maxHealthStatUI = PrefabManager.instantiatePrefab(PrefabIndex.PlayerStatUI)
                .getComponent(PlayerStatUI.class);
        maxHealthStatUI.setLabel(MAX_HEALTH_LABEL);
        maxHealthStatUI.getTransform().setGlobalPosition(infoPosition);
        maxHealthStatUI.getGameObject().setParent(playerStatUIHandlerObject);

        // Movement speed
        infoPosition = infoPosition.add(STAT_OFFSET);
        var movementSpeedStatUI = PrefabManager.instantiatePrefab(PrefabIndex.PlayerStatUI)
                .getComponent(PlayerStatUI.class);
        movementSpeedStatUI.setLabel(MOVEMENT_SPEED_LABEL);
        movementSpeedStatUI.getTransform().setGlobalPosition(infoPosition);
        movementSpeedStatUI.getGameObject().setParent(playerStatUIHandlerObject);

        // Link component
        playerStatUIHandler.linkAttackStatUI(attackStatUI);
        playerStatUIHandler.linkMaxHealthStatUI(maxHealthStatUI);
        playerStatUIHandler.linkDefenseStatUI(defenseStatUI);
        playerStatUIHandler.linkMovementSpeedStat(movementSpeedStatUI);

        return playerStatUIHandlerObject;

    }
}
