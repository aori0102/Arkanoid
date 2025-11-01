package game.Brick;

import org.Rendering.ImageAsset;

public enum BrickType {

    Normal(36, 100, 2, ImageAsset.ImageIndex.GreenBrick),
    Steel(80, 320, 6, ImageAsset.ImageIndex.PurpleBrick),
    Diamond(999999, 0, 0, ImageAsset.ImageIndex.None);

    public final int maxHealth;
    public final int score;
    public final ImageAsset.ImageIndex imageIndex;
    public final int exp;

    BrickType(int maxHealth, int score, int exp, ImageAsset.ImageIndex imageIndex) {
        this.maxHealth = maxHealth;
        this.score = score;
        this.imageIndex = imageIndex;
        this.exp = exp;
    }

}