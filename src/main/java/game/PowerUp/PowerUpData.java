package game.PowerUp;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PowerUpData {

    static final Map<PowerUpIndex, String> POWER_UP_IMAGE_PATH_MAP;
    static {
        Map<PowerUpIndex, String> data = new HashMap<>();
        data.put(PowerUpIndex.DuplicateBall, "/double_ball.png");
        data.put(PowerUpIndex.TriplicateBall, "/triple_ball.png");
        data.put(PowerUpIndex.Explosive, "/explosion.png");

        //noinspection Java9CollectionFactory
        POWER_UP_IMAGE_PATH_MAP = Collections.unmodifiableMap(data);
    }

}