package game.BrickObj;
import game.Brick.BrickGenMap.MapStyle;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Vector2;


public class BrickManager extends MonoBehaviour {

    public static final Vector2 BRICK_SIZE = new Vector2(64.0, 32.0);
    public static final Vector2 BRICK_MAP_ANCHOR = new Vector2(300.0, 100.0);

    private static BrickManager instance = null;

    private BrickFactory brickFactory = null;

    int timeFrame = 0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public BrickManager(GameObject owner) {
        super(owner);
        if (instance != null) {
            throw new IllegalStateException("BrickManager is a singleton!");
        }
        brickFactory = new BrickFactory(10, 10, MapStyle.HEART, 0.3);
        brickFactory.printBrickTypes();
        instance = this;
    }

    public void buildMap(int row, int col, MapStyle mapStyle, double diff) {
        brickFactory = new BrickFactory(row, col, mapStyle, diff);
        brickFactory.printBrickTypes();
    }

    public static BrickManager getInstance() {
        return instance;
    }

    public void deleted() {
        brickFactory.deleted();
    }

    public void setUpBrick(int x, int y, BrickType brickType) {
        brickFactory.setupNewBrick(x, y, brickType);
    }

    @Override
    public void update() {
        brickFactory.runProgress();
//            timeFrame++;
//            if(timeFrame == 600) {
//                brickFactory.deleted();
//            }
//
//            if(timeFrame == 1200) {
//                brickFactory = new BrickFactory(10, 10, MapStyle.RANDOM, 0.3);
//            }
    }


}