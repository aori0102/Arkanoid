package game.BrickObj.BrickGenMap;

import game.BrickObj.Brick;
import game.BrickObj.BrickType;

public class TransTypeNumBer {
    public static BrickType transNumberToType(int number) {
        return switch (number) {
            case 0 -> BrickType.Normal;
            case 1 -> BrickType.Steel;
            case 2 -> BrickType.Diamond;
            case 3 -> BrickType.Rocket;
            case 4 -> BrickType.Bomb;
            case 5 -> BrickType.Gift;
            case 6 -> BrickType.Reborn;
            case 7 -> BrickType.Rock;
            case 8 -> BrickType.Angel;
            default -> BrickType.Normal;
        };
    }

    public static int transTypeToNumber(BrickType type) {
        return switch (type) {
            case BrickType.Normal -> 0;
            case BrickType.Steel -> 1;
            case BrickType.Diamond -> 2;
            case BrickType.Rocket -> 3;
            case BrickType.Bomb -> 4;
            case BrickType.Gift-> 5 ;
            case BrickType.Reborn -> 6;
            case BrickType.Rock -> 7;
            case BrickType.Angel -> 8;
            default -> 0;
        };
    }
}

