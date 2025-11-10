package game.Rank;

import game.PlayerData.DataManager;
import org.Event.EventActionID;
import org.Event.EventHandler;
import org.Exception.ReinitializedSingletonException;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;

public final class RankManager extends MonoBehaviour {

    private static final int BASE_EXP = 20;
    private static final double EXP_MULTIPLIER = 1.6;

    private static RankManager instance = null;

    private int accumulatedRank = 0;

    private EventActionID experienceHolder_onAnyExperienceHolderDestroyed_ID = null;

    /**
     * <b>Read-only. Write via {@link #setCurrentExp}.</b>
     */
    private int _currentExp = 0;

    /**
     * <b>Read-only. Write via {@link #setRank}.</b>
     */
    private int _rank = 0;

    public EventHandler<OnExpChangedEventArgs> onExpChanged = new EventHandler<>(RankManager.class);

    public static class OnExpChangedEventArgs {
        public int currentExp;
        public double expRatio;
    }

    public EventHandler<Integer> onRankChanged = new EventHandler<>(RankManager.class);

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

    public void loadProgress() {
        var save = DataManager.getInstance().getProgress();
        setRank(save.getRank());
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

    private int getCurrentRankExp() {
        return (int) (Math.pow(EXP_MULTIPLIER, _rank) * BASE_EXP);
    }

    public int getEXP() {
        return _currentExp;
    }

    public int getRank() {
        return _rank;
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
            setRank(_rank + 1);
            this._currentExp = exceedAmount;
        }

        // Fire event
        var onExpChangedEventArgs = new OnExpChangedEventArgs();
        onExpChangedEventArgs.currentExp = _currentExp;
        onExpChangedEventArgs.expRatio = (double) _currentExp / getCurrentRankExp();
        onExpChanged.invoke(this, onExpChangedEventArgs);

    }

    /**
     * Setter for read-only field {@link #_rank}
     *
     * @param rank The value to set.
     */
    private void setRank(int rank) {
        accumulatedRank += Math.max(0, rank - this._rank);
        System.out.println("Accumulated rank: " + accumulatedRank);
        this._rank = rank;
        onRankChanged.invoke(this, _rank);
    }

    public boolean fetchAccumulatedRank() {
        if (accumulatedRank > 0) {
            accumulatedRank--;
            return true;
        }
        return false;
    }
}