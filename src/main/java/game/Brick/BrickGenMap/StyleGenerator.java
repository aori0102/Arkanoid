package game.Brick.BrickGenMap;

import game.Brick.InitMatrix.BrickMatrix;
import java.util.Random;

public interface StyleGenerator {
  BrickMatrix generate(int rows, int cols, double difficulty, Random rng);
}
