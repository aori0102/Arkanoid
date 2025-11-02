package game.BrickObj;

import game.BrickObj.BrickEvent.CollisionEvent;
import org.GameObject.GameObjectManager;
import utils.Vector2;

import java.util.Vector;

import static game.Brick.BrickGenMap.TransTypeNumBer.transNumberToType;
import static game.BrickObj.Render.setRender;

public class Init {

    private Init() {
    }

    public static record IntPair(int fi, int se) {
    }

    public final static int[] fx = {-1, 1, 0, 0, 1, 1, -1, -1};
    public final static int[] fy = {0, 0, 1, -1, 1, -1, 1, -1};

    public static Brick getNewBrick(BrickType brickType) {
        var currentBrick = GameObjectManager.instantiate().addComponent(Brick.class);
        currentBrick.setType(brickType);
        setRender(currentBrick);
        return currentBrick;
    }

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

        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Matrix clone() {
            Matrix copy = new Matrix(this.rows, this.columns, 0);
            copy.assignFrom(this);
            return copy;
        }
    }

    public static class BrickMatrix {
        private int rows;
        private int columns;
        private Vector<Vector<Brick>> matrix;

        public BrickMatrix(int rows, int columns, Brick val) {
            this.rows = rows;
            this.columns = columns;
            this.matrix = new Vector<>(rows);
            var anchor = new Vector2(300.0, 300.0);
            for (int r = 0; r < rows; r++) {
                Vector<Brick> row = new Vector<>(columns);
                for (int c = 0; c < columns; c++) {
                    var currentBrick = getNewBrick(val.getBrickType());
                    currentBrick.onBrickCollision.addListener(this::onBrickCollision);
                    currentBrick.setRowId(r);
                    currentBrick.setColID(c);

                    row.add(currentBrick);
                }
                matrix.add(row);
            }
        }

        private void onBrickCollision(Object curr, Void E) {
            if (curr instanceof Brick brickobj) {
                CollisionEvent.ColliEvent(brickobj.getRowID(), brickobj.getColID(), brickobj.getBrickType());
            }
        }

        public BrickMatrix(int rows, int columns) {
            this(rows, columns, getNewBrick(BrickType.Normal));
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
            beDestroy(r, c);
            matrix.get(r).set(c, null);
            value.setRowId(r);
            value.setColID(c);
            value.onBrickCollision.addListener(this::onBrickCollision);
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

        public boolean invalid(int r, int c) {
            return matrix.get(r).get(c) == null;
        }

        public void hitDamage(int r, int c, int damge) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).hitDamage(damge);
        }

        public void incHealth(int r, int c, int inc) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).heal(inc);
        }

        public void decHealth(int r, int c, int inc) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).decreaseHealth(inc);
        }

        public boolean isDestroyed(int r, int c) {
            if(invalid(r, c)) return true;
            return matrix.get(r).get(c).isDestroyed();
        }

        public int getHealth(int r, int c) {
            if(invalid(r, c)) return 0;
            return matrix.get(r).get(c).getHealth();
        }

        public BrickType getObjType(int r, int c) {
            if(invalid(r, c)) return BrickType.Normal;
            return matrix.get(r).get(c).getBrickType();
        }

        public void beDestroy(int r, int c) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).beDestroy();
        }

        public boolean getIsNewDeath(int r, int c) {
            if(invalid(r, c)) return false;
            return matrix.get(r).get(c).isObjNewDeath();
        }

        public void setObjDeathStatus(int r, int c) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).setObjDeathState();
        }

        public boolean isJustDamaged(int r, int c) {
            if(invalid(r, c)) return false;
            return matrix.get(r).get(c).isJustDamaged();
        }

        public void resetJustDamaged(int r, int c) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).resetIsDamaged();
        }

        public void setWaveIndex(int r, int c, int val) {
            if(invalid(r, c)) return;
            matrix.get(r).get(c).setWaveIndex(val);
        }

        public int getWaveIndex(int r, int c) {
            if(invalid(r, c)) return 0;
            return matrix.get(r).get(c).getWaveIndex();
        }

        public void setType(int r, int c, BrickType type) {
            get(r, c).setType(type);
            setRender(get(r, c));
        }

        public void transIntToBrick(Matrix that) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    System.out.println("beforeTransIntToBrick: r=" + r + " c=" +
                            c + " " + that.get(r, c) + " " + transNumberToType(that.get(r, c))
                            + " " + getObjType(r, c));
//                    setType(r, c, transNumberToType(that.get(r, c)));
                    System.out.println("transIntToBrick: r=" + r + " c=" +
                            c + " " + that.get(r, c) + " " + transNumberToType(that.get(r, c))
                            + " " + getObjType(r, c));
                }
            }
        }

        public void deleted() {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if(!invalid(r, c)) {
                        System.out.println("Destroy: " + r + " " + c);
                        beDestroy(r, c);
                    }
                }
            }
        }

        public void setWaveIndex(Matrix that) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if(invalid(r, c)) continue;
                    setWaveIndex(r, c, that.get(r, c));
                }
            }
        }

        public Matrix getWaveIndex() {
            Matrix matrix = new Matrix(rows, columns);
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < columns; c++) {
                    if(invalid(r, c)) continue;
                    matrix.set(r, c, getWaveIndex(r, c));
                }
            }

            return matrix.clone();
        }

    }

    public static BrickMatrix matrixObj;
    public static int rowData = 0;
    public static int colData = 0;
}
