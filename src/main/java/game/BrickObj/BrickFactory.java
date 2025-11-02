package game.BrickObj;

import game.BrickObj.BrickEvent.EndEvent;
import game.BrickObj.BrickEvent.WaveEffect;
import game.Brick.BrickGenMap.GenMap;
import game.Brick.BrickGenMap.MapStyle;
import org.GameObject.GameObjectManager;

import static game.BrickObj.Init.*;
import static game.BrickObj.Init.getNewBrick;

public class BrickFactory {

    private final double difficult;
    private final MapStyle kindMap;
    private int gameTime = 0;

    private EndEvent endEvent;
    private WaveEffect waveEffect;

    public BrickFactory(int rows, int cols, MapStyle _kindMap, double _difficult) {
        rowData = rows;
        colData = cols;
        this.difficult = _difficult;
        this.kindMap = _kindMap;
        gameTime = 0;
        setup();
    }

    public void setup() {
        var brickObject = GameObjectManager.instantiate("Brick");
        var brickComponent = brickObject.addComponent(Brick.class);
        brickComponent.setType(BrickType.Normal);
        matrixObj = new BrickMatrix(rowData, colData, brickComponent);

        GenMap gen = new GenMap(rowData, colData);
//        matrixObj.transIntToBrick(gen.generate(kindMap, difficult));

        endEvent = new EndEvent();
        waveEffect = new WaveEffect();
    }

    public void deleted() {
        matrixObj.deleted();
    }


    public void setupNewBrick(int x, int y, BrickType brickType) {
        matrixObj.set(x, y, getNewBrick(brickType));
    }

    public void runProgress() {
        gameTime++;
        if(gameTime % 5 == 0) {
            waveEffect.collectJustDamaged();
            waveEffect.runAllWave();
        }
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