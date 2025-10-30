package game.Damagable;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Random;
import utils.Vector2;

public final class DamagePopUpHandler extends MonoBehaviour {

    private static final double MAX_POP_UP_ANGLE = 34.0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DamagePopUpHandler(GameObject owner) {
        super(owner);
    }

    /**
     * Link the component with {@link DamageAcceptor} implemented to show damage
     * based on the event called from {@link DamageAcceptor#onDamageTaken}.
     *
     * @param damageAcceptor The component with {@link DamageAcceptor}.
     */
    public void linkDamageAcceptor(DamageAcceptor damageAcceptor) {
        damageAcceptor.onDamageTaken.addListener(this::damageAcceptor_getOnDamageTakenEvent);
    }

    /**
     * Called when {@link DamageAcceptor#onDamageTaken} is invoked.<br><br>
     * This function spawn a pop-up with the specified information
     *
     * @param sender Event caller, whichever implements {@link DamageAcceptor}.
     * @param e      Event argument containing all damage information.
     */
    private void damageAcceptor_getOnDamageTakenEvent(Object sender, OnDamageTakenEventArgs e) {
        instantiatePopUp(e.damageInfo, e.position);
    }

    private void instantiatePopUp(DamageInfo damageInfo, Vector2 position) {

        double popUpAngle = MathUtils.deg2rad(Random.range(-MAX_POP_UP_ANGLE, MAX_POP_UP_ANGLE));
        Vector2 direction = Vector2.up().rotateBy(popUpAngle);
        var popUp = GameObjectManager.instantiate("DamagePopUpUI")
                .addComponent(TextUI.class)
                .addComponent(DamagePopUpUI.class);
        popUp.setDamage(damageInfo.amount);
        popUp.setDamageType(damageInfo.type);
        popUp.setDirection(direction);
        popUp.getTransform().setGlobalPosition(position);

    }

}