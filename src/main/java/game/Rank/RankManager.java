package game.Rank;

import game.Player.PlayerData.DataManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

/**
 * Manager class that handles player's rank in game.
 */
public final class RankManager extends MonoBehaviour {

    private static final int BASE_EXP = 20;
    private static final double EXP_MULTIPLIER = 1.6;

    private static RankManager instance = null;

    /**
     * <b>Read-only. Write via {@link #setAccumulatedRank}.</b>
     */
    private int _accumulatedRank = 0;

    private EventActionID experienceHolder_onAnyExperienceHolderDestroyed_ID = null;

    /**
     * <b>Read-only. Write via {@link #setCurrentExp}.</b>
     */
    private int _currentExp = 0;

    private int rank = 0;

    public EventHandler<OnExpChangedEventArgs> onExpChanged = new EventHandler<>(RankManager.class);

    public static class OnExpChangedEventArgs {
        public int currentExp;
        public double expRatio;
    }

    public EventHandler<Integer> onRankChanged = new EventHandler<>(RankManager.class);
    public EventHandler<Void> onAccumulatedRankEmptied = new EventHandler<>(RankManager.class);
    public EventHandler<Void> onAccumulatedRankGained = new EventHandler<>(RankManager.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public RankManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new ReinitializedSingletonException("ExperienceManager is a singleton!");
        }
        instance = this;
    }

    public static RankManager getInstance() {
        return instance;
    }

    @Override
    public void start() {
        experienceHolder_onAnyExperienceHolderDestroyed_ID = ExperienceHolder.onAnyExperienceHolderDestroyed
                .addListener(this::experienceHolder_onAnyExperienceHolderDestroyed);
    }

    @Override
    public void onDestroy() {
        instance = null;

        ExperienceHolder.onAnyExperienceHolderDestroyed
                .removeListener(experienceHolder_onAnyExperienceHolderDestroyed_ID);
    }

    /**
     * Load the saved progress data from last session.
     */
    public void loadProgress() {
        var save = DataManager.getInstance().getProgress();
        rank = save.getRank();
        setCurrentExp(save.getExp());
    }

    /**
     * Called when {@link ExperienceHolder#onAnyExperienceHolderDestroyed} is invoked.<br><br>
     * This function adds exp after an object with {@link ExperienceHolder} is destroyed.
     *
     * @param sender Event caller {@link ExperienceHolder}.
     * @param e      Empty event argument.
     */
    private void experienceHolder_onAnyExperienceHolderDestroyed(Object sender, Void e) {
        if (sender instanceof ExperienceHolder expHolder) {
            setCurrentExp(_currentExp + expHolder.getExp());
        }
    }

    /**
     * Get the amount of EXP needed to rank up.
     *
     * @return The amount of EXP needed to rank up.
     */
    private int getCurrentRankExp() {
        return (int) (Math.pow(EXP_MULTIPLIER, rank) * BASE_EXP);
    }

    /**
     * Get the current EXP amount.
     *
     * @return The current EXP amount.
     */
    public int getEXP() {
        return _currentExp;
    }

    /**
     * Get the current rank.
     *
     * @return The current rank.
     */
    public int getRank() {
        return rank;
    }

    /**
     * Setter for read-only field {@link #_currentExp}
     *
     * @param currentExp The value to set.
     */
    private void setCurrentExp(int currentExp) {

        var rankExp = getCurrentRankExp();
        var exceedAmount = currentExp - rankExp;
        if (exceedAmount < 0) {
            this._currentExp = currentExp;
        } else {
            // Rank up
            increaseRank();
            this._currentExp = exceedAmount;
        }

        // Fire event
        var onExpChangedEventArgs = new OnExpChangedEventArgs();
        onExpChangedEventArgs.currentExp = _currentExp;
        onExpChangedEventArgs.expRatio = (double) _currentExp / getCurrentRankExp();
        onExpChanged.invoke(this, onExpChangedEventArgs);

    }

    /**
     * Setter for read-only field {@link #_accumulatedRank}
     *
     * @param accumulatedRank The value to set.
     */
    private void setAccumulatedRank(int accumulatedRank) {
        this._accumulatedRank = accumulatedRank;
        if (_accumulatedRank > 0) {
            onAccumulatedRankGained.invoke(this, null);
        } else {
            onAccumulatedRankEmptied.invoke(this, null);
        }
    }

    /**
     * Increase rank by {@code 1}. Called within {@link #setCurrentExp} after EXP exceeds
     * the amount needed to rank up.
     */
    private void increaseRank() {
        setAccumulatedRank(_accumulatedRank + 1);
        this.rank++;
        onRankChanged.invoke(this, rank);
    }

    /**
     * Try using accumulated rank. This is used by {@link game.Perks.Index.PerkManager} to
     * check if the player can select perks.
     *
     * @return {@code true} if there is still accumulated rank for perk selection, otherwise
     * {@code false}.
     */
    public boolean tryFetchAccumulatedRank() {
        if (_accumulatedRank > 0) {
            setAccumulatedRank(_accumulatedRank - 1);
            return true;
        }
        return false;
    }

}