package game.Voltraxis;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class VoltraxisHealth extends MonoBehaviour {

    private int health = VoltraxisData.BASE_MAX_HEALTH;

    public EventHandler<Void> onHealthChanged = new EventHandler<>(VoltraxisHealth.class);
    public EventHandler<Void> onDamaged = new EventHandler<>(VoltraxisHealth.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisHealth(GameObject owner) {
        super(owner);
    }

    public void damage(int amount) {
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

}