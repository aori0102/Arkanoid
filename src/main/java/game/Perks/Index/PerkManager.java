package game.Perks.Index;

import game.Level.LevelManager;
import game.Perks.Index.Prefab.PerkPrefabGenerator;
import game.Rank.RankManager;
import javafx.scene.input.MouseEvent;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class PerkManager extends MonoBehaviour {

    private static final int MAX_PERK_PER_GENERATION = 3;
    private static final double SLIDE_DISTANCE = -Main.STAGE_HEIGHT;
    private static final double SLIDE_DURATION = 1;

    private static PerkManager instance = null;

    private final ArrayList<Vector2> perkPositions = new ArrayList<>();

    private EventActionID levelManager_onPerkSelectionRequested_ID = null;

    private List<Perk> currentPerksOnScreen = new ArrayList<>();

    public EventHandler<Void> onPerkChosen = new EventHandler<>(PerkManager.class);
    public EventHandler<Void> onPerkSelectionCompleted = new EventHandler<>(PerkManager.class);

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
        if (instance != null) {
            throw new ReinitializedSingletonException("PerkManager is a singleton!");
        }
        instance = this;

        perkPositions.add(new Vector2(Main.STAGE_WIDTH / 2 - 250, 3 * Main.STAGE_HEIGHT / 2));
        perkPositions.add(new Vector2(Main.STAGE_WIDTH / 2, 3 * Main.STAGE_HEIGHT / 2));
        perkPositions.add(new Vector2(Main.STAGE_WIDTH / 2 + 250, 3 * Main.STAGE_HEIGHT / 2));
    }

    @Override
    public void awake() {
        onPerkChosen.addListener(this::perkManager_OnPerkChosen);
    }

    @Override
    public void start() {
        levelManager_onPerkSelectionRequested_ID = LevelManager.getInstance().onPerkSelectionRequested
                .addListener(this::levelManager_onPerkSelectionRequested);
    }

    @Override
    public void onDestroy() {
        if (LevelManager.getInstance() != null) {
            LevelManager.getInstance().onPerkSelectionRequested
                    .removeListener(levelManager_onPerkSelectionRequested_ID);
        }
        instance = null;
    }

    /**
     * Called when {@link LevelManager#onPerkSelectionRequested} is invoked.<br><br>
     * This function starts selecting perk.
     *
     * @param sender Event caller {@link LevelManager}.
     * @param e      Empty event argument.
     */
    private void levelManager_onPerkSelectionRequested(Object sender, Void e) {
        instantiatePerks();
    }

    public void instantiatePerks() {
        if (!RankManager.getInstance().tryFetchAccumulatedRank()) {
            onPerkSelectionCompleted.invoke(this, null);
            return;
        }
        currentPerksOnScreen = PerkPrefabGenerator.generateRandomPerks(MAX_PERK_PER_GENERATION);
        for (int i = 0; i < MAX_PERK_PER_GENERATION; i++) {
            currentPerksOnScreen.get(i).getTransform().setGlobalPosition(perkPositions.get(i));
            currentPerksOnScreen.get(i).onPointerClicked.addListener(this::perkManager_OnPointerClickedPerk);
        }

        showPerksAnimation();
    }

    public void perkManager_OnPerkChosen(Object sender, Void e) {
        destroyPerks();
        instantiatePerks();
    }

    public void perkManager_OnPointerClickedPerk(Object sender, MouseEvent e) {
        onPerkChosen.invoke(this, null);
    }

    private void destroyPerks() {
        for (Perk perk : currentPerksOnScreen) {
            perk.destroyText();
            GameObjectManager.destroy(perk.getGameObject());
        }

        currentPerksOnScreen.clear();
    }

    private void showPerksAnimation() {
        for (var perk : currentPerksOnScreen) {
            Tween.to(perk.getGameObject())
                    .moveY(SLIDE_DISTANCE, SLIDE_DURATION)
                    .ease(Ease.IN_OUT_BACK)
                    .play();
            System.out.println("Showing perks animation " + perk.textUI.getTransform().getGlobalPosition());
        }


    }

}
