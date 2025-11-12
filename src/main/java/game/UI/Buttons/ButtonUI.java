package game.UI.Buttons;

import javafx.scene.input.MouseEvent;
import game.Interface.*;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;

/**
 * Core component responsible for translating low-level pointer events from the engine
 * (via the implemented interfaces) into high-level {@link EventHandler} events that
 * can be subscribed to by components like {@link BaseButton} or a controller.
 * <p>
 * This class effectively acts as the **event aggregator** for mouse interactions
 * on the {@link GameObject} it is attached to.
 */
public class ButtonUI extends MonoBehaviour
        implements IPointerDownHandler,
        IPointerClickHandler,
        IPointerUpHandler,
        IPointerEnterHandler,
        IPointerExitHandler {

    private SpriteRenderer image;
    public EventHandler<MouseEvent> onPointerClick = new EventHandler<>(ButtonUI.class);
    public EventHandler<MouseEvent> onPointerUp = new EventHandler<>(ButtonUI.class);
    public EventHandler<MouseEvent> onPointerEnter = new EventHandler<>(ButtonUI.class);
    public EventHandler<MouseEvent> onPointerExit = new EventHandler<>(ButtonUI.class);
    public EventHandler<MouseEvent> onPointerDown = new EventHandler<>(ButtonUI.class);

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

    /**
     * Implementation of {@link IPointerClickHandler}.
     * Invokes the high-level {@link #onPointerClick} event.
     *
     * @param event The mouse event data.
     */
    @Override
    public void onPointerClicked(MouseEvent event) {
        onPointerClick.invoke(this, event);
    }

    /**
     * Implementation of {@link IPointerEnterHandler}.
     * Invokes the high-level {@link #onPointerEnter} event.
     *
     * @param event The mouse event data.
     */
    @Override
    public void onPointerEntered(MouseEvent event) {
        onPointerEnter.invoke(this, event);
    }

    /**
     * Implementation of {@link IPointerUpHandler}.
     * Invokes the high-level {@link #onPointerUp} event.
     *
     * @param event The mouse event data.
     */
    @Override
    public void onPointerUp(MouseEvent event) {
        onPointerUp.invoke(this, event);
    }

    /**
     * Implementation of {@link IPointerExitHandler}.
     * Invokes the high-level {@link #onPointerExit} event.
     *
     * @param event The mouse event data.
     */
    @Override
    public void onPointerExited(MouseEvent event) {
        onPointerExit.invoke(this, event);
    }

    /**
     * Implementation of {@link IPointerDownHandler}.
     * Invokes the high-level {@link #onPointerDown} event.
     *
     * @param event The mouse event data.
     */
    @Override
    public void onPointerDown(MouseEvent event) {
        onPointerDown.invoke(this, event);
    }

}