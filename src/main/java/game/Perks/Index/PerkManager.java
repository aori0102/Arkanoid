package game.Perks.Index;

import game.Perks.Object.AttackPerk;
import game.Perks.Object.CooldownPerk;
import game.Perks.Object.HealthPerk;
import game.Perks.Object.SpeedPerk;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Random;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;

public class PerkManager extends MonoBehaviour {

    public static PerkManager instance;
    public EventHandler<Perk> OnPerkChosen;

    private final ArrayList<Class<? extends Perk>> perks = new ArrayList<>();
    private final ArrayList<GameObject> onScreenPerks = new ArrayList<>();

    private final ArrayList<Vector2> perkPositions = new ArrayList<>();
    private final HashSet<Integer> randomPerksPick = new HashSet<>();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PerkManager(GameObject owner) {
        super(owner);
        if(instance == null) {
            instance = this;
        }

        perks.add(HealthPerk.class);
        perks.add(AttackPerk.class);
        perks.add(CooldownPerk.class);
        perks.add(SpeedPerk.class);

        perkPositions.add(new Vector2(0, 300));
        perkPositions.add(new Vector2(250, 300));
        perkPositions.add(new Vector2(500, 300));
    }

    public void instantiatePerks() {
        for(int i  = 0; i < 3; i++){
            var perkGameObject = GameObjectManager.instantiate("PerkGameObject"+i);
            int random = Random.range(0, perks.size() - 1);
            var randomPerk = perks.get(random);
            perkGameObject.addComponent(randomPerk);
            perkGameObject.getTransform().setGlobalPosition(perkPositions.get(i));
        }
    }


    @Override
    protected void destroyComponent() {

    }
}
