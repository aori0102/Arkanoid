package game.Brick;

import game.Brick.BrickEvent.CollisionEvent;
import game.Brick.BrickEvent.EndEvent;
import game.Brick.BrickEvent.WaveEffect;
import org.GameObjectManager;
import static game.Brick.InitMatrix.*;

public class BrickFactory {

    final int incHealthConst = 5;

    private int level;
    private String kindMap;

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
        var brickObject = GameObjectManager.instantiate("Brick");
        var brickComponent = brickObject.addComponent(BrickObj.class);
        brickComponent.setType(BrickObj.BrickType.Normal);
        matrixObj = new BrickMatrix(rowData, colData, brickComponent);

        endEvent = new EndEvent();
        waveEffect = new WaveEffect();
    }

    public void handleCollision(IntPair brickPosition, int damage) {
        int row = brickPosition.fi();
        int col = brickPosition.se();

        if (matrixObj.inBounds(row, col) && !matrixObj.isDestroyed(row, col)) {
            BrickObj.BrickType brickType = matrixObj.getObjType(row, col);
        }
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
}