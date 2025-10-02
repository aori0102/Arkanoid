package game.Brick.Factory;

import game.Brick.BrickObj;

import java.util.Random;
import java.util.Vector;

public class BrickFactory {

    final int incHealthConst = 5;
    final int decHealthConst = 10;

    public record Pair<A, B> (A row, B col) {
        public static <A, B> Pair<A, B> of (A first, B second) {
            return new Pair<>(first, second);
        }
    }

    private Vector<Pair<Integer, Integer>> listOfObjThatHitDamge;
    private Vector<Vector<BrickObj>> table;
    private final int row, col;
    private final int[] fx = {-1, 1, 0, 0, 1, 1, -1, -1};
    private final int[] fy = {0, 0, 1, -1, 1, -1, 1, -1};

    public BrickFactory(int row, int col) {
        this.row = row;
        this.col = col;
    }

    private int get(int r, int c) {
        return r * col + c;
    }

    public Boolean hitDamge(int x, int y, int damage) {
        if (table.get(x).get(y).isDestroyed()) return false;
        listOfObjThatHitDamge =  new Vector<Pair<Integer, Integer>>();
        table.get(x).get(y).hitDamage(damage);
        listOfObjThatHitDamge.add(Pair.of(x, y));
        return true;
    }

    public Boolean isObjDestroyed(int x, int y) {
        if (table.get(x).get(y).isDestroyed()) return true;
        return false;
    }

    private int pickTypeOfBrick() {
        Random rnd = new Random();
        int num =  rnd.nextInt(360);
        if (num < 160) return 0;
        else if (num < 240) return 1;
        else if (num < 280) return 2;
        else if (num < 320) return 3;
        return 4;
    }

    public void genBrickObj() {
        table = new Vector<>(row);
        for (int i = 0; i < row; i++) {
            Vector<BrickObj> curr = new Vector<>();
            for (int j = 0; j < col; j++) {
                int numType = pickTypeOfBrick();
                curr.add(new BrickObj(BrickObj.typeBrick.getType(numType)));
            }

            table.add(curr);
        }
    }

    public void runDamagedBricks() {
        for (Pair<Integer, Integer> cell : listOfObjThatHitDamge) {
            BrickObj obj = table.get(cell.row).get(cell.col);

            switch (obj.getObjType()) {
                case Rock -> {

                }
                case Nuclear -> {
                    for (int i = 0 ; i < 4; i++) {
                        int nextX = cell.row + fx[i];
                        int nextY = cell.col + fy[i];
                        if(nextX >= 0 && nextX < row && nextY >= 0 && nextY < col && table.get(nextX).get(nextY).isDestroyed()) {
                            table.get(nextX).get(nextY).hitDamage(decHealthConst);
                        }
                    }
                }
                case Gift -> {
                    for (int i = 0 ; i < 4; i++) {
                        int nextX = cell.row + fx[i];
                        int nextY = cell.col + fy[i];
                        if(nextX >= 0 && nextX < row && nextY >= 0 && nextY < col && table.get(nextX).get(nextY).isDestroyed()) {
                            table.get(nextX).get(nextY).incHealth(incHealthConst);
                        }
                    }
                }
            }
        }
        listOfObjThatHitDamge.clear();
    }

    public Vector<Vector<BrickObj>> getTable() {
        return this.table;
    }

    public void runProgAndManagement() {

        runDamagedBricks();
    }
}
