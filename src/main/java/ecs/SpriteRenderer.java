package ecs;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Vector2;

public class SpriteRenderer extends MonoBehaviour {

    protected ImageView sprite;
    private Vector2 imageDimension;

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
            java.io.InputStream stream = getClass().getResourceAsStream(path);
            if (stream == null) {
                throw new Exception("Path stream is null");
            }
            Image image = new Image(stream);
            sprite.setImage(image);
            imageDimension = new Vector2(image.getWidth(), image.getHeight());
        } catch (Exception e) {
            throw new RuntimeException("Image not found: " + e);
        }

    }

    /**
     * Remove the image for this Renderer.
     */
    public void resetImage() {
        sprite.setImage(null);
    }

    @Override
    public void update() {
        var transform = transform();
        var position = transform.getGlobalPosition();
        var size = transform.getGlobalScale().scaleUp(imageDimension);
        sprite.setX(position.x);
        sprite.setY(position.y);
        sprite.setFitWidth(size.x);
        sprite.setFitHeight(size.y);
    }

    public SpriteRenderer(GameObject owner) {
        super(owner);
        sprite = new ImageView();
        imageDimension = new Vector2();
        RendererManager.RegisterNode(sprite);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new SpriteRenderer(newOwner);
    }

    @Override
    protected void clear() {
        RendererManager.UnregisterNode(sprite);
    }

}
