package game.BrickObj.BrickGenMap;

import game.BrickObj.Init.Matrix;
import java.util.Random;

public interface StyleGenerator {
  Matrix generate(int rows, int cols, double difficulty, Random rng);
}
