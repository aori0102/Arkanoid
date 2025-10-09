package game.Brick;

import game.Brick.BrickEvent.BrokenEvent;
import game.Brick.BrickEvent.CollisionEvent;
import game.Brick.BrickEvent.EndEvent;
import game.Brick.BrickEvent.WaveEffect;
import game.Brick.BrickGenMap.GenMap;

public class BrickFactory extends InitBrick {

    final int incHealthConst = 5;

    private int level;
    private String kindMap;

    private CollisionEvent collisionEvent;
    private BrokenEvent brokenEvent;
    private EndEvent endEvent;
    private WaveEffect waveEffect;

    public BrickFactory(int rows, int cols, int level, String kindMap) {
        rowData = rows;
        colData = cols;
        this.level = level;
        this.kindMap = kindMap;
        setup();
    }

    public void setup() {


        matrixObj = new BrickMatrix(rowData, colData, new BrickObj(BrickObj.typeBrick.Normal) );

        collisionEvent = new CollisionEvent();
        brokenEvent = new BrokenEvent();
        endEvent = new EndEvent();
        waveEffect = new WaveEffect();
    }

    public void handleCollision(IntPair brickPosition, int damage) {
        int row = brickPosition.fi();
        int col = brickPosition.se();

        if (matrixObj.inBounds(row, col) && !matrixObj.isDestroyed(row, col)) {
            BrickObj.Type type = matrixObj.getObjType(row, col);
            collisionEvent.ColliEvent(row, col, type, damage);
        }
    }

    public void runProgress() {
        brokenEvent.runProgcess();
        waveEffect.getListObjHitDamage();
        waveEffect.runAllWave();
    }

    public boolean isGameFinished() {
        return endEvent.isEnd();
    }

    public BrickMatrix getBrickMatrix() {
        return matrixObj;
    }
}