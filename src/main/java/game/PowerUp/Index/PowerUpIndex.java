package game.PowerUp.Index;

import org.ImageAsset;

public enum PowerUpIndex {

    DuplicateBall(ImageAsset.ImageIndex.DuplicateBall),
    TriplicateBall(ImageAsset.ImageIndex.DuplicateBall),
    FireBall(ImageAsset.ImageIndex.None),
    Blizzard(ImageAsset.ImageIndex.None),
    Explosive(ImageAsset.ImageIndex.Explosive),
    Shield(ImageAsset.ImageIndex.None),
    Expand(ImageAsset.ImageIndex.None),
    Slow(ImageAsset.ImageIndex.None),
    Catch(ImageAsset.ImageIndex.None),
    LaserBeam(ImageAsset.ImageIndex.None),
    Recovery(ImageAsset.ImageIndex.None),
    None(ImageAsset.ImageIndex.None);

    private final ImageAsset.ImageIndex imageIndex;

    PowerUpIndex(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }

}
