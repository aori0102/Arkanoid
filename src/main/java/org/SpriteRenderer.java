package org;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Vector2;

public class SpriteRenderer extends MonoBehaviour {

    protected ImageView sprite;
    private Vector2 imageOriginalDimension;
    private Vector2 imageSize;
    private Vector2 pivot;

    /**
     * Set the pivot for rendering image, indicating the offset of
     * the original image point to the transform. Pivot is within
     * {@code (0, 0)} and {@code (1, 1)}, with {@code (0, 0)} being
     * the top left corner and {@code (1, 1)} being the bottom
     * right corner.
     *
     * @param pivot The pivot desired for the image.
     */
    public void setPivot(Vector2 pivot) {
        pivot.x = Math.clamp(pivot.x, 0.0, 1.0);
        pivot.y = Math.clamp(pivot.y, 0.0, 1.0);
        this.pivot = pivot;
    }

    /**
     * Set the size for the rendered image in pixel.
     *
     * @param imageSize The size in pixel.
     */
    public void setImageSize(Vector2 imageSize) {
        this.imageSize = imageSize;
    }

    /**
     * Get the current rendering size.
     *
     * @return The rendering size of the image.
     */
    public Vector2 getImageSize() {
        return new Vector2(imageSize);
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
            imageSize = new Vector2(imageOriginalDimension);
        } catch (Exception e) {
            throw new RuntimeException("Image not found: " + e);
        }

    }

    /**
     * Remove the image for this Renderer.
     */
    public void resetImage() {
        sprite.setImage(null);
        imageSize = new Vector2();
        imageOriginalDimension = new Vector2();
    }

    @Override
    public void update() {
        var transform = getTransform();
        var position = transform.getGlobalPosition();
        var size = transform.getGlobalScale().scaleUp(imageSize);
        position = position.subtract(pivot.scaleUp(size));
        sprite.setX(position.x);
        sprite.setY(position.y);
        sprite.setFitWidth(size.x);
        sprite.setFitHeight(size.y);
    }

    public SpriteRenderer(GameObject owner) {
        super(owner);
        sprite = new ImageView();
        imageOriginalDimension = new Vector2();
        imageSize = new Vector2();
        pivot = new Vector2();
        RendererManager.RegisterNode(sprite);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new SpriteRenderer(newOwner);
    }

    @Override
    protected void destroyComponent() {
        RendererManager.UnregisterNode(sprite);
        sprite = null;
        pivot = null;
        imageSize = null;
        imageOriginalDimension = null;
    }

}
