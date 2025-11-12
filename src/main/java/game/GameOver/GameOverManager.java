package game.GameOver;

import game.Level.LevelManager;
import game.Player.PlayerData.DataManager;
import game.Player.PlayerData.ProgressData;
import game.UI.Buttons.GameOverMenuButton;
import game.UI.Buttons.RestartButton;
import game.UI.GameOverMenuController;
import org.Annotation.LinkViaPrefab;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Prefab.PrefabIndex;
import org.Prefab.PrefabManager;
import org.Rendering.SpriteRenderer;
import org.Text.TextUI;
import utils.Time;
import utils.UITween.Tween;
import utils.Vector2;

/**
 * Manager class that handles interaction and UI upon game over.
 */
public final class GameOverManager extends MonoBehaviour {

    private static final double INFO_TWEEN_DELAY = 1.0;
    private static final double DELAY_BETWEEN_REVEAL = 0.8;
    private static final double DELAY_TO_MENU = 1.0;

    private static final double BUTTON_POSITION_OFFSET = 250;
    private static final double BUTTON_POSITION_Y = 600;
    private static final double BUTTON_TWEEN_DURATION = 0.6;

    private enum InfoIndex {
        Score,
        Rank,
        LevelCleared,
        BrickDestroyed,
    }

    private static GameOverManager instance = null;

    private final InfoIndex[] infoIndices = InfoIndex.values();

    private EventActionID levelManager_onGameOver_ID = null;

    private Time.CoroutineID toMenu_coroutineID = null;

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

    @LinkViaPrefab
    private RestartButton restartButton = null;

    @LinkViaPrefab
    private GameOverMenuButton gameOverMenuButton = null;

    private int currentDisplayIndex = 0;
    private Time.CoroutineID revealInfo_coroutineID = null;

    public EventHandler<Void> onRetryRequested = new EventHandler<>(GameOverManager.class);
    public EventHandler<Void> onMainMenuRequested = new EventHandler<>(GameOverManager.class);
    public EventHandler<Void> onAllInfoRevealed = new EventHandler<>(GameOverManager.class);

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

        PrefabManager.instantiatePrefab(PrefabIndex.RestartButton);
        PrefabManager.instantiatePrefab(PrefabIndex.GameOverMenuButton);
    }

    @Override
    public void awake() {
        background.getGameObject().setActive(false);
        gameOverText.getGameObject().setActive(false);
        restartButton.getGameObject().setActive(false);
        gameOverMenuButton.getGameObject().setActive(false);

        restartButton.getTransform()
                .setGlobalPosition(new Vector2(
                        Main.STAGE_WIDTH / 2 + BUTTON_POSITION_OFFSET,
                        BUTTON_POSITION_Y));

        gameOverMenuButton.getTransform()
                .setGlobalPosition(new Vector2(
                        Main.STAGE_WIDTH / 2 - BUTTON_POSITION_OFFSET,
                        BUTTON_POSITION_Y
                ));

        Tween.to(restartButton.getGameObject())
                .fadeTo(0, 0.0001)
                .play();

        Tween.to(gameOverMenuButton.getGameObject())
                .fadeTo(0, 0.0001)
                .play();

        GameObjectManager.instantiate("GameOverMenuController")
                .addComponent(GameOverMenuController.class);
    }

    @Override
    public void start() {
        levelManager_onGameOver_ID = LevelManager.getInstance().onGameOver
                .addListener(this::levelManager_onGameOver);
        onAllInfoRevealed.addListener(this::showButtons);
    }

    @Override
    protected void onDestroy() {
        if (LevelManager.getInstance() != null) {
            LevelManager.getInstance().onGameOver
                    .removeListener(levelManager_onGameOver_ID);
        }
        instance = null;

        Time.removeCoroutine(revealInfo_coroutineID);
        Time.removeCoroutine(toMenu_coroutineID);
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

        ProgressData progressData = DataManager.getInstance().getProgress();

        scoreInfo.setAmountText(String.valueOf(progressData.getScore()));
        levelClearedInfo.setAmountText(String.valueOf(progressData.getLevel()));
        brickDestroyedInfo.setAmountText(String.valueOf(progressData.getBrickDestroyed()));
        rankInfo.setAmountText(String.valueOf(progressData.getRank()));

        scoreInfo.flyInInfo();
        levelClearedInfo.flyInInfo();
        brickDestroyedInfo.flyInInfo();
        rankInfo.flyInInfo();

        currentDisplayIndex = 0;
        revealInfo_coroutineID = Time.addCoroutine(this::revealInfo, INFO_TWEEN_DELAY);
    }

    /**
     * Gradually reveals record info one by one via {@link GameOverInfoDisplayUI#revealAmount()}.
     */
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
            revealInfo_coroutineID = Time.addCoroutine(this::revealInfo, DELAY_BETWEEN_REVEAL);
        } else {
            toMenu_coroutineID
                    = Time.addCoroutine(() -> onMainMenuRequested.invoke(this, null), DELAY_TO_MENU);
        }
    }

    /**
     * Shows the final action buttons (Restart and Menu) by making them active and fading them in.
     * This method is triggered by the {@link #onAllInfoRevealed} event.
     *
     * @param sender The object that invoked the event.
     * @param e      Empty event argument.
     */
    private void showButtons(Object sender, Void e) {
        restartButton.getGameObject().setActive(true);
        gameOverMenuButton.getGameObject().setActive(true);

        Tween.to(restartButton.getGameObject())
                .fadeTo(1, BUTTON_TWEEN_DURATION)
                .play();

        Tween.to(gameOverMenuButton.getGameObject())
                .fadeTo(1, BUTTON_TWEEN_DURATION)
                .play();
    }

    /**
     * Retrieves the {@link RestartButton} component.
     *
     * @return The RestartButton instance.
     */
    public RestartButton getRestartButton() {
        return restartButton;
    }

    /**
     * Retrieves the {@link GameOverMenuButton} component.
     *
     * @return The GameOverMenuButton instance.
     */
    public GameOverMenuButton getGameOverMenuButton() {
        return gameOverMenuButton;
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


    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param restartButton .
     */
    public void linkRestartButton(RestartButton restartButton) {
        this.restartButton = restartButton;
    }


    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link }
     * as part of component linking process.</i></b>
     *
     * @param gameOverMenuButton .
     */
    public void linkGameOverMenuButton(GameOverMenuButton gameOverMenuButton) {
        this.gameOverMenuButton = gameOverMenuButton;
    }

}