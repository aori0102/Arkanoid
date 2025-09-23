package ecs.UI;
import ecs.MouseEvent.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.event.ActionListener;

public class ButtonUI extends UI
        implements IPointerDownHandler, IPointerClickHandler, IPointerUpHandler,
        IPointerEnterHandler, IPointerExitHandler {
    private Image image;
    private final ImageView imageView = new ImageView();
    private Runnable function;

    public ButtonUI(ButtonUI button){
        this.image = button.image;
        imageView.setImage(image);
        function = null;
        setHeight(image.getHeight());
        setWidth(image.getWidth());

        imageView.setOnMouseClicked(e -> {
            int x = (int) e.getX();
            int y = (int) e.getY();

            // Check transparency at clicked pixel
            if (x >= 0 && x < image.getWidth() && y >= 0 && y < image.getHeight()) {
                int argb = image.getPixelReader().getArgb(x, y);
                int alpha = (argb >> 24) & 0xff;

                if (alpha > 0 && function != null) {
                    function.run();
                } else {
                    e.consume(); // ignore transparent clicks
                }
            }
        });
    }

    public ButtonUI(Image image){
        this.image = image;
        imageView.setImage(image);
        function = null;
    }

    public void setImage(Image image){
        this.image = image;
        imageView.setImage(image);
    }

    public Image getImage(){
        return image;
    }

    public void setOnClick(Runnable action){
        function = action;
    }
}
