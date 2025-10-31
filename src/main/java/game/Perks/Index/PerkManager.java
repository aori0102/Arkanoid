package game.Perks.Index;

import game.Perks.Index.Prefab.PerkPrefabGenerator;
import javafx.scene.input.MouseEvent;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import utils.Random;
import utils.Vector2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PerkManager extends MonoBehaviour {

    private static PerkManager instance;
    public EventHandler<Void> OnPerkChosen = new EventHandler<Void>(PerkManager.class);

    private static final int MAX_PERK_PER_GENERATION = 3;

    private final ArrayList<Vector2> perkPositions = new ArrayList<>();
    private List<Perk> currentPerksOnScreen = new ArrayList<>();

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

        perkPositions.add(new Vector2(Main.STAGE_WIDTH/2 - 250, Main.STAGE_HEIGHT/2));
        perkPositions.add(new Vector2(Main.STAGE_WIDTH/2, Main.STAGE_HEIGHT/2));
        perkPositions.add(new Vector2(Main.STAGE_WIDTH/2 + 250, Main.STAGE_HEIGHT/2));
    }

    @Override
    public void awake() {
        OnPerkChosen.addListener(this::perkManager_OnPerkChosen);
    }

    public void instantiatePerks() {
        currentPerksOnScreen = PerkPrefabGenerator.generateRandomPerks(MAX_PERK_PER_GENERATION);
        for (int i = 0; i < MAX_PERK_PER_GENERATION; i++) {
            currentPerksOnScreen.get(i).getTransform().setGlobalPosition(perkPositions.get(i));
            currentPerksOnScreen.get(i).onPointerClicked.addListener(this::perkManager_OnPointerClickedPerk);
        }
    }

    public void perkManager_OnPerkChosen(Object sender, Void e ){
        destroyPerks();
    }

    public void perkManager_OnPointerClickedPerk(Object sender, MouseEvent e ){
        OnPerkChosen.invoke(this, null);
    }

    private void destroyPerks() {
        for(Perk perk: currentPerksOnScreen){
            GameObjectManager.destroy(perk.getGameObject());
        }

         currentPerksOnScreen.clear();
    }

}
