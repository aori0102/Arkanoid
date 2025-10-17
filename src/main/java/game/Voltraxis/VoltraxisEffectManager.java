package game.Voltraxis;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Time;
import utils.Vector2;

import java.util.LinkedList;
import java.util.function.Function;

/**
 * Central class for controlling effects. It handles visualizing
 * current effects and stat modifier as each effect is applied.
 */
public final class VoltraxisEffectManager extends MonoBehaviour {

    private static final Vector2 ICON_OFFSET = new Vector2(30.0, 0.0);

    private double attackMultiplier = 1.0;
    private double defenseMultiplier = 1.0;
    private double basicCooldownMultiplier = 1.0;
    private double damageTakenProportion = 1.0;

    /**
     * Utility class to hold effect information.
     *
     * @param index                  The index of the effect.<br>
     * @param value                  The value this effect applies.<br>
     * @param effectEndingConstraint The constraint upon which to
     *                               terminate the effect. This is
     *                               a function which accepts a {@code double}
     *                               as the parameter representing the delta time
     *                               from the moment this effect starts and returns
     *                               a {@code bool}. Most common way to use this is
     *                               by {@code (delta) -> return delta > cooldown},
     *                               which means this effect will last for {@code cooldown}
     *                               second(s).
     */
    public record EffectInfo(
            VoltraxisData.EffectIndex index,
            double value,
            Function<Double, Boolean> effectEndingConstraint
    ) {
    }

    /**
     * Linker class that links UI with their info.
     *
     * @param skillStartTick The time when the effect starts.
     * @param info           The overall info of the effect.
     * @param icon           The icon UI of the effect.
     */
    private record EffectUILinker(
            double skillStartTick,
            EffectInfo info,
            VoltraxisEffectIcon icon,
            Runnable effectEndedCallback
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

            if (uiLinker.info.effectEndingConstraint.apply(Time.time - uiLinker.skillStartTick)) {

                modifyStatOnEffectRemoved(uiLinker.info);
                if (uiLinker.effectEndedCallback != null) {
                    uiLinker.effectEndedCallback.run();
                }
                iterator.remove();
                GameObjectManager.destroy(uiLinker.icon.getGameObject());

            } else {
                uiLinker.icon.setTargetPosition(ICON_OFFSET.multiply(index));
            }

        }

    }

    /**
     * Add an effect.<br><br>
     * This function initialize a UI for the respective
     * effect and modifies stats based on the effect.
     *
     * @param info The info of the effect to be added.
     */
    public void addEffect(EffectInfo info, Runnable onEffectEndedCallback) {

        var iconUI = VoltraxisPrefab.instantiateVoltraxisEffectIcon(info.index);
        iconUI.getGameObject().setParent(getGameObject());
        iconUI.setEntry(ICON_OFFSET.multiply(effectUILinkerList.size()));
        var uiLinker = new EffectUILinker(Time.time, info, iconUI, onEffectEndedCallback);
        effectUILinkerList.add(uiLinker);

        modifyStatOnEffectAdded(info);

    }

    /**
     * Add an effect if it's not already there.
     *
     * @param info The info of the effect to be added.
     */
    public void addEffectExclusive(EffectInfo info, Runnable onEffectEndedCallback) {
        if (!hasEffect(info.index)) {
            addEffect(info, onEffectEndedCallback);
        }
    }

    /**
     * Modify Voltraxis' statistic after adding an effect.
     *
     * @param info The effect info that was added.
     */
    private void modifyStatOnEffectAdded(EffectInfo info) {

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

            case VoltraxisData.EffectIndex.Frostbite:
                addEffect(new EffectInfo(
                        VoltraxisData.EffectIndex.DamageTakenIncrement,
                        VoltraxisData.FROST_BITE_DAMAGE_TAKEN_INCREMENT,
                        (_) -> hasEffect(VoltraxisData.EffectIndex.Frostbite)
                ), null);

        }

    }

    /**
     * Modify Voltraxis' statistic after removing an effect.
     *
     * @param info The info of the effect that was removed.
     */
    private void modifyStatOnEffectRemoved(EffectInfo info) {

        switch (info.index) {

            case VoltraxisData.EffectIndex.AttackIncrement:
                attackMultiplier -= info.value;
                break;

            case VoltraxisData.EffectIndex.DefenceReduction:
                defenseMultiplier += info.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenDecrement:
                damageTakenProportion += info.value;
                break;

            case VoltraxisData.EffectIndex.DamageTakenIncrement:
                damageTakenProportion -= info.value;
                break;

            case VoltraxisData.EffectIndex.SkillCooldownDecrement:
                basicCooldownMultiplier += info.value;
                break;

        }

    }

    /**
     * Check whether Voltraxis has the corresponding effect provided.
     *
     * @param index The index of the effect to check for.
     * @return {@code true} if Voltraxis has the effect, otherwise
     * {@code false}.
     */
    public boolean hasEffect(VoltraxisData.EffectIndex index) {
        for (var linker : effectUILinkerList) {
            if (linker.info.index == index) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get the overall ATK after applying all modifiers.
     *
     * @return The overall ATK.
     */
    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    /**
     * Get the overall DEF after applying all modifiers.
     *
     * @return The overall DEF.
     */
    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    /**
     * Get the overall DMG taken proportion after applying
     * all modifiers.
     *
     * @return The overall DMG taken proportion.
     */
    public double getDamageTakenProportion() {
        return damageTakenProportion;
    }

    /**
     * Get the overall cooldown of Voltraxis' basic skill
     * after applying all modifiers.
     *
     * @return The overall basic skill cooldown of Voltraxis.
     */
    public double getBasicCooldownMultiplier() {
        return basicCooldownMultiplier;
    }

}