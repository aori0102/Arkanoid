package game.Voltraxis.Prefab;

import game.Voltraxis.*;
import game.Voltraxis.Object.*;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Physics.BoxCollider;
import utils.Vector2;

/**
 * Prefab of the main object Voltraxis.
 */
public final class VoltraxisPrefab implements IVoltraxisPrefab {

    private static final Vector2 BOSS_POSITION = new Vector2(600.0, 300.0);
    private static final Vector2 BOSS_COLLIDER_SIZE = new Vector2(230.0, 230.0);

    @Override
    public GameObject instantiatePrefab() {

        var bossObject = GameObjectManager.instantiate("Boss");

        var voltraxis = GameObjectManager.instantiate("Voltraxis")
                .addComponent(Voltraxis.class)
                .addComponent(VoltraxisCharging.class)
                .addComponent(VoltraxisEffectManager.class)
                .addComponent(VoltraxisGroggy.class)
                .addComponent(VoltraxisNormalAttackBrain.class)
                .addComponent(VoltraxisSkillHandler.class)
                .addComponent(VoltraxisStatManager.class)
                .addComponent(VoltraxisPowerCoreManager.class)
                .addComponent(BoxCollider.class)
                .addComponent(VoltraxisDamageAcceptor.class)
                .addComponent(VoltraxisHealth.class)
                .getGameObject();
        voltraxis.setParent(bossObject);

        // Collider
        voltraxis.getComponent(BoxCollider.class).setLocalSize(BOSS_COLLIDER_SIZE);

        // Visual
        var visual = new VoltraxisVisualPrefab().instantiatePrefab();
        visual.setParent(voltraxis);

        // Groggy UI
        var groggyUI = new GroggyUIPrefab().instantiatePrefab();
        groggyUI.setParent(bossObject);

        // Charging UI
        var chargingUI = new ChargingUIPrefab().instantiatePrefab();
        chargingUI.setParent(bossObject);

        // Effect bar UI
        var effectBarUI = new EffectBarUIPrefab().instantiatePrefab();
        effectBarUI.setParent(bossObject);

        // Health bar UI
        var healthBarUI = new HealthBarUIPrefab().instantiatePrefab();
        healthBarUI.setParent(bossObject);

        // Starting position
        voltraxis.getTransform().setGlobalPosition(BOSS_POSITION);

        return voltraxis;

    }

}