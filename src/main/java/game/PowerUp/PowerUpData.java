package game.PowerUp;

import org.ImageAsset;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class PowerUpData {

    static final Map<PowerUpIndex, ImageAsset.ImageIndex> POWER_UP_IMAGE_PATH_MAP;

    static {
        Map<PowerUpIndex, ImageAsset.ImageIndex> data = new HashMap<>();
        data.put(PowerUpIndex.DuplicateBall, ImageAsset.ImageIndex.DuplicateBall);
        data.put(PowerUpIndex.TriplicateBall, ImageAsset.ImageIndex.Ball);
        data.put(PowerUpIndex.Explosive, ImageAsset.ImageIndex.Explosive);

        //noinspection Java9CollectionFactory
        POWER_UP_IMAGE_PATH_MAP = Collections.unmodifiableMap(data);
    }

}