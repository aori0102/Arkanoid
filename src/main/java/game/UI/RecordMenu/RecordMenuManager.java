package game.UI.RecordMenu;

import game.PlayerData.DataManager;
import game.PlayerData.Record;
import game.UI.GoBackButtonManager;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Text.TextUI;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

import java.util.ArrayList;
import java.util.List;

public class RecordMenuManager extends MonoBehaviour {
    private static RecordMenuManager instance;
    private boolean isRecordMenuShowed = false;
    private TextUI noNumber;
    private TextUI rank;
    private TextUI score;
    private TextUI brickDestroyed;
    private TextUI levelCleared;

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

        PrefabManager.instantiatePrefab(PrefabIndex.RecordTitle);



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

    public void destroyRecordsUI() {
        for (RecordUI recordUI : recordUIList) {
            GameObjectManager.destroy(recordUI.getGameObject());
        }

        recordUIList.clear();
    }

    public static RecordMenuManager getInstance() {
        return instance;
    }

    public boolean isRecordMenuShowed() {
        return isRecordMenuShowed;
    }

    public void showUI() {
        int i = 0;
        for (RecordUI recordUI : recordUIList) {
            Tween.to(recordUI.getGameObject())
                    .moveX( - TWEEN_DISTANCE, TWEEN_DURATION)
                    .ease(Ease.OUT_BACK)
                    .setDelay(0 + i * 0.1)
                    .play();
            i++;
        }
        GoBackButtonManager.getInstance().showUI();
        isRecordMenuShowed = true;
        recordTitle.startAnimation();
    }

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
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param recordTitle .
     */
    public void linkRecordTitle(RecordTitle recordTitle) {
        this.recordTitle = recordTitle;
    }
}
