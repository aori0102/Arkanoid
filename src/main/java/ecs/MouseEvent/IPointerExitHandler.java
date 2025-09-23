package ecs.MouseEvent;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public interface IPointerExitHandler {
    public void onPointerExited();
    default void attachPointerExited(Node node) {
        if(node instanceof ImageView imageView){
            Image image = imageView.getImage();
            imageView.setOnMouseExited(e -> {
                int x = (int) e.getX();
                int y = (int) e.getY();

                // Check transparency at clicked pixel
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    int argb = image.getPixelReader().getArgb(x, y);
                    int alpha = (argb >> 24) & 0xff;

                    if (alpha > 0) {
                        onPointerExited();
                    } else {
                        e.consume(); // ignore transparent clicks
                    }
                }
            });
        }
        else {
            node.setOnMouseExited(e -> onPointerExited());
        }
    }
}
