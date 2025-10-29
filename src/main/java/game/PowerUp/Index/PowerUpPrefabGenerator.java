package game.PowerUp.Index;

import game.PowerUp.BlizzardBall;
import game.PowerUp.DuplicateBall;
import game.PowerUp.FireBall;
import game.PowerUp.Index.PowerUpPrefab.*;
import game.PowerUp.TriplicateBall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class PowerUpPrefabGenerator {

    public static final HashMap<Class<? extends PowerUp>, PowerUpPrefab> powerUpPrefabHashMap = new HashMap<>();
    public static final List<Class<? extends PowerUp>> registeredPowerUps;

    static {
        powerUpPrefabHashMap.put(FireBall.class, new FireBallPrefab());
        //powerUpPrefabHashMap.put(BlizzardBall.class, new BlizzardBallPrefab());
        //powerUpPrefabHashMap.put(TriplicateBall.class, new TriplicateBallPrefab());
        //powerUpPrefabHashMap.put(DuplicateBall.class, new DuplicateBallPrefab());

        registeredPowerUps = new ArrayList<>(powerUpPrefabHashMap.keySet());
    }
}
