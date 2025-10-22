package game.Player.PlayerSkills;

import org.Rendering.ImageAsset;

public enum SkillIndex {

    LaserBeam(ImageAsset.ImageIndex.LaserBeam),
    Magnet(ImageAsset.ImageIndex.None),
    Invincible(ImageAsset.ImageIndex.None),

    None(ImageAsset.ImageIndex.None),;

    SkillIndex(ImageAsset.ImageIndex imageIndex) {
        this.imageIndex = imageIndex;
    }

    private final ImageAsset.ImageIndex imageIndex;

    public ImageAsset.ImageIndex getImageIndex() {
        return imageIndex;
    }

}
