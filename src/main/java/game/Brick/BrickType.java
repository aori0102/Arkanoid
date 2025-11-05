package game.Brick;

import org.Rendering.ImageAsset;

public enum BrickType {

    Normal(36, 100, 2, ImageAsset.ImageIndex.CyanBrick),
    Steel(80, 320, 6, ImageAsset.ImageIndex.PurpleBrick),
    Diamond(999, 280, 8, ImageAsset.ImageIndex.BrickDiamond),
    Rock(50, 130, 4, ImageAsset.ImageIndex.BrickRock),
    Rocket(50, 110, 7, ImageAsset.ImageIndex.BrickRocket),
    Bomb(50, 90, 3, ImageAsset.ImageIndex.BrickBomb),
    Gift(10, 50, 2, ImageAsset.ImageIndex.BrickGift),
    Reborn(10, 210, 4, ImageAsset.ImageIndex.BrickReborn),
    Angel(10, 180, 5, ImageAsset.ImageIndex.BrickAngel);

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