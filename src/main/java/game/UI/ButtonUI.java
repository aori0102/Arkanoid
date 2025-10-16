package game.UI;

import javafx.scene.input.MouseEvent;
import game.Interface.*;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;


public class ButtonUI extends MonoBehaviour
        implements IPointerDownHandler,
        IPointerClickHandler,
        IPointerUpHandler,
        IPointerEnterHandler,
        IPointerExitHandler {

    private SpriteRenderer image;
    public EventHandler<MouseEvent> onPointerClick = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerUp = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerEnter = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerExit = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerDown = new EventHandler<>(this);

    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public ButtonUI(GameObject owner) {

        super(owner);

        image = owner.addComponent(SpriteRenderer.class);

        //Attach mouse event
        attachPointerClick(getTransform());
        attachPointerEnter(getTransform());
        attachPointerUp(getTransform());
        attachPointerDown(getTransform());
        attachPointerExited(getTransform());

    }

    @Override
    protected void destroyComponent() {
        image = null;
        onPointerClick = null;
        onPointerEnter = null;
        onPointerExit = null;
        onPointerDown = null;
        onPointerUp = null;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new ButtonUI(newOwner);
    }

    @Override
    public void onPointerClicked(MouseEvent event) {
        onPointerClick.invoke(this, event);
    }

    @Override
    public void onPointerEntered(MouseEvent event) {
        onPointerEnter.invoke(this, event);
    }

    @Override
    public void onPointerUp(MouseEvent event) {
        onPointerUp.invoke(this, event);
    }

    @Override
    public void onPointerExited(MouseEvent event) {
        onPointerExit.invoke(this, event);
    }

    @Override
    public void onPointerDown(MouseEvent event) {
        onPointerDown.invoke(this, event);
    }

}