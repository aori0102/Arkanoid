package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Random;
import utils.Time;
import utils.Vector2;

import java.util.LinkedList;
import java.util.function.Function;

public final class VoltraxisEffectManager extends MonoBehaviour {

    private static final Vector2 ICON_OFFSET = new Vector2(30.0, 0.0);

    private double attackMultiplier = 1.0;
    private double defenseMultiplier = 1.0;
    private double basicCooldownMultiplier = 1.0;
    private double damageTakenProportion = 1.0;

    public record EffectInfo(
            VoltraxisData.EffectIndex index,
            double value,
            Function<Double, Boolean> skillEndConstraint
    ) {
    }

    private record EffectUILinker(
            double skillStartTick,
            EffectInfo info,
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

    @Override
    public void update() {

        var iterator = effectUILinkerList.listIterator();
        while (iterator.hasNext()) {

            var uiLinker = iterator.next();
            var index = iterator.nextIndex();

            if (uiLinker.info.skillEndConstraint.apply(Time.time - uiLinker.skillStartTick)) {

                iterator.remove();
                GameObjectManager.destroy(uiLinker.icon.getGameObject());

                switch (uiLinker.info.index) {

                    case VoltraxisData.EffectIndex.AttackIncrement:
                        attackMultiplier -= uiLinker.info.value;
                        break;

                    case VoltraxisData.EffectIndex.DefenceReduction:
                        defenseMultiplier += uiLinker.info.value;
                        break;

                    case VoltraxisData.EffectIndex.DamageTakenDecrement:
                        damageTakenProportion += uiLinker.info.value;
                        break;

                    case VoltraxisData.EffectIndex.DamageTakenIncrement:
                        damageTakenProportion -= uiLinker.info.value;
                        break;

                    case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                        basicCooldownMultiplier += uiLinker.info.value;
                        break;

                }
            } else {
                uiLinker.icon.setTargetPosition(ICON_OFFSET.multiply(index));
            }

        }

    }

    public void addEffect(EffectInfo info) {

        switch (info.index) {

            case VoltraxisData.EffectIndex.AttackIncrement:
                attackMultiplier += info.value;
                break;

            case VoltraxisData.EffectIndex.DefenceReduction:
                defenseMultiplier -= info.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenDecrement:
                damageTakenProportion -= info.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenProportion += info.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier -= info.value;
                break;

        }

        var iconUI = VoltraxisPrefab.instantiateVoltraxisEffectIcon(info.index);
        iconUI.getGameObject().setParent(getGameObject());
        iconUI.setEntry(ICON_OFFSET.multiply(effectUILinkerList.size()));
        var uiLinker = new EffectUILinker(Time.time, info, iconUI);
        effectUILinkerList.add(uiLinker);

    }

    private void onEffectReachingRemoval(EffectUILinker uiLinker) {
        uiLinker.icon.setPulse();
    }

    private void onEffectSpawned(Object sender, EffectInfo e) {
        VoltraxisData.EffectIndex index = switch (Random.Range(0, 4)) {
            case 0 -> VoltraxisData.EffectIndex.PowerCore;
            case 1 -> VoltraxisData.EffectIndex.AttackIncrement;
            case 2 -> VoltraxisData.EffectIndex.ChargingEX;
            case 3 -> VoltraxisData.EffectIndex.DefenceReduction;
            default -> null;
        };
        addEffect(e);
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public double getDamageTakenProportion() {
        return damageTakenProportion;
    }

    public double getBasicCooldownMultiplier() {
        return basicCooldownMultiplier;
    }

}
