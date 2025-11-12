package game.Voltraxis;

import game.Entity.EntityHealth;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public class VoltraxisSFX extends MonoBehaviour {
    private static VoltraxisSFX instance;
    private EventActionID sfx_onChargingEntered = null;
    private EventActionID sfx_onEachBallShot = null;
    private EventActionID sfx_onUnleashingUltimate = null;
    private EventActionID sfx_onChargingTerminated = null;
    private EventActionID sfx_onHealthChanged = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisSFX(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("VoltraxisSFX already initialized");
        }

        instance = this;
    }

    @Override
    public void start() {
        sfx_onChargingEntered = Voltraxis.getInstance().getVoltraxisCharging()
                .onChargingEntered.addListener(this::sfx_onChargingEntered);
        sfx_onEachBallShot = Voltraxis.getInstance().getVoltraxisNormalAttackBrain()
                .onEachBallShot.addListener(this::sfx_onEachBallShot);
        sfx_onUnleashingUltimate = Voltraxis.getInstance().getVoltraxisCharging()
                .onUnleashingLaser.addListener(this::sfx_onUnleashingUltimate);
        sfx_onChargingTerminated = Voltraxis.getInstance().getVoltraxisCharging()
                .onChargingTerminated.addListener(this::sfx_onChargingTerminated);
        sfx_onHealthChanged = Voltraxis.getInstance().getVoltraxisHealth()
                .onHealthChanged.addListener(this::setSfx_onHealthChanged);

    }

    public VoltraxisSFX getInstance() {
        return instance;
    }

    @Override
    protected void onDestroy() {
        Voltraxis.getInstance().getVoltraxisCharging()
                .onChargingEntered.removeListener(sfx_onChargingEntered);
        Voltraxis.getInstance().getVoltraxisNormalAttackBrain()
                .onEachBallShot.removeListener(sfx_onEachBallShot);
        Voltraxis.getInstance().getVoltraxisCharging()
                .onUnleashingLaser.removeListener(sfx_onUnleashingUltimate);
        Voltraxis.getInstance().getVoltraxisCharging()
                .onChargingTerminated.removeListener(sfx_onChargingTerminated);
        Voltraxis.getInstance().getVoltraxisHealth()
                .onHealthChanged.removeListener(sfx_onHealthChanged);

        instance = null;
    }

    private void sfx_onChargingEntered(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.BossCharging);
    }

    private void sfx_onEachBallShot(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.BossNormalAttack);
    }

    private void sfx_onUnleashingUltimate(Object sender, Void e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.BossUltimate);
    }

    private void sfx_onChargingTerminated(Object sender, Void e) {
        AudioManager.stopSFX(SFXAsset.SFXIndex.BossCharging);
    }

    private void setSfx_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {
        AudioManager.playSFX(SFXAsset.SFXIndex.OnBrickHit);
    }


}
