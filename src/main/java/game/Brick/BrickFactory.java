package game.Brick;

import game.Brick.BrickEvent.EndEvent;
import game.Brick.BrickEvent.WaveEffect;
import game.Brick.BrickGenMap.GenMap;
import game.Brick.BrickGenMap.MapStyle;
import org.GameObject.GameObjectManager;

import static game.Brick.InitMatrix.*;

public class BrickFactory {

    final int incHealthConst = 5;

    private final double difficult;
    private final MapStyle kindMap;

    private EndEvent endEvent;
    private WaveEffect waveEffect;

    public BrickFactory(int rows, int cols, MapStyle _kindMap, double _difficult) {
        rowData = rows;
        colData = cols;
        this.difficult = _difficult;
        this.kindMap = _kindMap;
        setup();
    }

    public void setup() {
        var brickObject = GameObjectManager.instantiate("Brick");
        var brickComponent = brickObject.addComponent(Brick.class);
        brickComponent.setType(BrickType.Normal);
        matrixObj = new BrickMatrix(rowData, colData, brickComponent);

      /*  GenMap gen = new GenMap(rowData, colData);
        matrixObj = gen.generate(kindMap, difficult);
*/
        endEvent = new EndEvent();
        waveEffect = new WaveEffect();
    }

    public void handleCollision(IntPair brickPosition, int damage) {
        int row = brickPosition.fi();
        int col = brickPosition.se();

        if (matrixObj.inBounds(row, col) && !matrixObj.isDestroyed(row, col)) {
            matrixObj.hitDamage(row, col, damage);
        }
    }

    public void setupNewBrick(int x, int y, BrickType brickType) {
        matrixObj.set(x, y, getNewBrick(brickType));
    }

    public void runProgress() {
        waveEffect.getListObjHitDamage();
        waveEffect.runAllWave();
    }

    public boolean isGameFinished() {
        return endEvent.isEnd();
    }

    public BrickMatrix getBrickMatrix() {
        return matrixObj;
    }

    public Matrix getWaveMatrix() {
        return waveEffect.getStateMatrix().clone();
    }

    /**
     * Return the wave status of the given cell.
     *
     * @param x The x coordinate of the cell.
     * @param y The y coordinate of the cell.
     * @return An integer representing the state of the cell.
     * <ul>
     *     <li>-1 means that object is doing nothing.</li>
     *     <li>-2 means that object is dead.</li>
     *     <li>1, 2, 3 means that object is in waveType1, 2, 3 respectively.</li>
     * </ul>
     */
    public int getStateWaveCell(int x, int y) {
        return waveEffect.getStateMatrix().get(x, y);
    }

    public void printBrickTypes() {
        int rows = matrixObj.rows();
        int cols = matrixObj.columns();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Brick brick = matrixObj.get(r, c);
                if (brick != null) {
                    System.out.print(brick.getType() + "\t");
                } else {
                    System.out.print("EMPTY\t");
                }
            }
            System.out.println();
        }
    }
}