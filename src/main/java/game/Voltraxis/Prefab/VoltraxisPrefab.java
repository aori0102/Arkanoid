package game.Voltraxis.Prefab;

import game.Voltraxis.*;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Main;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.Vector2;

/**
 * Prefab of the main object Voltraxis.
 */
public final class VoltraxisPrefab extends Prefab {

    private static final Vector2 BOSS_POSITION = new Vector2(Main.STAGE_WIDTH / 2.0, 300.0);
    private static final Vector2 BOSS_COLLIDER_SIZE = new Vector2(230.0, 230.0);

    @Override
    public GameObject instantiatePrefab() {

        var bossObject = GameObjectManager.instantiate("Boss");

        var voltraxisObject = GameObjectManager.instantiate("Voltraxis")
                .addComponent(Voltraxis.class)
                .addComponent(VoltraxisCharging.class)
                .addComponent(VoltraxisEffectManager.class)
                .addComponent(VoltraxisGroggy.class)
                .addComponent(VoltraxisNormalAttackBrain.class)
                .addComponent(VoltraxisSkillHandler.class)
                .addComponent(VoltraxisStat.class)
                .addComponent(VoltraxisPowerCoreManager.class)
                .addComponent(BoxCollider.class)
                .addComponent(VoltraxisHealth.class)
                .getGameObject();
        voltraxisObject.setParent(bossObject);
        voltraxisObject.setLayer(Layer.Boss);
        var voltraxis = voltraxisObject.getComponent(Voltraxis.class);

        // Collider
        voltraxisObject.getComponent(BoxCollider.class).setLocalSize(BOSS_COLLIDER_SIZE);

        // Visual
        var visual = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_Visual);
        visual.setParent(voltraxisObject);

        //SFX
        var sfx = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_SFX);
        sfx.setParent(voltraxis);

        // Groggy UI
        var groggyUI = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_GroggyUI);
        groggyUI.setParent(bossObject);

        // Charging UI
        var chargingUI = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_ChargingUI);
        chargingUI.setParent(bossObject);

        // Effect bar UI
        var effectBarUI = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_EffectBarUI);
        effectBarUI.setParent(bossObject);

        // Health bar UI
        var healthBarUI = PrefabManager.instantiatePrefab(PrefabIndex.Voltraxis_HealthBarUI);
        healthBarUI.setParent(bossObject);

        // Starting position
        voltraxisObject.getTransform().setGlobalPosition(BOSS_POSITION);

        // Link component
        voltraxis.linkBossObject(bossObject);

        return voltraxisObject;

    }

}