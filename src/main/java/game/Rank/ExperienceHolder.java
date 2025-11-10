package game.Rank;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Component for whichever object that releases experience after being destroyed.
 */
public final class ExperienceHolder extends MonoBehaviour {

    private int exp = 0;

    public static EventHandler<Void> onAnyExperienceHolderDestroyed = new EventHandler<>(ExperienceHolder.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public ExperienceHolder(GameObject owner) {
        super(owner);
    }

    @Override
    public void onDestroy() {
        onAnyExperienceHolderDestroyed.invoke(this, null);
    }

    /**
     * Get the amount of EXP this object holds.
     *
     * @return The amount of EXP this object holds
     */
    public int getExp() {
        return exp;
    }

    /**
     * Set the amount of EXP this object holds.
     *
     * @param exp The amount of EXP this object holds.
     */
    public void setExp(int exp) {
        this.exp = exp;
    }

}