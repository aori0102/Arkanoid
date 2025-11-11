package game.Voltraxis;

import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Main class of the Voltraxis boss. Handle central logic
 * (damage, health, status...) and skills.
 */
public class Voltraxis extends MonoBehaviour {

    private VoltraxisEffectManager voltraxisEffectManager = null;
    private VoltraxisPowerCoreManager voltraxisPowerCoreManager = null;
    private VoltraxisGroggy voltraxisGroggy = null;
    private VoltraxisCharging voltraxisCharging = null;
    private VoltraxisNormalAttackBrain voltraxisNormalAttackBrain = null;
    private VoltraxisSkillHandler voltraxisSkillHandler = null;
    private VoltraxisStat voltraxisStat = null;
    private VoltraxisHealth voltraxisHealth = null;
    private VoltraxisSFX voltraxisSFX = null;

    public EventHandler<Void> onBossDestroyed = new EventHandler<>(Voltraxis.class);

    private static Voltraxis instance = null;

    public Voltraxis(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("Voltraxis is a singleton!");
        }
        instance = this;
    }

    @Override
    public void awake() {
        voltraxisEffectManager = getComponent(VoltraxisEffectManager.class);
        voltraxisPowerCoreManager = getComponent(VoltraxisPowerCoreManager.class);
        voltraxisGroggy = getComponent(VoltraxisGroggy.class);
        voltraxisCharging = getComponent(VoltraxisCharging.class);
        voltraxisNormalAttackBrain = getComponent(VoltraxisNormalAttackBrain.class);
        voltraxisSkillHandler = getComponent(VoltraxisSkillHandler.class);
        voltraxisStat = getComponent(VoltraxisStat.class);
        voltraxisHealth = getComponent(VoltraxisHealth.class);
    }

    @Override
    protected void onDestroy() {
        onBossDestroyed.invoke(this, null);
        instance = null;
    }

    public VoltraxisHealth getVoltraxisHealth() {
        return voltraxisHealth;
    }

    public static Voltraxis getInstance() {
        return instance;
    }

    public VoltraxisEffectManager getVoltraxisEffectManager() {
        return voltraxisEffectManager;
    }

    public VoltraxisPowerCoreManager getVoltraxisPowerCoreManager() {
        return voltraxisPowerCoreManager;
    }

    public VoltraxisGroggy getVoltraxisGroggy() {
        return voltraxisGroggy;
    }

    public VoltraxisCharging getVoltraxisCharging() {
        return voltraxisCharging;
    }

    public VoltraxisNormalAttackBrain getVoltraxisNormalAttackBrain() {
        return voltraxisNormalAttackBrain;
    }

    public VoltraxisSkillHandler getVoltraxisSkillHandler() {
        return voltraxisSkillHandler;
    }

    public VoltraxisStat getVoltraxisStatManager() {
        return voltraxisStat;
    }

    public void inflictEffect(VoltraxisEffectManager.EffectInputInfo info) {
        voltraxisEffectManager.addEffect(info);
    }

}