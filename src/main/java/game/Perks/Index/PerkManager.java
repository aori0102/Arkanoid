package game.Perks.Index;

import game.Perks.Index.Prefab.PerkPrefabGenerator;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import utils.Random;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;

public class PerkManager extends MonoBehaviour {

    private static PerkManager instance;
    public EventHandler<Perk> OnPerkChosen;

    private static final int MAX_PERK_PER_GENERATION = 3;

    private final ArrayList<Vector2> perkPositions = new ArrayList<>();
    private final ArrayList<GameObject> onScreenPerks = new ArrayList<>();

    private final HashSet<Integer> randomPerksPick = new HashSet<>();

    public static PerkManager getInstance() {
        return instance;
    }

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PerkManager(GameObject owner) {
        super(owner);
        if (instance == null) {
            instance = this;
        }

        perkPositions.add(new Vector2(0, 300));
        perkPositions.add(new Vector2(250, 300));
        perkPositions.add(new Vector2(500, 300));
    }

    public void instantiatePerks() {
        var perkList = PerkPrefabGenerator.generateRandomPerks(MAX_PERK_PER_GENERATION);
        for (int i = 0; i < MAX_PERK_PER_GENERATION; i++) {
            perkList.get(i).getTransform().setGlobalPosition(perkPositions.get(i));
        }
    }

}
