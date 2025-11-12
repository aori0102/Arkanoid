package game.UI.RecordMenu;

import game.PlayerData.DataManager;
import game.PlayerData.Record;
import game.UI.GoBackButtonManager;
import javafx.scene.paint.Color;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the "Record Menu" UI, which displays the player's high scores and stats.
 * <p>
 * This class implements the Singleton pattern, initializes the static header elements,
 * dynamically generates the records list from {@link DataManager}, and handles the
 * sliding animations for showing and hiding the menu.
 */
public class RecordMenuManager extends MonoBehaviour {
    private static RecordMenuManager instance;
    private boolean isRecordMenuShowed = false;
    private TextUI noNumber;
    private TextUI rank;
    private TextUI score;
    private TextUI brickDestroyed;
    private TextUI levelCleared;

    private List<TextUI> textUIS = new ArrayList<>();
    private double TEXT_HEIGHT = 225;
    private double TWEEN_DISTANCE = Main.STAGE_WIDTH;
    private double TWEEN_DURATION = 0.3;

    private List<RecordUI> recordUIList = new ArrayList<>();

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public RecordMenuManager(GameObject owner) {
        super(owner);

        if (instance != null) {
            throw new ReinitializedSingletonException("RecordManager already initialized!");
        }
        instance = this;

        // Instantiate the title component
        PrefabManager.instantiatePrefab(PrefabIndex.RecordTitle);

        initTextUI();

        // Apply consistent styling to header texts
        setTextStyle(noNumber);
        setTextStyle(rank);
        setTextStyle(score);
        setTextStyle(brickDestroyed);
        setTextStyle(levelCleared);

        // Set positions for header texts
        noNumber.getTransform().setGlobalPosition(new Vector2(250, TEXT_HEIGHT));
        rank.getTransform().setGlobalPosition(new Vector2(345, TEXT_HEIGHT));
        score.getTransform().setGlobalPosition(new Vector2(570, TEXT_HEIGHT));
        brickDestroyed.getTransform().setGlobalPosition(new Vector2(805, TEXT_HEIGHT));
        levelCleared.getTransform().setGlobalPosition(new Vector2(930, TEXT_HEIGHT));

        // Start header texts faded out
        for (TextUI textUI : textUIS) {
            Tween.to(textUI.getGameObject())
                    .fadeTo(0, 0.001)
                    .ease(Ease.IN_OUT)
                    .play();
        }

    }

    @Override
    public void start() {
        destroyRecordsUI();
        createRecordsUI();

        int i = 0;
        for (RecordUI recordUI : recordUIList) {
            recordUI.getTransform().setGlobalPosition(new Vector2(3 * Main.STAGE_WIDTH / 2, 300 + 80 * i));
            i++;
        }

    }

    /**
     * Dynamically creates the {@link RecordUI} instances based on the data retrieved
     * from the {@link DataManager}.
     */
    public void createRecordsUI() {
        int i = 1;
        for (Record record : DataManager.getInstance().getRecords()) {
            RecordUI recordUI = GameObjectManager.instantiate("RecordUI").addComponent(RecordUI.class);
            recordUI.setRecordRanking(i);
            recordUI.setRank(record.getRank());
            recordUI.setScore(record.getScore());
            recordUI.setNumberOfBrickDestroyed(record.getBrickDestroyed());
            recordUI.setLevelCleared(record.getLevelCleared());
            recordUIList.add(recordUI);
            i++;
        }
    }

    /**
     * Destroys all currently existing dynamic {@link RecordUI} GameObjects.
     * This prepares the menu for reloading fresh data.
     */
    public void destroyRecordsUI() {
        for (RecordUI recordUI : recordUIList) {
            GameObjectManager.destroy(recordUI.getGameObject());
        }

        recordUIList.clear();
    }

    /**
     * Retrieves the Singleton instance of the RecordMenuManager.
     *
     * @return The single active instance.
     */
    public static RecordMenuManager getInstance() {
        return instance;
    }

    /**
     * Retrieves the Singleton instance of the RecordMenuManager.
     *
     * @return The single active instance.
     */
    public boolean isRecordMenuShowed() {
        return isRecordMenuShowed;
    }

    /**
     * Animates the Record Menu into view by sliding the records from the right
     * and fading in the header texts.
     */
    public void showUI() {
        int i = 0;
        for (RecordUI recordUI : recordUIList) {
            Tween.to(recordUI.getGameObject())
                    .moveX(-TWEEN_DISTANCE, TWEEN_DURATION)
                    .ease(Ease.OUT_BACK)
                    .setDelay(0 + i * 0.1)
                    .play();
            i++;
        }
        for (TextUI textUI : textUIS) {
            Tween.to(textUI.getGameObject())
                    .fadeTo(1, TWEEN_DURATION)
                    .ease(Ease.IN_OUT)
                    .play();
        }

        GoBackButtonManager.getInstance().showUI();
        isRecordMenuShowed = true;
        recordTitle.startAnimation();
    }

    /**
     * Animates the Record Menu out of view by sliding the records back to the right
     * and fading out the header texts.
     */
    public void hideUI() {
        int i = 0;
        for (RecordUI recordUI : recordUIList) {
            Tween.to(recordUI.getGameObject())
                    .moveX(TWEEN_DISTANCE, TWEEN_DURATION)
                    .ease(Ease.IN_BACK)
                    .setDelay(0 + i * 0.1)
                    .play();
            i++;
        }

        for (TextUI textUI : textUIS) {
            Tween.to(textUI.getGameObject())
                    .fadeTo(0, TWEEN_DURATION)
                    .ease(Ease.IN_OUT)
                    .play();
        }
        GoBackButtonManager.getInstance().hideUI();
        isRecordMenuShowed = false;
        recordTitle.exitAnimation();
    }

    @Override
    protected void onDestroy() {
        instance = null;
    }

    private RecordTitle recordTitle = null;

    /**
     * Links the instantiated {@link RecordTitle} component to this manager.
     * <p>
     * This method is intended to be called internally during the prefab linking process.
     * </p>
     *
     * @param recordTitle The newly instantiated {@link RecordTitle} component.
     */
    public void linkRecordTitle(RecordTitle recordTitle) {
        this.recordTitle = recordTitle;
    }

    /**
     * Applies standard styling properties to a {@link TextUI} component for the header.
     *
     * @param text The {@link TextUI} component to style.
     */
    private void setTextStyle(TextUI text) {
        text.setFontSize(40);
        text.setFont(FontDataIndex.Jersey_25);
        text.setGlow();
        text.setSolidFill(Color.YELLOW);

        text.setVerticalAlignment(TextVerticalAlignment.Middle);
        text.setHorizontalAlignment(TextHorizontalAlignment.Center);
    }

    /**
     * Sets the parent of the header {@link TextUI} GameObjects to the manager's owner GameObject
     * for proper scene hierarchy organization.
     */
    private void setParent() {
        noNumber.getGameObject().setParent(getGameObject());
        rank.getGameObject().setParent(getGameObject());
        score.getGameObject().setParent(getGameObject());
        brickDestroyed.getGameObject().setParent(getGameObject());
        levelCleared.getGameObject().setParent(getGameObject());
    }

    /**
     * Instantiates and initializes the static header {@link TextUI} components
     * with their default text content.
     */
    private void initTextUI() {
        noNumber = GameObjectManager.instantiate("NoNumber").addComponent(TextUI.class);
        rank = GameObjectManager.instantiate("Rank").addComponent(TextUI.class);
        score = GameObjectManager.instantiate("Score").addComponent(TextUI.class);
        brickDestroyed = GameObjectManager.instantiate("Brick").addComponent(TextUI.class);
        levelCleared = GameObjectManager.instantiate("Level").addComponent(TextUI.class);

        setParent();

        noNumber.setText("No.");
        rank.setText("Rank");
        score.setText("Score");
        brickDestroyed.setText("Brick");
        levelCleared.setText("Level");
        levelCleared.setWrapWidth(120);

        textUIS.add(noNumber);
        textUIS.add(rank);
        textUIS.add(score);
        textUIS.add(brickDestroyed);
        textUIS.add(levelCleared);
    }
}
