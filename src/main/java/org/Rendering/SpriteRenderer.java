package org.Rendering;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import utils.Vector2;

public class SpriteRenderer extends Renderable {

    private static final double RADIAL_FULL_DEGREE = 360.0;

    public enum FillType {
        None,
        Vertical_TopToBottom,
        Vertical_BottomToTop,
        Horizontal_LeftToRight,
        Horizontal_RightToLeft,
        Radial_Clockwise,
        Radial_CounterClockwise,
    }

    public enum RadialStartPoint {
        Top(90.0),
        Bottom(270.0),
        Left(180.0),
        Right(0.0);

        private final double startAngle;

        RadialStartPoint(double startAngle) {
            this.startAngle = startAngle;
        }

        public double getStartAngle() {
            return startAngle;
        }

    }

    /// Read-only, mark with prefix '_', can only be written in respective setters
    private FillType _fillType = FillType.None;
    private RadialStartPoint _radialStartPoint = RadialStartPoint.Top;
    private double _fillAmount = 1.0;

    private Vector2 imageOriginalDimension = new Vector2();
    private Rectangle rectangularClip = new Rectangle();
    protected ImageView sprite = new ImageView();
    private Rotate rotateProperty = new Rotate();
    private Arc circularClip = new Arc();

    public SpriteRenderer(GameObject owner) {

        super(owner);

        sprite.getTransforms().add(rotateProperty);
        circularClip.setType(ArcType.ROUND);

        onRenderPositionChanged.addListener(this::renderable_onRenderPositionChanged);
        onRenderSizeChanged.addListener(this::renderable_onRenderSizeChanged);
        onPivotChanged.addListener(this::renderable_onPivotChanged);

        updateRenderPosition();
        updateRenderSize();
        updateRotationPivot();

    }

    private void updateRenderPosition() {
        var renderPosition = getRenderPosition();
        sprite.setX(renderPosition.x);
        sprite.setY(renderPosition.y);
    }

    private void updateRenderSize() {
        var renderSize = getRenderSize();
        sprite.setFitWidth(renderSize.x);
        sprite.setFitHeight(renderSize.y);
    }

    private void renderable_onRenderPositionChanged(Object sender, Void e) {
        updateRenderPosition();
        updateRotationPivot();
    }

    private void renderable_onRenderSizeChanged(Object sender, Void e) {
        updateRenderSize();
        updateRotationPivot();
    }

    private void renderable_onPivotChanged(Object sender, Void e) {
        updateRenderPosition();
        updateRotationPivot();
    }

    /**
     * Set the rotation for this image rendering.
     *
     * @param degrees The angle in degree.
     */
    public void setImageRotation(double degrees) {
        rotateProperty.setAngle(degrees);
    }

    /**
     * Set the image for this Renderer.
     *
     * @param image The image to set for this Renderer.
     */
    public void setImage(Image image) {
        sprite.setImage(image);
        imageOriginalDimension = new Vector2(image.getWidth(), image.getHeight());
        setSize(imageOriginalDimension);
        updateRotationPivot();
    }

    private void updateRotationPivot() {
        var globalPivot = getPivotPoint();
        rotateProperty.setPivotX(globalPivot.x);
        rotateProperty.setPivotY(globalPivot.y);
    }

    /**
     * Set the fill amount for image fill.
     *
     * @param fillAmount The fill amount from {@code 0.0} (empty)
     *                   to {@code 1.0} (full).
     */
    public void setFillAmount(double fillAmount) {
        this._fillAmount = Math.clamp(fillAmount, 0.0, 1.0);
        onFillAmountChanged();
    }

    /**
     * Set the fill type.
     * <ul>
     * <li>{@link FillType#None} No filling.</li>
     * <li>{@link FillType#Vertical_TopToBottom} Rectangular fill, from Top to Bottom.</li>
     * <li>{@link FillType#Vertical_BottomToTop} Rectangular fill, from Bottom to Top.</li>
     * <li>{@link FillType#Horizontal_LeftToRight} Rectangular fill, from Left to Right.</li>
     * <li>{@link FillType#Horizontal_RightToLeft} Rectangular fill, from Right to Left.</li>
     * <li>{@link FillType#Radial_Clockwise} Circular fill, Clockwise.</li>
     * <li>{@link FillType#Radial_CounterClockwise} Circular fill, Counterclockwise.</li>
     * </ul>
     *
     * @param fillType The type of image fill.
     */
    public void setFillType(FillType fillType) {
        this._fillType = fillType;
        onFillTypeChanged();
    }

    /**
     * Set the start point for radial image fill.
     *
     * @param radialStartPoint The start point of the image fill.
     */
    public void setRadialStartPoint(RadialStartPoint radialStartPoint) {
        this._radialStartPoint = radialStartPoint;
        onRadialStartPointChanged();
    }

    public void setSpriteClip(Vector2 clipAnchor, Vector2 clipSize) {
        Rectangle2D clip = new Rectangle2D(clipAnchor.x, clipAnchor.y, clipSize.x, clipSize.y);
        sprite.setViewport(clip);
    }

    /**
     * Remove the image for this Renderer.
     */
    public void resetImage() {
        sprite.setImage(null);
    }

    /**
     * Called when {@link #_fillType} is modified. This function
     * links the respective clip format according to {@link #_fillType},
     * then adjust the clip's properties to match the fill type.
     */
    private void onFillTypeChanged() {

        if (isFillLinear()) {
            sprite.setClip(rectangularClip);
        } else if (isFillRadial()) {
            sprite.setClip(circularClip);
        } else {
            sprite.setClip(null);
        }

        updateRectangularClip();
        updateCircularClip();

    }

    /**
     * Called when {@link #_radialStartPoint} is modified. This
     * function modify the starting point of the arc clip
     * {@link #circularClip} to match with the starting point.
     */
    private void onRadialStartPointChanged() {
        updateCircularClip();
    }

    /**
     * Called when {@link #_fillAmount} is modified. This function
     * adjust the clip's property to match with the image fill.
     */
    private void onFillAmountChanged() {

        updateRectangularClip();
        updateCircularClip();

    }

    /**
     * Check if the current image fill is linear, either
     * vertically or horizontally.
     *
     * @return {@code true} if {@link #_fillType} is either
     * {@link FillType#Vertical_BottomToTop}, {@link FillType#Vertical_TopToBottom},
     * {@link FillType#Horizontal_LeftToRight} or {@link FillType#Horizontal_RightToLeft},
     * otherwise {@code false}.
     */
    private boolean isFillLinear() {

        return switch (_fillType) {
            case Vertical_TopToBottom,
                 Vertical_BottomToTop,
                 Horizontal_LeftToRight,
                 Horizontal_RightToLeft -> true;
            case Radial_Clockwise,
                 Radial_CounterClockwise,
                 None -> false;
        };

    }

    /**
     * Check if the current image fill is radial.
     *
     * @return {@code true} if {@link #_fillType} is either
     * {@link FillType#Radial_Clockwise}, or {@link FillType#Radial_CounterClockwise},
     * otherwise {@code false}.
     */
    private boolean isFillRadial() {

        return switch (_fillType) {
            case Vertical_TopToBottom,
                 Vertical_BottomToTop,
                 Horizontal_LeftToRight,
                 Horizontal_RightToLeft,
                 None -> false;
            case Radial_Clockwise,
                 Radial_CounterClockwise -> true;
        };

    }

    /**
     * Update {@link #rectangularClip}'s property, including
     * its position, size and fill according to {@link #_fillType}.
     */
    private void updateRectangularClip() {

        var renderPosition = getRenderPosition();
        var renderSize = getRenderSize();
        var x = renderPosition.x;
        var y = renderPosition.y;
        var width = renderSize.x;
        var height = renderSize.y;

        switch (_fillType) {

            case Vertical_TopToBottom:

                rectangularClip.setX(x);
                rectangularClip.setY(y);
                rectangularClip.setWidth(width);
                rectangularClip.setHeight(height * _fillAmount);

                break;

            case Vertical_BottomToTop:

                rectangularClip.setX(x);
                rectangularClip.setY(y + height * (1.0 - _fillAmount));
                rectangularClip.setWidth(width);
                rectangularClip.setHeight(height * _fillAmount);

                break;

            case Horizontal_LeftToRight:

                rectangularClip.setX(x);
                rectangularClip.setY(y);
                rectangularClip.setWidth(width * _fillAmount);
                rectangularClip.setHeight(height);

                break;

            case Horizontal_RightToLeft:

                rectangularClip.setX(x + width * (1.0 - _fillAmount));
                rectangularClip.setY(y);
                rectangularClip.setWidth(width * _fillAmount);
                rectangularClip.setHeight(height);

                break;

        }

    }

    /**
     * Update {@link #circularClip}'s property, including
     * its position, radius and fill according to {@link #_fillType}.
     */
    private void updateCircularClip() {

        var renderPosition = getRenderPosition();
        var renderSize = getRenderSize();
        var x = renderPosition.x;
        var y = renderPosition.y;
        var width = renderSize.x;
        var height = renderSize.y;
        var radius = Math.sqrt(width * width + height * height) / 2.0;

        circularClip.setCenterX(x + width / 2.0);
        circularClip.setCenterY(y + height / 2.0);
        circularClip.setRadiusX(radius);
        circularClip.setRadiusY(radius);
        circularClip.setStartAngle(_radialStartPoint.getStartAngle());

        switch (_fillType) {

            case Radial_Clockwise:

                circularClip.setLength(-_fillAmount * RADIAL_FULL_DEGREE);

                break;

            case Radial_CounterClockwise:

                circularClip.setLength(_fillAmount * RADIAL_FULL_DEGREE);

                break;

        }

    }

    public Node getNode() {
        return sprite;
    }

    public ImageView getSprite() {
        return sprite;
    }
}
