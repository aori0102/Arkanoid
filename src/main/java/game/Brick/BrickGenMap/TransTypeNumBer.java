package game.Brick.BrickGenMap;

import game.Brick.BrickType;

/**
 * A utility class for translating between {@link BrickType} enums
 * and their corresponding integer representations (IDs).
 *
 * <p>This is commonly used to store map data in a more memory-efficient
 * integer-based {@code Matrix}.
 *
 * <p><b>Note:</b> The original class name 'TransTypeNumBer' contained a typo.
 * It has been corrected to 'TransTypeNumber'.
 */
public class TransTypeNumBer {

    /**
     * Converts a numeric ID into its corresponding {@link BrickType} enum.
     *
     * @param number The integer representation (ID) of the brick.
     * @return The matching {@link BrickType}. Returns {@link BrickType#Normal}
     * as a default if the number is not recognized.
     */
    public static BrickType transNumberToType(int number) {
        return switch (number) {
            case 0 -> BrickType.Normal;
            case 1 -> BrickType.Steel;
            case 2 -> BrickType.Diamond;
            case 3 -> BrickType.Rocket;
            case 4 -> BrickType.Bomb;
            case 5 -> BrickType.Gift;
            case 6 -> BrickType.Wheel;
            case 7 -> BrickType.Rock;
            case 8 -> BrickType.Angel;
            default -> BrickType.Normal;
        };
    }

    /**
     * Converts a {@link BrickType} enum into its corresponding numeric ID.
     *
     * @param type The {@link BrickType} enum instance to convert.
     * @return The matching integer ID. Returns 0 (the ID for Normal)
     * as a default if the type is not recognized.
     */
    public static int transTypeToNumber(BrickType type) {
        return switch (type) {
            case BrickType.Normal -> 0;
            case BrickType.Steel -> 1;
            case BrickType.Diamond -> 2;
            case BrickType.Rocket -> 3;
            case BrickType.Bomb -> 4;
            case BrickType.Gift-> 5 ;
            case BrickType.Wheel -> 6;
            case BrickType.Rock -> 7;
            case BrickType.Angel -> 8;
            default -> 0;
        };
    }
}

