package game.Brick.BrickGenMap;

import game.Brick.Init.Matrix;
import java.util.Random;

public interface StyleGenerator {
  Matrix generate(int rows, int cols, double difficulty, Random rng);
}
