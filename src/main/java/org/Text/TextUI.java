package org.Text;

import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.GameObject.GameObject;
import org.GameObject.Transform;
import org.Rendering.Renderable;
import utils.Vector2;

public class TextUI extends Renderable {

    private static final String DEFAULT_FONT_FACE = "System";
    private static final double DEFAULT_FONT_SIZE = 12.0;

    private Text text = new Text();
    private double fontSize = DEFAULT_FONT_SIZE;
    private String fontName = DEFAULT_FONT_FACE;
    private TextVerticalAlignment verticalAlignment = TextVerticalAlignment.Top;
    private TextHorizontalAlignment horizontalAlignment = TextHorizontalAlignment.Left;
    private Color textColor = Color.BLACK;

    public TextUI(GameObject owner) {
        super(owner);

        onRenderPositionChanged.addListener(this::renderer_onRenderPositionChanged);
        onRenderSizeChanged.addListener(this::renderable_onRenderSizeChanged);
        text.setTextOrigin(VPos.TOP);

    }

    /**
     * Called when {@link Renderable#onRenderSizeChanged} is invoked.<br><br>
     * This function adjust the text size to match with the object's scale.
     *
     * @param sender Event caller {@link Renderable}.
     * @param e      Empty event argument.
     */
    private void renderable_onRenderSizeChanged(Object sender, Void e) {
        updateFont();
    }

    /**
     * Called from {@link #onRenderPositionChanged} when this UI's render
     * position is modified. This function update the render position
     * of the text component.
     *
     * @param sender {@link  Renderable}.
     * @param e      Empty event argument.
     */
    private void renderer_onRenderPositionChanged(Object sender, Void e) {
        updateRenderPosition();
    }

    /**
     * Update the current font based on the set name and size.
     */
    private void updateFont() {
        var scale = getTransform().getLocalScale();
        text.setFont(Font.font(fontName, fontSize * Math.max(scale.x, scale.y)));
        text.setFill(textColor);
        updateRenderPosition();
    }

    /**
     * Get the display text.
     *
     * @return The display text.
     */
    public Text getText() {
        return text;
    }

    /**
     * Set the display text.
     *
     * @param text The text to be displayed.
     */
    public void setText(String text) {
        this.text.setText(text);
        updateRenderPosition();
    }

    /**
     * Get the display text.
     *
     * @return The display text.
     */
    public String getTextString() {
        return text.getText();
    }

    /**
     * Get the text bounding box dimension.
     *
     * @return The text bounding box dimension.
     */
    public Vector2 getTextDimension() {
        return new Vector2(getTextWidth(), getTextHeight());
    }

    /**
     * Get the text bounding box width.
     *
     * @return The text bounding box width.
     */
    public double getTextWidth() {
        return text.getBoundsInLocal().getWidth();
    }

    /**
     * Get the text bounding box height.
     *
     * @return The text bounding box height.
     */
    public double getTextHeight() {
        return text.getBoundsInLocal().getHeight();
    }

    /**
     * Set the font for this text.
     *
     * @param fontDataIndex The font index to be displayed.
     */
    public void setFont(FontDataIndex fontDataIndex) {
        fontName = fontDataIndex.getFontName();
        updateFont();
    }

    /**
     * Set the render color for this text.
     *
     * @param textColor The color for the text.
     */
    public void setTextColor(Color textColor) {
        this.textColor = textColor;
        updateFont();
    }

    /**
     * Set the font size for this text. The text's font size is also affected
     * by the objects local scale, via {@link Transform#getLocalScale()}. The
     * scale based on whichever value between {@code x} and {@code y} is greater.
     *
     * @param size The size of the displayed font.
     */
    public void setFontSize(double size) {
        fontSize = size;
        updateFont();
    }

    /**
     * Set text vertical alignment.
     *
     * @param alignment The vertical alignment of the displayed text.
     */
    public void setVerticalAlignment(TextVerticalAlignment alignment) {
        verticalAlignment = alignment;
        updateRenderPosition();
    }

    /**
     * Set text horizontal alignment.
     *
     * @param alignment The horizontal alignment of the displayed text.
     */
    public void setHorizontalAlignment(TextHorizontalAlignment alignment) {
        horizontalAlignment = alignment;
        updateRenderPosition();
    }

    /**
     * Utility function to update true render position
     * of the text after pivoting by alignments.
     */
    private void updateRenderPosition() {

        var pivot = new Vector2();
        pivot.x = switch (horizontalAlignment) {
            case Left -> 0.0;
            case Center -> 0.5;
            case Right -> 1.0;
        };
        pivot.y = switch (verticalAlignment) {
            case Top -> 0.0;
            case Middle -> 0.5;
            case Bottom -> 1.0;
        };
        var renderPosition
                = getRenderPosition().subtract(pivot.scaleUp(getTextDimension()));
        text.setX(renderPosition.x);
        text.setY(renderPosition.y);

    }

    @Override
    public Node getNode() {
        return text;
    }

}
