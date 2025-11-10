package game.Brick;

import org.Rendering.ImageAsset;

/**
 * Enumerates the different types of bricks available in the game.
 * Each brick type defines its properties such as health, score value,
 * experience points, and the visual asset it uses.
 */
public enum BrickType {

    /**
     * A standard, breakable brick.
     */
    Normal(36, 100, 2, ImageAsset.ImageIndex.BrickNormal),

    /**
     * A durable brick that requires more hits to break.
     */
    Steel(80, 320, 6, ImageAsset.ImageIndex.BrickSteel),

    /**
     * An indestructible (or extremely high health) brick.
     */
    Diamond(999, 280, 8, ImageAsset.ImageIndex.BrickDiamond),

    /**
     * A tough brick, stronger than Normal but weaker than Steel.
     */
    Rock(100, 130, 4, ImageAsset.ImageIndex.BrickRock),

    /**
     * A special brick that may trigger a rocket power-up or effect.
     */
    Rocket(100, 110, 7, ImageAsset.ImageIndex.BrickRocket),

    /**
     * A special brick that may trigger an explosion.
     */
    Bomb(100, 90, 3, ImageAsset.ImageIndex.BrickBomb),

    /**
     * A special brick that may drop a gift or power-up.
     */
    Gift(100, 50, 2, ImageAsset.ImageIndex.BrickGift),

    /**
     * A special brick (visual appears to be 'Reborn').
     */
    Wheel(100, 210, 4, ImageAsset.ImageIndex.BrickReborn),

    /**
     * A special brick (visual appears to be 'Angel').
     */
    Angel(100, 180, 5, ImageAsset.ImageIndex.BrickAngel);

    /**
     * The durability or hit points of the brick.
     */
    public final int maxHealth;

    /**
     * The score awarded to the player when this brick is destroyed.
     */
    public final int score;

    /**
     * The index of the image asset used to render this brick.
     */
    public final ImageAsset.ImageIndex imageIndex;

    /**
     * The experience points (EXP) awarded to the player when this brick is destroyed.
     */
    public final int exp;

    /**
     * Constructor for the enum.
     *
     * @param maxHealth  The health/durability of the brick.
     * @param score      The score value for destroying the brick.
     * @param exp        The experience points awarded.
     * @param imageIndex The {@link ImageAsset.ImageIndex} for the brick's sprite.
     */
    BrickType(int maxHealth, int score, int exp, ImageAsset.ImageIndex imageIndex) {
        this.maxHealth = maxHealth;
        this.score = score;
        this.imageIndex = imageIndex;
        this.exp = exp;
    }
}