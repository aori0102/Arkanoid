package game.Brick;

import org.GameObject.GameObjectManager;

import java.util.List;
import java.util.Vector;


/**
 * Provides utility methods, constants, and nested classes for managing the game's brick grid.
 * This class is not intended to be instantiated, as indicated by its private constructor.
 * It includes helpers for boundary checking, brick state management, and a general-purpose Matrix class.
 */
public class Init {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private Init() {
    }

    /**
     * A simple record to store a pair of integers, often used for coordinates (row, column).
     *
     * @param fi The first integer (e.g., row).
     * @param se The second integer (e.g., column).
     */
    public static record IntPair(int fi, int se) {
    }

    /**
     * Array of x-coordinate offsets for 8-directional movement (horizontal, vertical, and diagonal).
     * Corresponds to the {@link #fy} array.
     */
    public final static int[] fx = {-1, 1, 0, 0, 1, 1, -1, -1};
    /**
     * Array of y-coordinate offsets for 8-directional movement (horizontal, vertical, and diagonal).
     * Corresponds to the {@link #fx} array.
     */
    public final static int[] fy = {0, 0, 1, -1, 1, -1, 1, -1};

    /**
     * Checks if a given row and column are within the specified grid boundaries.
     *
     * @param r    The row index to check.
     * @param c    The column index to check.
     * @param rows The total number of rows in the grid.
     * @param cols The total number of columns in the grid.
     * @return {@code true} if (r, c) is within bounds [0, rows-1] and [0, cols-1], {@code false} otherwise.
     */
    public static boolean inBounds(int r, int c, int rows, int cols) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    /**
     * Checks if the brick at the specified grid location was just damaged.
     *
     * @param brickGrid The 2D list representing the brick grid.
     * @param row       The row index of the brick.
     * @param col       The column index of the brick.
     * @return {@code true} if the brick exists, is valid (per {@link #valid}), and is in a "just damaged" state, {@code false} otherwise.
     */
    public static boolean isJustDamaged(List<List<Brick>> brickGrid, int row, int col) {
        if (!valid(brickGrid, row, col)) return false;
        return brickGrid.get(row).get(col).isJustDamaged();
    }

    /**
     * Checks if the brick at the specified grid location is considered destroyed (i.e., is {@code null}).
     *
     * @param brickGrid The 2D list representing the brick grid.
     * @param row       The row index to check.
     * @param col       The column index to check.
     * @return {@code true} if the coordinates are valid and the brick at that location is {@code null}, {@code false} otherwise.
     */
    public static boolean isDestroyed(List<List<Brick>> brickGrid, int row, int col) {
        if (!valid(brickGrid, row, col)) return false;
        return brickGrid.get(row).get(col) == null;
    }

    /**
     * Destroys the brick at the specified location.
     * It does nothing if the coordinates are invalid (per {@link #valid}).
     *
     * @param brickGrid The 2D list representing the brick grid.
     * @param row       The row index of the brick to destroy.
     * @param col       The column index of the brick to destroy.
     */
    public static void destroyBrick(List<List<Brick>> brickGrid, int row, int col) {
        if (!valid(brickGrid, row, col)) return;
        GameObjectManager.destroy(brickGrid.get(row).get(col).getGameObject());
    }

    /**
     * Validates if the given coordinates (r, c) are within the grid bounds AND point to a non-null brick.
     *
     * @param brickGrid The 2D list representing the brick grid.
     * @param r         The row index to check.
     * @param c         The column index to check.
     * @return {@code true} if (r, c) is in bounds and {@code brickGrid.get(r).get(c) != null}, {@code false} otherwise.
     */
    public static boolean valid(List<List<Brick>> brickGrid, int r, int c) {
        return r >= 0 && c >= 0 && r < brickGrid.size() && c < brickGrid.get(r).size() && brickGrid.get(r).get(c) != null;
    }

    /**
     * A simple 2D matrix class for storing integer values.
     * This class is implemented using a {@code Vector} of {@code Vector}s.
     * It supports basic matrix operations like get, set, fill, and cloning.
     */
    public static class Matrix implements Cloneable {
        /**
         * The number of rows in the matrix.
         */
        private int rows;
        /**
         * The number of columns in the matrix.
         */
        private int columns;
        /**
         * The underlying data structure, a Vector of Vectors holding the integer values.
         */
        private Vector<Vector<Integer>> matrix;

        /**
         * Constructs a new Matrix with the specified dimensions, initializing all cells to a given value.
         *
         * @param rows    The number of rows.
         * @param columns The number of columns.
         * @param val     The value to fill the matrix with.
         */
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

        /**
         * Constructs a new Matrix with the specified dimensions, initializing all cells to 0.
         *
         * @param rows    The number of rows.
         * @param columns The number of columns.
         */
        public Matrix(int rows, int columns) {
            this(rows, columns, 0);
        }

        /**
         * Copy constructor. Creates a deep copy of another Matrix.
         *
         * @param other The Matrix to copy.
         */
        public Matrix(Matrix other) {
            this(other.rows, other.columns, 0);
            assignFrom(other);
        }

        /**
         * Gets the number of rows in the matrix.
         *
         * @return The number of rows.
         */
        public int rows() {
            return rows;
        }

        /**
         * Gets the number of columns in the matrix.
         *
         * @return The number of columns.
         */
        public int columns() {
            return columns;
        }

        /**
         * Checks if a given row and column are within the matrix boundaries.
         *
         * @param r The row index to check.
         * @param c The column index to check.
         * @return {@code true} if (r, c) is within bounds, {@code false} otherwise.
         */
        public boolean inBounds(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < columns;
        }

        /**
         * Retrieves the value at the specified cell.
         *
         * @param r The row index.
         * @param c The column index.
         * @return The integer value at (r, c).
         * @throws ArrayIndexOutOfBoundsException if (r, c) is out of bounds.
         */
        public int get(int r, int c) {
            return matrix.get(r).get(c);
        }

        /**
         * Sets the value at the specified cell.
         *
         * @param r     The row index.
         * @param c     The column index.
         * @param value The new integer value to set.
         * @throws ArrayIndexOutOfBoundsException if (r, c) is out of bounds.
         */
        public void set(int r, int c, int value) {
            matrix.get(r).set(c, value);
        }

        /**
         * Checks if a cell is "valid," meaning it is within bounds AND its value is not -2.
         * The value -2 is treated as a special marker for an invalid or non-accessible cell.
         *
         * @param r The row index.
         * @param c The column index.
         * @return {@code true} if the cell is in bounds and its value is not -2, {@code false} otherwise.
         */
        public boolean valid(int r, int c) {
            return r >= 0 && r < rows && c >= 0 && c < columns && (get(r, c) != -2);
        }

        /**
         * Fills the entire matrix with a specified value.
         *
         * @param value The value to set for all cells.
         */
        public void fill(int value) {
            for (int r = 0; r < rows; r++) {
                Vector<Integer> row = matrix.get(r);
                for (int c = 0; c < columns; c++) {
                    row.set(c, value);
                }
            }
        }

        /**
         * Assigns the contents of another matrix to this one, resizing this matrix to match.
         * This method performs a deep copy of the data and updates this matrix's dimensions.
         *
         * @param other The source Matrix to copy from.
         */
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

        /**
         * Assigns the contents of another matrix to this one.
         * If the dimensions do not match, this method will call {@link #assignFromResize(Matrix)}
         * to resize this matrix first. Otherwise, it copies the values cell by cell.
         *
         * @param other The source Matrix to copy from.
         */
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

        /**
         * Prints the contents of the matrix to the standard output ({@code System.out}).
         * Cells are separated by spaces, and rows are separated by newlines.
         */
        public void print() {
            for (int row = 0; row < rows; row++) {
                for (int c = 0; c < columns; c++) {
                    System.out.print(matrix.get(row).get(c));
                    System.out.print(" ");
                }
                System.out.println();
            }
        }

        /**
         * Creates and returns a deep copy of this matrix.
         *
         * @return A new {@code Matrix} instance with the same dimensions and data as this one.
         */
        @Override
        @SuppressWarnings("MethodDoesntCallSuperMethod")
        public Matrix clone() {
            Matrix copy = new Matrix(this.rows, this.columns, 0);
            copy.assignFrom(this);
            return copy;
        }
    }
}