package game.Perks.Index.Prefab;

import game.Perks.Index.Perk;
import game.Perks.Object.AttackPerk;
import game.Perks.Object.CooldownPerk;
import game.Perks.Object.HealthPerk;
import game.Perks.Object.SpeedPerk;
import org.GameObject.GameObjectManager;
import utils.Random;

import java.util.ArrayList;
import java.util.List;

public final class PerkPrefabGenerator {

    public static final ArrayList<Class<? extends Perk>> perks = new ArrayList<>();

    static {
        perks.add(HealthPerk.class);
        perks.add(AttackPerk.class);
        perks.add(CooldownPerk.class);
        perks.add(SpeedPerk.class);
    }

    /**
     * Generate {@code amount} distinct perks.
     *
     * @param amount The number of perk to generate.
     * @return An array consisting all generated perks.
     */
    public static List<Perk> generateRandomPerks(int amount) {

        if (amount > perks.size()) {
            throw new IllegalArgumentException("Amount cannot exceed unique perk count.");
        }

        List<Perk> generatedPerkArray = new ArrayList<>();
        var perkClassList = Random.shuffle(perks);
        for (int i = 0; i < amount; i++) {
            var perkClass = perkClassList.get(i);
            var perkGameObject = GameObjectManager.instantiate("Perk");
            generatedPerkArray.add(perkGameObject.addComponent(perkClass));
        }
        return generatedPerkArray;

    }

}