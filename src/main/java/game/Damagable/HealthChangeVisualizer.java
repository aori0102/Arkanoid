package game.Damagable;

import game.Entity.EntityHealth;
import org.Event.EventActionID;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import utils.MathUtils;
import utils.Random;
import utils.Vector2;

public final class HealthChangeVisualizer extends MonoBehaviour {

    private static final double MAX_POP_UP_ANGLE = 34.0;

    private EntityHealth entityHealth = null;

    private EventActionID entityHealth_onHealthChanged_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public HealthChangeVisualizer(GameObject owner) {
        super(owner);
    }

    @Override
    public void onDestroy() {
        entityHealth.onHealthChanged
                .removeListener(entityHealth_onHealthChanged_ID);
    }

    /**
     * Links a component that inherits {@link EntityHealth} to handle pop up UI on health changes.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link HealthChangePopUpUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param entityHealth The component that inherits {@link EntityHealth} to link.
     */
    public void linkEntityHealth(EntityHealth entityHealth) {
        this.entityHealth = entityHealth;
        entityHealth_onHealthChanged_ID = entityHealth.onHealthChanged
                .addListener(this::entityHealth_onHealthChanged);
    }

    /**
     * Called when {@link EntityHealth#onHealthChanged} is invoked.<br><br>
     * This function display a small number pop up when an entity's health changes.
     *
     * @param sender Event caller {@link EntityHealth}.
     * @param e      Event argument containing information of the entity's change of health.
     */
    private void entityHealth_onHealthChanged(Object sender, EntityHealth.OnHealthChangedEventArgs e) {

        // Instantiate
        var popUp = PrefabManager.instantiatePrefab(PrefabIndex.HealthChange_PopUp)
                .getComponent(HealthChangePopUpUI.class);
        popUp.setAmount(e.delta);
        popUp.setHealthAlterType(e.alterType);

        // Set position and shooting direction
        double popUpAngle = MathUtils.deg2rad(Random.range(-MAX_POP_UP_ANGLE, MAX_POP_UP_ANGLE));
        Vector2 direction = Vector2.up().rotateBy(popUpAngle);
        popUp.setDirection(direction);
        popUp.getTransform().setGlobalPosition(e.position);

    }

}