package game.Brick;

import game.Brick.BrickEvent.CollisionEvent;
import org.GameObjectManager;

import java.util.Vector;
public class InitMatrix {

    private InitMatrix() {}
    public static record IntPair(int fi, int se) {}

    public final static int[] fx = {-1, 1, 0, 0, 1, 1, -1, -1};
    public final static int[] fy = {0, 0, 1, -1, 1, -1, 1, -1};

    public static Brick getNewBrick(BrickType brickType) {
        var brickObject = GameObjectManager.instantiate("Brick");
        var brickComponent = brickObject.addComponent(Brick.class);
        brickComponent.setType(brickType);
        return brickComponent;
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
            if(other.rows != this.rows || other.columns != this.columns) {
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

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Matrix clone() {
            Matrix copy = new Matrix(this.rows, this.columns, 0);
            copy.assignFrom(this);
            return copy;
        }
    }

    public static class BrickMatrix implements Cloneable {
        private int rows;
        private int columns;
        private Vector<Vector<Brick>> matrix;

        public BrickMatrix(int rows, int columns, Brick val) {
            this.rows = rows;
            this.columns = columns;
            this.matrix = new Vector<>(rows);
            for (int r = 0; r < rows; r++) {
                Vector<Brick> row = new Vector<>(columns);
                for (int c = 0; c < columns; c++) {
                    var curr = GameObjectManager.instantiate(val.getGameObject());
                    var tmp = curr.getComponent(Brick.class);
                    tmp.onBrickCollision.addListener(this::onBrickCollision);
                    tmp.setRowId(r);
                    tmp.setColID(c);
                    row.add(tmp);
                }
                matrix.add(row);
            }
        }

        private void onBrickCollision(Object curr, Void E) {
            if(curr instanceof Brick brickobj) {
                CollisionEvent.ColliEvent(brickobj.getRowID(), brickobj.getColID(), brickobj.getBrickType());
            }
        }

        public BrickMatrix(int rows, int columns) {
            this(rows, columns, null);
        }

        public BrickMatrix(BrickMatrix other) {
            this(other.rows, other.columns, null);
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

        public Brick get(int r, int c) {
            return matrix.get(r).get(c);
        }

        public void set(int r, int c, Brick value) {
            value.setRowId(r);
            value.setColID(c);
            matrix.get(r).set(c, value);
        }

        public void fill(Brick value) {
            for (int r = 0; r < rows; r++) {
                Vector<Brick> row = matrix.get(r);
                for (int c = 0; c < columns; c++) {
                    value.setColID(c);
                    value.setRowId(r);
                    row.set(c, value);
                }
            }
        }

        public void assignFromResize(BrickMatrix other) {
            this.rows = other.rows;
            this.columns = other.columns;

            Vector<Vector<Brick>> newMat = new Vector<>(rows);
            for (int r = 0; r < rows; r++) {
                Vector<Brick> row = new Vector<>(columns);
                for (int c = 0; c < columns; c++) {
                    row.add(other.matrix.get(r).get(c)); // shallow phần tử
                }
                newMat.add(row);
            }
            this.matrix = newMat;
        }

        public void assignFrom(BrickMatrix other) {
            if (other.rows != this.rows || other.columns != this.columns) {
                assignFromResize(other);
                return;
            }

            for (int r = 0; r < rows; r++) {
                Vector<Brick> dstRow = this.matrix.get(r);
                Vector<Brick> srcRow = other.matrix.get(r);
                for (int c = 0; c < columns; c++) {
                    dstRow.set(c, srcRow.get(c));
                }
            }
        }

        public void hitDamage(int r, int c, int damge) {
             matrix.get(r).get(c).hitDamage(damge);
        }

        public void incHealth(int r, int c, int inc) {
            matrix.get(r).get(c).heal(inc);
        }

        public void decHealth(int r, int c, int inc) {
            matrix.get(r).get(c).decreaseHealth(inc);
        }

        public boolean isDestroyed(int r, int c) {
            return matrix.get(r).get(c).isDestroyed();
        }

        public int getHealth(int r, int c) {
            return matrix.get(r).get(c).getHealth();
        }

        public BrickType getObjType(int r, int c) {
            return matrix.get(r).get(c).getBrickType();
        }

        public void beDestroy(int r, int c) {
            matrix.get(r).get(c).beDestroy();
        }

        public boolean getIsNewDeath(int r, int c) {
            return matrix.get(r).get(c).isObjNewDeath();
        }

        public void setObjDeathStatus(int r, int c) {
            matrix.get(r).get(c).setObjDeathState();
        }

        public boolean isJustDamaged(int r, int c) {
            return matrix.get(r).get(c).isJustDamaged();
        }

        public void resetJustDamaged(int r, int c) {
            matrix.get(r).get(c).resetIsDamaged();
        }

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public BrickMatrix clone() {
            BrickMatrix copy = new BrickMatrix(this.rows, this.columns, null);
            copy.assignFrom(this);
            return copy;
        }
    }

    public static BrickMatrix matrixObj;
    public static int rowData = 0;
    public static int colData = 0;
}
