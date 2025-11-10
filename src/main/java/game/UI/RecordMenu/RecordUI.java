package game.UI.RecordMenu;

import javafx.scene.paint.Color;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Main;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

public class RecordUI extends MonoBehaviour {

    private TextUI recordRanking;
    private TextUI rank;
    private TextUI score;
    private TextUI numberOfBrickDestroyed;
    private TextUI levelCleared;
    private SpriteRenderer background;

    private Vector2 pivot = new Vector2(0.5, 0.5);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public RecordUI(GameObject owner) {
        super(owner);

        background = GameObjectManager.instantiate("Background")
                .addComponent(SpriteRenderer.class);
        recordRanking = GameObjectManager.instantiate("RecordRanking")
                .addComponent(TextUI.class);
        rank = GameObjectManager.instantiate("Rank")
                .addComponent(TextUI.class);
        score = GameObjectManager.instantiate("Score")
                .addComponent(TextUI.class);
        numberOfBrickDestroyed = GameObjectManager.instantiate("NumberOfBrickDestroyed")
                .addComponent(TextUI.class);
        levelCleared = GameObjectManager.instantiate("LevelCleared")
                        .addComponent(TextUI.class);

        setParent();
        setPivot();
        setRenderLayer();
        setTextStyle(recordRanking);
        setTextStyle(rank);
        setTextStyle(score);
        setTextStyle(numberOfBrickDestroyed);
        setTextStyle(levelCleared);


        background.setImage(ImageAsset.ImageIndex.RecordUI.getImage());
        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH / 2, Main.STAGE_HEIGHT / 2));

        recordRanking.getTransform().setLocalPosition(new Vector2(-355, 0));
        rank.getTransform().setLocalPosition(new Vector2(-255, 0));
        score.getTransform().setLocalPosition(new Vector2(-25, 0));
        numberOfBrickDestroyed.getTransform().setLocalPosition(new Vector2(207, 0));
        levelCleared.getTransform().setLocalPosition(new Vector2(330, 0));




    }

    public void setRecordRanking(int recordRanking) {
        this.recordRanking.setText(String.valueOf(recordRanking));
    }

    public void setRank(int rank) {
        this.rank.setText(String.valueOf(rank));
    }

    public void setScore(int score) {
        this.score.setText(String.valueOf(score));
    }

    public void setLevelCleared(int levelCleared) {
        this.levelCleared.setText(String.valueOf(levelCleared));
    }

    public void setNumberOfBrickDestroyed(int numberOfBrickDestroyed) {
        this.numberOfBrickDestroyed.setText(String.valueOf(numberOfBrickDestroyed));
    }

    private void setParent(){
        background.getGameObject().setParent(getGameObject());
        recordRanking.getGameObject().setParent(getGameObject());
        rank.getGameObject().setParent(getGameObject());
        score.getGameObject().setParent(getGameObject());
        numberOfBrickDestroyed.getGameObject().setParent(getGameObject());
        levelCleared.getGameObject().setParent(getGameObject());
    }

    private void setRenderLayer(){
        background.setRenderLayer(RenderLayer.UI_Bottom);
        recordRanking.setRenderLayer(RenderLayer.UI_Middle);
        rank.setRenderLayer(RenderLayer.UI_Middle);
        score.setRenderLayer(RenderLayer.UI_Middle);
        numberOfBrickDestroyed.setRenderLayer(RenderLayer.UI_Middle);
        levelCleared.setRenderLayer(RenderLayer.UI_Middle);
    }

    private void setPivot(){
        background.setPivot(pivot);
//        recordRanking.setPivot(pivot);
//        rank.setPivot(pivot);
//        score.setPivot(pivot);
//        numberOfBrickDestroyed.setPivot(pivot);
//        levelCleared.setPivot(pivot);
    }

    private void setTextStyle(TextUI text){
        text.setFontSize(50);
        text.setFont(FontDataIndex.Jersey_25);
        text.setGlow();
        text.setSolidFill(Color.YELLOW);

        text.setVerticalAlignment(TextVerticalAlignment.Middle);
        text.setHorizontalAlignment(TextHorizontalAlignment.Center);
    }
}
