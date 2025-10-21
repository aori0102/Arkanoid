package game.BrickObj.BrickGenMap;

import game.BrickObj.InitMatrix.BrickMatrix;
import java.util.Random;

public interface StyleGenerator {
  BrickMatrix generate(int rows, int cols, double difficulty, Random rng);
}
