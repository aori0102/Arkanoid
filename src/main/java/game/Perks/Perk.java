package game.Perks;

import game.Interface.IPointerClickHandler;
import game.Interface.IPointerEnterHandler;
import game.Interface.IPointerExitHandler;
import javafx.scene.input.MouseEvent;
import org.*;

public abstract class Perk extends MonoBehaviour
        implements IPointerClickHandler,
        IPointerEnterHandler,
        IPointerExitHandler {
    public EventHandler<MouseEvent> onPointerClicked = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerEntered = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerExited = new EventHandler<>(this);

    protected TextUI textUI;
    protected SpriteAnimator spriteAnimator;

    protected AnimationClipData perkKey;

    GameObject childGameObject = null;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Perk(GameObject owner) {
        super(owner);
        textUI = owner.addComponent(TextUI.class);
        textUI.setFont(FontDataIndex.Jersey_25);
        spriteAnimator = owner.addComponent(SpriteAnimator.class);

        childGameObject = GameObjectManager.instantiate("Text");
        childGameObject.setParent(childGameObject);
        //attach pointer
        attachPointerClick(getTransform());
        attachPointerEnter(getTransform());
        attachPointerExited(getTransform());
    }

    @Override
    public void awake() {
        setUpVisual();
        spriteAnimator.addAnimationClip(perkKey);
    }

    @Override
    public void start() {
        spriteAnimator.playAnimation(perkKey);
    }

    protected abstract void setUpVisual();
    protected abstract void applyEffect();

    @Override
    public void onPointerClicked(MouseEvent event) {
        applyEffect();
    }

    @Override
    public void onPointerEntered(MouseEvent event) {

    }

    @Override
    public void onPointerExited(MouseEvent event) {

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }
}
