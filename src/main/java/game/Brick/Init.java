package game.Brick;
import java.util.Vector;


public class Init {

    private Init() {
    }

    public static record IntPair(int fi, int se) {
    }

    public final static int[] fx = {-1, 1, 0, 0, 1, 1, -1, -1};
    public final static int[] fy = {0, 0, 1, -1, 1, -1, 1, -1};

    public static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public static class Matrix implements Cloneable {
        private int rows;
        private int columns;
        private Vector<Vector<Integer>> matrix;

        public Matrix(int rows, int columns, int val) {
            this.rows = rows;
            this.columns = columns;
            this.matrix = new Vector<>(rows);
            for (int r = 0; r < rows; r++) {
                Vector<Integer> row = new Vector<>(columns);
                for (int c = 0; c < columns; c++) {
                    row.add(val);
                }
                matrix.add(row);
            }
        }

        public Matrix(int rows, int columns) {
            this(rows, columns, 0);
        }

        public Matrix(Matrix other) {
            this(other.rows, other.columns, 0);
            assignFrom(other);
        }

        public int rows() {
            return rows;
        }

        public int columns() {
            return columns;
        }

        public boolean inBounds(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < columns;
        }

        public int get(int r, int c) {
            return matrix.get(r).get(c);
        }

        public void set(int r, int c, int value) {
            matrix.get(r).set(c, value);
        }

        public boolean valid(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < columns && (get(r, c) != -2);
        }

        public void fill(int value) {
            for (int r = 0; r < rows; r++) {
                Vector<Integer> row = matrix.get(r);
                for (int c = 0; c < columns; c++) {
                    row.set(c, value);
                }
            }
        }

        public void assignFromResize(Matrix other) {
            this.rows = other.rows;
            this.columns = other.columns;

            Vector<Vector<Integer>> newMat = new Vector<>(rows);
            for (int r = 0; r < rows; r++) {
                Vector<Integer> row = new Vector<>(columns);
                for (int c = 0; c < columns; c++) {
                    row.add(other.matrix.get(r).get(c));
                }
                newMat.add(row);
            }
            this.matrix = newMat;
        }

        public void assignFrom(Matrix other) {
            if (other.rows != this.rows || other.columns != this.columns) {
                assignFromResize(other);
                return;
            }

            for (int r = 0; r < rows; r++) {
                Vector<Integer> dstRow = this.matrix.get(r);
                Vector<Integer> srcRow = other.matrix.get(r);
                for (int c = 0; c < columns; c++) {
                    dstRow.set(c, srcRow.get(c));
                }
            }
        }

        public void print() {
            for (int row = 0; row < rows; row++) {
                for (int c = 0; c < columns; c++) {
                    System.out.print(matrix.get(row).get(c));
                    System.out.print(" ");
                }
                System.out.println();
            }
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Matrix clone() {
            Matrix copy = new Matrix(this.rows, this.columns, 0);
            copy.assignFrom(this);
            return copy;
        }
    }
}
