package ecs.MouseEvent;

import javafx.scene.Node;
import javafx.scene.image.*;

public interface IPointerDownHandler {
    public void onPointerDown();
    default void attachPointerDown(Node node) {
        if(node instanceof ImageView imageView){
            Image image = imageView.getImage();
            imageView.setOnMousePressed(e -> {
                int x = (int) e.getX();
                int y = (int) e.getY();

                // Check transparency at clicked pixel
                if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                    int argb = image.getPixelReader().getArgb(x, y);
                    int alpha = (argb >> 24) & 0xff;

                    if (alpha > 0) {
                        onPointerDown();
                    } else {
                        e.consume(); // ignore transparent clicks
                    }
                }
            });
        }
        else {
            node.setOnMousePressed(e -> onPointerDown());
        }
    }
}
