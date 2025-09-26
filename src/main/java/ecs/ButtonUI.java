package ecs;

import ecs.MouseEvent.*;
import ecs.UI.UI;
import javafx.application.Platform;
import javafx.geometry.Bounds;
import javafx.scene.text.Text;
import utils.Vector2;


public class ButtonUI extends UI
        implements IPointerDownHandler,
        IPointerClickHandler,
        IPointerUpHandler,
        IPointerEnterHandler,
        IPointerExitHandler {

    private SpriteRenderer image;
    private Runnable buttonClickFunction;
    private Runnable buttonEnterFunction;
    private Runnable buttonUpFunction;
    private Runnable buttonExitFunction;
    private Runnable buttonDownFunction;
    private TextUI textUI;
    private boolean isCentered = false;

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public ButtonUI(GameObject owner) {

        super(owner);


        image = owner.addComponent(SpriteRenderer.class);
        textUI = owner.addComponent(TextUI.class);


        //Attach mouse event
        buttonClickFunction = null;
        attachPointerClick(image.getView());
        attachPointerEnter(image.getView());
        attachPointerUp(image.getView());
        attachPointerDown(image.getView());
        attachPointerExited(image.getView());

    }
    @Override
    public void update() {
        if(!isCentered) {
            setTextUIPositionCenter();
            isCentered = true;
        }
    }
    public TextUI getTextUI() {
        return textUI;
    }

    @Override
    public double getHeight() {
        return image.getView().getBoundsInParent().getHeight();
    }

    @Override
    public double getWidth() {
        return image.getView().getBoundsInParent().getWidth();
    }

    public void setTextUIPositionCenter() {
        double boxW = getWidth();
        double boxH = getHeight();

        Bounds b = textUI.getText().getBoundsInLocal();
        double textW = b.getWidth();
        double textH = b.getHeight();

        // center position of the box
        double centerX = this.transform().getPosition().x + boxW / 2;
        double centerY = this.transform().getPosition().y + boxH / 2;

        // shift text so its center = box center
        double posX = centerX - (textW / 2 + b.getMinX());
        double posY = centerY - (textH / 2 + b.getMinY());

        textUI.getText().setX(posX);
        textUI.getText().setY(posY);


    }

    /**
     * Set the image for this {@link ButtonUI}.
     * <p>
     * This also re-initialize effective area of the button to that image.
     * </p>
     *
     * @param path The path of the image.
     */
    public void setImage(String path) {
        image.setImage(path);

        attachPointerClick(image.getView());
        attachPointerEnter(image.getView());
        attachPointerUp(image.getView());
        attachPointerDown(image.getView());
        attachPointerExited(image.getView());


    }

    /**
     * Add action to the {@link #buttonClickFunction} of the button instead of overwrite it.
     *
     * @param action Action you want to add to {@link #buttonClickFunction}.
     */
    public void setAdditionalOnClick(Runnable action) {
        if (buttonClickFunction == null) {
            buttonClickFunction = action;
        } else {
            Runnable oldFunction = buttonClickFunction;
            buttonClickFunction = () -> {
                oldFunction.run();
                action.run();
            };
        }
    }

    /**
     * Initialize action or overwrite action of the {@link #buttonClickFunction} of the button
     *
     * @param action Action you want the {@link #buttonClickFunction} to be.
     */
    public void setOnClick(Runnable action) {
        buttonClickFunction = action;
    }

    @Override
    public void onPointerClicked() {
        if (buttonClickFunction != null) {
            buttonClickFunction.run();
        }
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new ButtonUI(newOwner);
    }

    @Override
    protected void clear() {

    }

    @Override
    public void onPointerEntered() {
        if (buttonEnterFunction != null) {
            buttonEnterFunction.run();
        }
    }

    @Override
    public void onPointerUp() {
        if (buttonUpFunction != null) {
            buttonUpFunction.run();
        }
    }

    @Override
    public void onPointerExited() {
        if (buttonExitFunction != null) {
            buttonExitFunction.run();
        }
    }

    @Override
    public void onPointerDown() {
        if (buttonDownFunction != null) {
            buttonDownFunction.run();
        }
    }
}
