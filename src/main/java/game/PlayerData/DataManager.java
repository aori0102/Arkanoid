package game.PlayerData;

import com.google.gson.Gson;
import game.GameManager.GameManager;
import game.Level.LevelManager;
import game.MapGenerator.BrickMapManager;
import game.Player.Player;
import game.Rank.RankManager;
import game.Score.ScoreManager;
import org.Event.EventActionID;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class DataManager extends MonoBehaviour {

    private static final int MAX_RECORD = 5;

    public final Path RECORD_FILE = GameManager.PLAYER_DATA_DIRECTORY.resolve("Record.json");
    public final Path SAVE_FILE = GameManager.PLAYER_DATA_DIRECTORY.resolve("Save.json");

    private static DataManager instance = null;

    private final ProgressData progressData = new ProgressData();
    private final List<Record> recordList = new ArrayList<>();
    private final Comparator<Record> recordComparator
            = Comparator.comparing(Record::getScore)
            .thenComparing(Record::getLevelCleared)
            .thenComparing(Record::getRank)
            .thenComparing(Record::getBrickDestroyed)
            .reversed();

    private EventActionID levelManager_onLevelConcluded_ID = null;
    private EventActionID levelManager_onGameOver_ID = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DataManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("SaveManager is a singleton!");
        }
        instance = this;
        loadProgress();
        loadRecords();
    }

    public static DataManager getInstance() {
        return instance;
    }

    @Override
    public void onDestroy() {
        saveProgress();
        saveRecords();
        if (LevelManager.getInstance() != null) {
            LevelManager.getInstance().onLevelConcluded
                    .removeListener(levelManager_onLevelConcluded_ID);
            LevelManager.getInstance().onGameOver
                    .removeListener(levelManager_onGameOver_ID);
        }
        instance = null;
    }

    @Override
    public void start() {
        levelManager_onLevelConcluded_ID = LevelManager.getInstance().onLevelConcluded
                .addListener(this::levelManager_onLevelConcluded);
        levelManager_onGameOver_ID = LevelManager.getInstance().onGameOver
                .addListener(this::levelManager_onGameOver);
    }

    /**
     * Called when {@link LevelManager#onGameOver} is invoked.<br><br>
     * This function saves a new record and remove current save when a game is over.
     *
     * @param sender Event caller {@link LevelManager}.
     * @param e      Empty event argument.
     */
    private void levelManager_onGameOver(Object sender, Void e) {
        generateNewRecord();
    }

    /**
     * Called when {@link LevelManager#onLevelConcluded} is invoked.<br><br>
     * This function saves the progress when a level is concluded.
     *
     * @param sender Event caller {@link LevelManager}.
     * @param e      Empty event argument.
     */
    private void levelManager_onLevelConcluded(Object sender, Void e) {
        updateSave();
    }

    public void loadProgress() {

        try {

            if (!Files.exists(SAVE_FILE)) {
                System.out.println("[ProgressManager] Progress save file not found. Creating empty progress save.");
                return;
            }

            Gson gsonLoader = new Gson();
            String json = Files.readString(SAVE_FILE);
            ProgressData progressData = gsonLoader.fromJson(json, ProgressData.class);
            this.progressData.overrideSave(progressData);

            System.out.println("[ProgressManager] Progress save file loaded.");
            System.out.println(progressData);

        } catch (Exception e) {
            System.err.println("[ProgressManager] Error while loading records: " + e.getMessage());
        }

    }

    private void saveProgress() {

        try {

            Gson gsonSaver = new Gson();
            String json = gsonSaver.toJson(progressData);
            Files.writeString(SAVE_FILE, json);

        } catch (Exception e) {
            System.err.println("[ProgressManager] Error while saving records: " + e.getMessage());
        }

    }

    // TODO: update save when perk is selected
    private void updateSave() {
        progressData.setScore(ScoreManager.getInstance().getScore());
        progressData.setCombo(ScoreManager.getInstance().getCombo());

        progressData.setExp(RankManager.getInstance().getEXP());
        progressData.setRank(RankManager.getInstance().getRank());

        progressData.setLevel(LevelManager.getInstance().getClearedLevel());

        progressData.setBrickDestroyed(BrickMapManager.getInstance().getBrickDestroyed());

        progressData.setPerkList(new Integer[]{1, 4, 2, 6});
        // TODO: update this
        // TODO: update perk when loading progress

        progressData.setHealth(Player.getInstance().getPlayerPaddle().getPaddleHealth().getHealth());
    }

    private void generateNewRecord() {
        recordList.add(new Record(progressData));
        recordList.sort(recordComparator);
        while (recordList.size() > MAX_RECORD) {
            recordList.removeLast();
        }
    }

    public void resetSave() {
        progressData.reset();
    }

    public ProgressData getProgress() {
        return progressData;
    }

    private void loadRecords() {

        try {

            if (!Files.exists(RECORD_FILE)) {
                System.out.println("[ProgressManager] Record file not found. Creating empty record.");
                return;
            }

            Gson gsonLoader = new Gson();
            String json = Files.readString(RECORD_FILE);

            Record[] records = gsonLoader.fromJson(json, Record[].class);
            recordList.addAll(List.of(records));
            recordList.sort(recordComparator);

        } catch (Exception e) {
            System.err.println("[ProgressManager] Error while loading records: " + e.getMessage());
        }

    }

    private void saveRecords() {

        try {

            Gson gsonSaver = new Gson();
            String json = gsonSaver.toJson(recordList);
            Files.writeString(RECORD_FILE, json);

        } catch (Exception e) {
            System.err.println("[ProgressManager] Error while saving records: " + e.getMessage());
        }

    }

    public List<Record> getRecords() {
        return recordList;
    }

}