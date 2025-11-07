package game.Brick;

import game.Entity.EntityHealth;
import game.Entity.EntityStat;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;

public final class BrickHealth extends EntityHealth {

    @LinkViaPrefab
    private Brick brick = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickHealth(GameObject owner) {
        super(owner);
    }

    @Override
    protected Class<? extends EntityStat> getStatComponentClass() {
        return BrickStat.class;
    }

    @Override
    public void awake() {
        super.awake();
        onHealthReachesZero.addListener(this::brickHealth_onHealthReachesZero);
    }

    /**
     * Called when {@link BrickHealth#onHealthReachesZero} is invoked.<br><br>
     * This function destroy the brick upon its health reaches zero.
     *
     * @param sender This object.
     * @param e      Empty event argument.
     */
    private void brickHealth_onHealthReachesZero(Object sender, Void e) {
        GameObjectManager.destroy(gameObject);
    }

    /**
     * Link centralized brick object.<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link BrickPrefab}
     * as part of component linking process.</i></b>
     *
     * @param brick The centralized brick class.
     */
    public void linkBrick(Brick brick) {
        this.brick = brick;
    }

}