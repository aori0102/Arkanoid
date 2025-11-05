package game.Brick;

import org.Rendering.ImageAsset;

public enum BrickType {

    Normal(36, 100, ImageAsset.ImageIndex.BrickNormal),
    Rock(50, 30, ImageAsset.ImageIndex.BrickRock),
    Steel(80, 320, ImageAsset.ImageIndex.BrickSteel),
    Diamond(999999, 0, ImageAsset.ImageIndex.BrickDiamond),
    Rocket(50, 10, ImageAsset.ImageIndex.BrickRocket),
    Bomb(50, 30, ImageAsset.ImageIndex.BrickBomb),
    Gift(10, 10, ImageAsset.ImageIndex.BrickGift),
    Reborn(10, 10, ImageAsset.ImageIndex.BrickReborn),
    Angel(10, 10, ImageAsset.ImageIndex.BrickAngel),;

    public final int maxHealth;
    public final int score;
    public final ImageAsset.ImageIndex imageIndex;

    BrickType(int maxHealth, int score, ImageAsset.ImageIndex imageIndex) {
        this.maxHealth = maxHealth;
        this.score = score;
        this.imageIndex = imageIndex;
    }
}