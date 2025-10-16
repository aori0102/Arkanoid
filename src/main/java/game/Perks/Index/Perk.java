package game.Perks.Index;

import game.Interface.IPointerClickHandler;
import game.Interface.IPointerEnterHandler;
import game.Interface.IPointerExitHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Vector2;

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
    private final double WIDTH = 198;
    private final double HEIGHT = 288;
    GameObject childGameObject = null;
    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Perk(GameObject owner) {
        super(owner);
        textUI = owner.addComponent(TextUI.class);

        spriteAnimator = owner.addComponent(SpriteAnimator.class);

        childGameObject = GameObjectManager.instantiate("Text");
        childGameObject.setParent(owner);
        textUI = childGameObject.addComponent(TextUI.class);
        textUI.setFont(FontDataIndex.Jersey_25);
        //attach pointer
        attachPointerClick(getTransform());
        attachPointerEnter(getTransform());
        attachPointerExited(getTransform());
    }

    @Override
    public void awake() {
        setUpVisual();
        setTextVisual();
        spriteAnimator.addAnimationClip(perkKey);

        onPointerEntered.addListener(this::enterAnimation);
        onPointerExited.addListener(this::exitAnimation);
    }

    @Override
    public void start() {
        spriteAnimator.playAnimation(perkKey);
    }

    protected abstract void setUpVisual();
    protected abstract void perk_onPointerClicked(Object sender, MouseEvent e);

    @Override
    public void onPointerClicked(MouseEvent event) {
        onPointerClicked.invoke(this, null);
    }

    @Override
    public void onPointerEntered(MouseEvent event) {
        onPointerEntered.invoke(this, null);
    }

    @Override
    public void onPointerExited(MouseEvent event) {
        onPointerExited.invoke(this, null);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }

    protected void enterAnimation(Object sender, MouseEvent e) {
        System.out.println("enterAnimation!");
    }
    protected void exitAnimation(Object sender, MouseEvent e) {
        System.out.println("exitAnimation!");
    }

    private void setTextVisual(){
        textUI.getTransform().setLocalPosition(new Vector2(WIDTH/2, HEIGHT/2));
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFontSize(20);
        textUI.getText().setFill(Color.YELLOW);
    }
}
