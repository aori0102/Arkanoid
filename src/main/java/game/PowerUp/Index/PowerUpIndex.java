package game.PowerUp.Index;

import org.Rendering.ImageAsset;

public enum PowerUpIndex {

    DuplicateBall(ImageAsset.ImageIndex.DuplicateBall),
    TriplicateBall(ImageAsset.ImageIndex.TriplicateBall),
    FireBall(ImageAsset.ImageIndex.FireBallICon),
    Blizzard(ImageAsset.ImageIndex.BlizzardBallIcon),
    Explosive(ImageAsset.ImageIndex.Explosive),
    Shield(ImageAsset.ImageIndex.ShieldIcon),
    Expand(ImageAsset.ImageIndex.None),
    Slow(ImageAsset.ImageIndex.None),
    Catch(ImageAsset.ImageIndex.None),
    LaserBeam(ImageAsset.ImageIndex.LaserBeam),
    Recovery(ImageAsset.ImageIndex.HealIcon),
    None(ImageAsset.ImageIndex.None);

    private final ImageAsset.ImageIndex imageIndex;

    PowerUpIndex(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }

}
