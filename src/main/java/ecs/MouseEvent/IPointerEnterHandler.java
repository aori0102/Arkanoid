package ecs.MouseEvent;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface IPointerEnterHandler {
    /**
     * Provides a contract for handling pointer (mouse) enter events on JavaFX nodes or object's image.
     * <p>
     * Classes that implement this interface must override {@link #onPointerEntered()}
     * to define the behavior that occurs when a node is entered.
     * </p>
     */
    public void onPointerEntered();
    /**
     * Attach area that the event will occur.
     *
     * @param node Area that you want the event to occur on.
     *             <p>
     *             If the node has an instance of an {@link ImageView} type, the area of that image will be used.
     *             (Recommend to use {@link ImageView} to initialize effective area).
     *             </p>
     */
    default void attachPointerEnter(Node node) {
        if(node instanceof ImageView imageView){
            Image image = imageView.getImage();
            imageView.setOnMouseEntered(e -> {
                int x = (int) e.getX();
                int y = (int) e.getY();

                // Check transparency at clicked pixel
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    int argb = image.getPixelReader().getArgb(x, y);
                    int alpha = (argb >> 24) & 0xff;

                    if (alpha > 0) {
                        onPointerEntered();
                    } else {
                        e.consume(); // ignore transparent clicks
                    }
                }
            });
        }
        else {
            node.setOnMouseEntered(e -> onPointerEntered());
        }
    }
}
