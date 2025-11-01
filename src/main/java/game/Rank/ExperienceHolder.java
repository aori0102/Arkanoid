package game.Rank;

import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

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

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

}