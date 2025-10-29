package game.Voltraxis;

import game.Voltraxis.Interface.ITakePlayerDamage;
import game.Voltraxis.Object.PowerCore;
import game.Voltraxis.Prefab.VoltraxisPrefab;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Time;

/**
 * Main class of the Voltraxis boss. Handle central logic
 * (damage, health, status...) and skills.
 */
public class Voltraxis extends MonoBehaviour implements ITakePlayerDamage {

    private VoltraxisEffectManager voltraxisEffectManager = null;
    private VoltraxisPowerCoreManager voltraxisPowerCoreManager = null;
    private VoltraxisGroggy voltraxisGroggy = null;
    private VoltraxisCharging voltraxisCharging = null;
    private VoltraxisNormalAttackBrain voltraxisNormalAttackBrain = null;
    private VoltraxisSkillHandler voltraxisSkillHandler = null;
    private VoltraxisStatManager voltraxisStatManager = null;

    private Time.CoroutineID chargingCoroutineID = null;
    private int health = VoltraxisData.BASE_MAX_HEALTH;

    private static Voltraxis instance = null;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(Voltraxis.class);
    public EventHandler<Void> onDamaged = new EventHandler<>(Voltraxis.class);

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
        voltraxisStatManager = getComponent(VoltraxisStatManager.class);
    }

    @Override
    protected void onDestroy() {
        // TODO: look through the coroutine
        chargingCoroutineID = null;

        instance = null;
    }

    @Override
    public void takeDamage(int amount) {

        int finalDamage = (int) (amount * ((double) voltraxisStatManager.getDefence() / (voltraxisStatManager.getDefence() + VoltraxisData.DEFENSE_STRENGTH_SCALE)));
        health -= amount;
        if (health <= 0) {
            health = 0;
            System.out.println("Boss ded");
        }

        onHealthChanged.invoke(this, null);
        onDamaged.invoke(this, null);

    }

    /**
     * Get Voltraxis' current HP.
     *
     * @return Voltraxis' currentHP.
     */
    public int getHealth() {
        return health;
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

    public VoltraxisStatManager getVoltraxisStatManager() {
        return voltraxisStatManager;
    }

    public void inflictEffect(VoltraxisEffectManager.EffectInputInfo info) {
        voltraxisEffectManager.addEffect(info);
    }

}