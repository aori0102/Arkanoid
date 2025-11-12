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

/**
 * Represents a single row item (UI element) in the high score/record menu.
 * <p>
 * This component manages the display of a specific player record, including its
 * background, ranking number, score, and statistics. It initializes all necessary
 * {@link SpriteRenderer} and {@link TextUI} components.
 */
public class RecordUI extends MonoBehaviour {

    private final TextUI recordRanking;
    private final TextUI rank;
    private final TextUI score;
    private final TextUI numberOfBrickDestroyed;
    private final TextUI levelCleared;
    private final SpriteRenderer background;

    private final Vector2 pivot = new Vector2(0.5, 0.5);

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

    /**
     * Sets the display text for the record's position in the leaderboard.
     *
     * @param recordRanking The rank number (e.g., 1, 2, 3).
     */
    public void setRecordRanking(int recordRanking) {
        this.recordRanking.setText(String.valueOf(recordRanking));
    }

    /**
     * Sets the display text for the player's rank achieved during the recorded session.
     *
     * @param rank The rank value.
     */
    public void setRank(int rank) {
        this.rank.setText(String.valueOf(rank));
    }

    /**
     * Sets the display text for the total score achieved.
     *
     * @param score The score value.
     */
    public void setScore(int score) {
        this.score.setText(String.valueOf(score));
    }

    /**
     * Sets the display text for the maximum level cleared.
     *
     * @param levelCleared The level number.
     */
    public void setLevelCleared(int levelCleared) {
        this.levelCleared.setText(String.valueOf(levelCleared));
    }

    /**
     * Sets the display text for the total number of bricks destroyed.
     *
     * @param numberOfBrickDestroyed The brick count.
     */
    public void setNumberOfBrickDestroyed(int numberOfBrickDestroyed) {
        this.numberOfBrickDestroyed.setText(String.valueOf(numberOfBrickDestroyed));
    }

    /**
     * Sets the current GameObject as the parent for all instantiated child components.
     * This ensures the entire record row moves as a single unit.
     */
    private void setParent() {
        background.getGameObject().setParent(getGameObject());
        recordRanking.getGameObject().setParent(getGameObject());
        rank.getGameObject().setParent(getGameObject());
        score.getGameObject().setParent(getGameObject());
        numberOfBrickDestroyed.getGameObject().setParent(getGameObject());
        levelCleared.getGameObject().setParent(getGameObject());
    }

    /**
     * Sets the render layer for the background and all text fields.
     * The background is set lower than the text fields to ensure proper layering.
     */
    private void setRenderLayer() {
        background.setRenderLayer(RenderLayer.UI_Bottom);
        recordRanking.setRenderLayer(RenderLayer.UI_Middle);
        rank.setRenderLayer(RenderLayer.UI_Middle);
        score.setRenderLayer(RenderLayer.UI_Middle);
        numberOfBrickDestroyed.setRenderLayer(RenderLayer.UI_Middle);
        levelCleared.setRenderLayer(RenderLayer.UI_Middle);
    }

    /**
     * Sets the pivot point (center) for the background component.
     */
    private void setPivot() {
        background.setPivot(pivot);
    }

    /**
     * Applies standard visual styling to a {@link TextUI} component used in the record row.
     *
     * @param text The {@link TextUI} component to style.
     */
    private void setTextStyle(TextUI text) {
        text.setFontSize(50);
        text.setFont(FontDataIndex.Jersey_25);
        text.setGlow();
        text.setSolidFill(Color.YELLOW);

        text.setVerticalAlignment(TextVerticalAlignment.Middle);
        text.setHorizontalAlignment(TextHorizontalAlignment.Center);
    }
}
