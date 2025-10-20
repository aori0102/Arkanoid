package game.Brick;

public class TestFactory {
    public static void main(String[] args) {

        BrickFactory factory = new BrickFactory(8, 8, 1, "trx");

        factory.handleCollision(new InitMatrix.IntPair(1, 1), 10);
        factory.runProgress();
        System.out.println("Wave matrix after hit (1,1):");
        printMatrix(factory.getWaveMatrix());

        factory.handleCollision(new InitMatrix.IntPair(1, 2), 10);
        factory.runProgress();
        System.out.println("Wave matrix after hit (1,2):");
        printMatrix(factory.getWaveMatrix());

        factory.handleCollision(new InitMatrix.IntPair(1, 3), 10);
        factory.runProgress();
        System.out.println("Wave matrix after hit (1,3):");
        printMatrix(factory.getWaveMatrix());
    }

    private static void printMatrix(InitMatrix.Matrix m) {
        for (int i = 0; i < m.rows(); i++) {
            for (int j = 0; j < m.columns(); j++) {
                System.out.printf("%3d", m.get(i, j));
            }
            System.out.println();
        }
        System.out.println("----------------------");
    }
}
