package game.Brick;

import org.Rendering.ImageAsset;

public enum BrickType {

    Normal(18, 100, ImageAsset.ImageIndex.GreenBrick),
    Steel(36, 320, ImageAsset.ImageIndex.PurpleBrick),
    Diamond(999999, 0, ImageAsset.ImageIndex.None);

    public final int maxHealth;
    public final int score;
    public final ImageAsset.ImageIndex imageIndex;

    BrickType(int maxHealth, int score, ImageAsset.ImageIndex imageIndex) {
        this.maxHealth = maxHealth;
        this.score = score;
        this.imageIndex = imageIndex;
    }

}