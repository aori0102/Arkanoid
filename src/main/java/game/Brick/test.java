package game.Brick;

public class test {
    public static void main(String[] args) {
        BrickFactory obj = new BrickFactory(8, 8, 1, "trx");
        obj.setup();

        obj.handleCollision(new InitMatrix.IntPair(1, 1), 10);
        obj.runProgress();
        obj.handleCollision(new InitMatrix.IntPair(1, 2), 10);
        obj.runProgress();
        obj.handleCollision(new InitMatrix.IntPair(1, 3), 10);
        obj.runProgress();
    }
}
