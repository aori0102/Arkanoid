package org;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import utils.Vector2;

public class SpriteRenderer extends MonoBehaviour implements IHasNode {

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
    private Vector2 _imageSize;
    private Vector2 _imageGlobalSize;
    private Vector2 _imageGlobalAnchor;
    private FillType _fillType;
    private RadialStartPoint _radialStartPoint;
    private double _fillAmount;

    private double rotateAngle;
    private Vector2 imageOriginalDimension;
    private Vector2 pivot;
    private Rectangle rectangularClip;
    protected ImageView sprite;
    private Rotate rotate;
    private Arc circularClip;

    public SpriteRenderer(GameObject owner) {
        super(owner);
        sprite = new ImageView();
        imageOriginalDimension = new Vector2();
        _imageSize = new Vector2();
        pivot = new Vector2();
        rotate = new Rotate();
        rotateAngle = 0.0;
        sprite.getTransforms().add(rotate);
        RendererManager.registerNode(sprite);
        _fillType = FillType.None;
        _radialStartPoint = RadialStartPoint.Top;
        _fillAmount = 1.0;
        rectangularClip = new Rectangle();
        circularClip = new Arc();
        circularClip.setType(ArcType.ROUND);
        _imageGlobalSize = new Vector2();
        _imageGlobalAnchor = new Vector2();
        getTransform().onPositionChanged.addListener(this::transform_onPositionChanged);
        getTransform().onScaleChanged.addListener(this::transform_onScaleChanged);
    }

    /**
     * Set the pivot for rendering image, indicating the offset of
     * the original image point to the transform. Pivot is within
     * {@code (0, 0)} and {@code (1, 1)}, with {@code (0, 0)} being
     * the top left corner and {@code (1, 1)} being the bottom
     * right corner. This affect the sprite's rotation as well.
     *
     * @param pivot The pivot desired for the image.
     */
    public void setPivot(Vector2 pivot) {
        pivot.x = Math.clamp(pivot.x, 0.0, 1.0);
        pivot.y = Math.clamp(pivot.y, 0.0, 1.0);
        this.pivot = pivot;
        updateRotate();
    }

    /**
     * Set the size for the rendered image in pixel.
     *
     * @param imageSize The size in pixel.
     */
    public void setImageSize(Vector2 imageSize) {

        validateComponentCompatibility();

        this._imageSize = imageSize;
        setImageGlobalSize(imageSize.scaleUp(getTransform().getGlobalScale()));

    }

    /**
     * Set the rotation for this image rendering.
     *
     * @param degrees The angle in degree.
     */
    public void setImageRotation(double degrees) {

        validateComponentCompatibility();

        rotateAngle = degrees;
        updateRotate();

    }

    /**
     * Set the image for this Renderer.
     *
     * @param path The path of the image. All image will be
     *             within the folder 'resources', so {@code path} should
     *             begin with a {@code \}, meaning from the root
     *             of 'resources' folder.
     *
     */
    public void setImage(String path) {

        validateComponentCompatibility();

        try {
            // Get the resource folder
            java.io.InputStream stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                throw new Exception("Path stream is null");
            }
            // Set image and sprite
            Image image = new Image(stream);
            sprite.setImage(image);
            imageOriginalDimension = new Vector2(image.getWidth(), image.getHeight());
            setImageSize(imageOriginalDimension);
        } catch (Exception e) {
            throw new RuntimeException("Image not found: " + e);
        }

    }

    /**
     * Set the fill amount for image fill.
     *
     * @param fillAmount The fill amount from {@code 0.0} (empty)
     *                   to {@code 1.0} (full).
     */
    public void setFillAmount(double fillAmount) {

        validateComponentCompatibility();

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

        validateComponentCompatibility();

        this._fillType = fillType;
        onFillTypeChanged();

    }

    /**
     * Set the start point for radial image fill.
     *
     * @param radialStartPoint The start point of the image fill.
     */
    public void setRadialStartPoint(RadialStartPoint radialStartPoint) {

        validateComponentCompatibility();

        this._radialStartPoint = radialStartPoint;
        onRadialStartPointChanged();

    }

    /**
     * Setter for read-only field {@link #_imageGlobalSize}.
     * Calls {@link #onImageGlobalSizeChanged()} as a result.
     *
     * @param size The global size to set.
     */
    private void setImageGlobalSize(Vector2 size) {
        this._imageGlobalSize = new Vector2(size);
        onImageGlobalSizeChanged();
    }

    /**
     * Setter for read-only field {@link #_imageGlobalAnchor}.
     * Calls {@link #onImageGlobalAnchorChanged()} as a result.
     *
     * @param anchor The global anchor of the image.
     */
    private void setImageGlobalAnchor(Vector2 anchor) {
        this._imageGlobalAnchor = new Vector2(anchor);
        onImageGlobalAnchorChanged();
    }

    /**
     * Get the current rendering size.
     *
     * @return The rendering size of the image.
     */
    public Vector2 getImageSize() {
        return new Vector2(_imageSize);
    }

    /**
     * Remove the image for this Renderer.
     */
    public void resetImage() {

        validateComponentCompatibility();

        sprite.setImage(null);
        setImageSize(new Vector2());
        imageOriginalDimension = new Vector2();

    }

    /**
     * Override the current image of this SpriteRenderer,
     * can only be called by {@link SpriteAnimator}.
     *
     * @param image The image to override.
     */
    protected void overrideImage(Image image) {
        sprite.setImage(image);
        imageOriginalDimension = new Vector2(image.getWidth(), image.getHeight());
        setImageSize(imageOriginalDimension);
    }

    /**
     * Override the current rotation of this SpriteRenderer,
     * can only be called by {@link SpriteAnimator}.
     *
     * @param degrees The angle of rotation in degrees.
     */
    protected void overrideRotation(double degrees) {
        rotateAngle = degrees;
        updateRotate();
    }

    /**
     * Override the current viewport of this SpriteRenderer,
     * can only be called by {@link SpriteAnimator}.
     *
     * @param anchor The top left point of the clip.
     * @param size   The size from the {@code anchor} of the clip.
     */
    protected void overrideClip(Vector2 anchor, Vector2 size) {
        sprite.setViewport(new Rectangle2D(anchor.x, anchor.y, size.x, size.y));
        setImageSize(size);
    }

    /**
     * Override the current render size of the image from
     * this SpriteRenderer, can only be called within
     * {@link SpriteAnimator}.
     *
     * @param renderSize The render size of the image.
     */
    protected void overrideRenderSize(Vector2 renderSize) {
        setImageSize(new Vector2(renderSize));
    }

    /**
     * Called when the {@link #_imageSize} is modified.
     * This function update {@link #_imageGlobalSize}, then
     * adjust the clip ({@link #rectangularClip}, {@link #circularClip}
     * accordingly.
     */
    private void onImageGlobalSizeChanged() {

        var width = _imageGlobalSize.x;
        var height = _imageGlobalSize.y;
        sprite.setFitWidth(width);
        sprite.setFitHeight(height);

        updateRectangularClip();
        updateCircularClip();

    }

    /**
     * Called when the {@link #_imageGlobalAnchor} is modified. This
     * function reposition the sprite and clips to match the new
     * position.
     */
    private void onImageGlobalAnchorChanged() {

        var x = _imageGlobalAnchor.x;
        var y = _imageGlobalAnchor.y;
        sprite.setX(x);
        sprite.setY(y);

        updateRectangularClip();
        updateCircularClip();

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
     * Called from {@link Transform#onPositionChanged} when the position
     * of the {@link  Transform} attached with this MonoBehaviour changes.
     *
     * @param sender {@link Transform}.
     * @param e      An empty event argument.
     */
    private void transform_onPositionChanged(Object sender, Void e) {

        var transform = getTransform();
        var position = transform.getGlobalPosition();
        position = position.subtract(pivot.scaleUp(_imageGlobalSize));
        setImageGlobalAnchor(position);

    }

    /**
     * Called from {@link Transform#onScaleChanged} when the scale
     * of the {@link  Transform} attached with this MonoBehaviour changes.
     *
     * @param sender {@link Transform}.
     * @param e      An empty event argument.
     */
    private void transform_onScaleChanged(Object sender, Void e) {

        var transform = getTransform();
        var size = transform.getGlobalScale().scaleUp(_imageSize);
        setImageGlobalSize(size);

    }

    /**
     * Update the rotation rendering for this image.
     */
    private void updateRotate() {

        var pivotByDimension = imageOriginalDimension.scaleUp(pivot);
        rotate.setAngle(rotateAngle);
        rotate.setPivotX(pivotByDimension.x);
        rotate.setPivotY(pivotByDimension.y);

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
     * Validate the components attached along with this {@code MonoBehaviour}
     * to check whether this component is driven by another. Throw {@link IllegalStateException}
     * if there is any.
     */
    private void validateComponentCompatibility() {

        if (getComponent(SpriteAnimator.class) != null) {
            throw new IllegalStateException("This SpriteRenderer is being driven by SpriteAnimator");
        }

    }

    /**
     * Update {@link #rectangularClip}'s property, including
     * its position, size and fill according to {@link #_fillType}.
     */
    private void updateRectangularClip() {

        var x = _imageGlobalAnchor.x;
        var y = _imageGlobalAnchor.y;
        var width = _imageGlobalSize.x;
        var height = _imageGlobalSize.y;

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

        var x = _imageGlobalAnchor.x;
        var y = _imageGlobalAnchor.y;
        var width = _imageGlobalSize.x;
        var height = _imageGlobalSize.y;
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

    @Override
    public void update() {
        if (_fillType == FillType.Radial_Clockwise) {
            System.out.println("Anchor: " + _imageGlobalAnchor.x + ", " + _imageGlobalAnchor.y + ", " + _imageGlobalSize.x + ", " + _imageGlobalSize.y);
            System.out.println("Clip: " + circularClip.getCenterX() + ", " + circularClip.getCenterY() + ", " + circularClip.getRadiusX() + ", " + circularClip.getRadiusY());
            System.out.println("Start: " + circularClip.getStartAngle() + " | Length: " + circularClip.getLength());
        }
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new SpriteRenderer(newOwner);
    }

    @Override
    protected void destroyComponent() {
        RendererManager.unregisterNode(sprite);
        sprite = null;
        pivot = null;
        _imageSize = null;
        imageOriginalDimension = null;
        rectangularClip = null;
        circularClip = null;
        rotate = null;
        _imageGlobalSize = null;
        _imageGlobalAnchor = null;
    }

    @Override
    public Node getNode() {
        return sprite;
    }

}
