package game.GameOver;

import game.Level.LevelManager;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.Time;

public final class GameOverManager extends MonoBehaviour {

    private static final double INFO_TWEEN_DELAY = 1.9;
    private static final double DELAY_BETWEEN_REVEAL = 0.8;

    private enum InfoIndex {
        Score,
        Rank,
        LevelCleared,
        BrickDestroyed,
    }

    private static GameOverManager instance = null;

    private final InfoIndex[] infoIndices = InfoIndex.values();

    private EventActionID levelManager_onGameOver_ID = null;

    @LinkViaPrefab
    private SpriteRenderer background = null;

    @LinkViaPrefab
    private GameOverInfoDisplayUI scoreInfo = null;

    @LinkViaPrefab
    private GameOverInfoDisplayUI levelClearedInfo = null;

    @LinkViaPrefab
    private GameOverInfoDisplayUI brickDestroyedInfo = null;

    @LinkViaPrefab
    private GameOverInfoDisplayUI rankInfo = null;

    @LinkViaPrefab
    private TextUI gameOverText = null;

    private int currentDisplayIndex = 0;
    private Time.CoroutineID revealInfo_coroutineID = null;

    public EventHandler<Void> onRetryRequested = new EventHandler<>(GameOverManager.class);
    public EventHandler<Void> onMainMenuRequested = new EventHandler<>(GameOverManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameOverManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("GameOverManager is a singleton!");
        }
        instance = this;
    }

    @Override
    public void awake() {
        background.getGameObject().setActive(false);
        gameOverText.getGameObject().setActive(false);
    }

    @Override
    public void start() {
        levelManager_onGameOver_ID = LevelManager.getInstance().onGameOver
                .addListener(this::levelManager_onGameOver);
    }

    @Override
    protected void onDestroy() {
        if (LevelManager.getInstance() != null) {
            LevelManager.getInstance().onGameOver
                    .removeListener(levelManager_onGameOver_ID);
        }
        instance = null;

        Time.removeCoroutine(revealInfo_coroutineID);
    }

    public static GameOverManager getInstance() {
        return instance;
    }

    /**
     * Called when {@link LevelManager#onGameOver} is invoked.<br><br>
     * This function displays the game over scene.
     *
     * @param sender Event caller {@link LevelManager}.
     * @param e      Empty event argument.
     */
    private void levelManager_onGameOver(Object sender, Void e) {
        background.getGameObject().setActive(true);
        gameOverText.getGameObject().setActive(true);

        scoreInfo.setAmountText("57275");
        levelClearedInfo.setAmountText("23");
        brickDestroyedInfo.setAmountText("3324");
        rankInfo.setAmountText("43");

        scoreInfo.flyInInfo();
        levelClearedInfo.flyInInfo();
        brickDestroyedInfo.flyInInfo();
        rankInfo.flyInInfo();

        currentDisplayIndex = 0;
        revealInfo_coroutineID = Time.addCoroutine(this::revealInfo, Time.getTime() + INFO_TWEEN_DELAY);
    }

    private void revealInfo() {
        var infoIndex = infoIndices[currentDisplayIndex];
        switch (infoIndex) {
            case Score -> scoreInfo.revealAmount();
            case LevelCleared -> levelClearedInfo.revealAmount();
            case BrickDestroyed -> brickDestroyedInfo.revealAmount();
            case Rank -> rankInfo.revealAmount();
        }
        currentDisplayIndex++;
        if (currentDisplayIndex < infoIndices.length) {
            revealInfo_coroutineID = Time.addCoroutine(this::revealInfo, Time.getTime() + DELAY_BETWEEN_REVEAL);
        } else {
            Time.addCoroutine(() -> onMainMenuRequested.invoke(this, null), Time.getTime() + 3);
        }
    }

    /**
     * Link the game over background<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param background The game over background renderer.
     */
    public void linkBackground(SpriteRenderer background) {
        this.background = background;
    }

    /**
     * Link the score info UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param scoreText The score info UI.
     */
    public void linkScoreText(GameOverInfoDisplayUI scoreText) {
        this.scoreInfo = scoreText;
    }

    /**
     * Link the level cleared info UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param levelClearedText The level cleared info UI.
     */
    public void linkLevelClearedText(GameOverInfoDisplayUI levelClearedText) {
        this.levelClearedInfo = levelClearedText;
    }

    /**
     * Link brick destroyed info UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param brickDestroyedText The brick destroyed info UI.
     */
    public void linkBrickDestroyedText(GameOverInfoDisplayUI brickDestroyedText) {
        this.brickDestroyedInfo = brickDestroyedText;
    }

    /**
     * Link rank info UI<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param rankText The rank info UI.
     */
    public void linkRankText(GameOverInfoDisplayUI rankText) {
        this.rankInfo = rankText;
    }

    /**
     * Link game over label<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverManagerPrefab}
     * as part of component linking process.</i></b>
     *
     * @param gameOverText The game over text.
     */
    public void linkGameOverText(TextUI gameOverText) {
        this.gameOverText = gameOverText;
    }

}