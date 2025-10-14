package game.Voltraxis;

import org.GameObject;
import org.GameObjectManager;
import org.MonoBehaviour;
import utils.Random;
import utils.Time;
import utils.Vector2;

import java.util.LinkedList;

public final class VoltraxisEffectManager extends MonoBehaviour {

    private static final double TIME_BEFORE_REMOVAL = 3.2;
    private static final Vector2 ICON_OFFSET = new Vector2(30.0, 0.0);

    private Voltraxis voltraxis = null;

    private record EffectUILinker(
            VoltraxisData.EffectIndex index,
            double value,
            VoltraxisEffectIcon icon
    ) {
    }

    private final LinkedList<EffectUILinker> effectUILinkerList = new LinkedList<>();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public VoltraxisEffectManager(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {
        effectUILinkerList.clear();
    }

    void addEffect(VoltraxisData.EffectIndex index, double value, double duration) {

        var iconUI = VoltraxisPrefab.instantiateVoltraxisEffectIcon(index);
        iconUI.getGameObject().setParent(getGameObject());
        var uiLinker = new EffectUILinker(index, value, iconUI);
        iconUI.setEntry(ICON_OFFSET.multiply(effectUILinkerList.size()));
        effectUILinkerList.add(uiLinker);

        Time.addCoroutine(() -> onEffectReachingRemoval(uiLinker), Time.time + TIME_BEFORE_REMOVAL);
        Time.addCoroutine(() -> removeEffect(uiLinker), Time.time + duration);

    }

    private void onEffectReachingRemoval(EffectUILinker uiLinker) {
        uiLinker.icon.setPulse();
    }

    private void removeEffect(EffectUILinker uiLinker) {

        var temp = effectUILinkerList.listIterator();
        boolean removed = false;
        while (temp.hasNext()) {

            var index = temp.nextIndex();
            var value = temp.next();
            if (removed) {
                value.icon.setTargetPosition(ICON_OFFSET.multiply(index));
            } else if (value == uiLinker) {
                temp.remove();
                removed = true;
                GameObjectManager.destroy(value.icon.getGameObject());
            }

        }

    }

    void setVoltraxis(Voltraxis voltraxis) {
        this.voltraxis = voltraxis;
        voltraxis.onEffectAdded.addListener(this::onEffectSpawned);
    }

    private void onEffectSpawned(Object sender, Void e) {
        VoltraxisData.EffectIndex index = switch (Random.Range(0, 4)) {
            case 0 -> VoltraxisData.EffectIndex.PowerCore;
            case 1 -> VoltraxisData.EffectIndex.AttackIncrement;
            case 2 -> VoltraxisData.EffectIndex.ChargingEX;
            case 3 -> VoltraxisData.EffectIndex.DefenceReduction;
            default -> null;
        };
        addEffect(index, 2.5, Random.Range(3.9, 6.8));
    }

}
