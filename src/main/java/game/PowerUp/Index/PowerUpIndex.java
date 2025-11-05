package game.PowerUp.Index;

import org.Prefab.PrefabIndex;
import org.Rendering.ImageAsset;

public enum PowerUpIndex {

    DuplicateBall(ImageAsset.ImageIndex.DuplicateBall, PrefabIndex.PowerUp_Duplicate, 5),
    TriplicateBall(ImageAsset.ImageIndex.TriplicateBall, PrefabIndex.PowerUp_Triplicate, 3),
    FireBall(ImageAsset.ImageIndex.FireBallICon, PrefabIndex.PowerUp_FireBall, 10),
    Blizzard(ImageAsset.ImageIndex.BlizzardBallIcon, PrefabIndex.PowerUp_BlizzardBall, 10),
    Shield(ImageAsset.ImageIndex.ShieldIcon, PrefabIndex.PowerUp_Shield, 3),
    Recovery(ImageAsset.ImageIndex.HealIcon, PrefabIndex.PowerUp_Recovery, 3);

    public final ImageAsset.ImageIndex imageIndex;
    public final PrefabIndex prefabIndex;
    public final int maxCocurrent;

    PowerUpIndex(ImageAsset.ImageIndex imageIndex, PrefabIndex prefabIndex, int maxCocurrent) {
        this.imageIndex = imageIndex;
        this.prefabIndex = prefabIndex;
        this.maxCocurrent = maxCocurrent;
    }

}
