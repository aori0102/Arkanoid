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
import utils.Random;
import utils.Time;
import utils.Vector2;

public abstract class Perk extends MonoBehaviour
        implements IPointerClickHandler,
        IPointerEnterHandler,
        IPointerExitHandler {
    public EventHandler<MouseEvent> onPointerClicked = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerEntered = new EventHandler<>(this);
    public EventHandler<MouseEvent> onPointerExited = new EventHandler<>(this);
    private static final double MAX_PERK_FLUCTUATION_DISTANCE = 3.2;
    private static final double PERK_FLUCTUATION_RATE = 0.49;
    private static final double hoverOffset = 100;
    public double randomTime = 0;
    public Vector2 oldPos;


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

        onPointerEntered.addListener(this::perk_onPointerEntered);
        onPointerExited.addListener(this::perk_onPointerExited);
        onPointerClicked.addListener(this::perk_onPointerClicked);


    }

    @Override
    public void start() {
        spriteAnimator.playAnimation(perkKey);

        oldPos = getTransform().getGlobalPosition();
    }

    @Override
    public void update() {
        idleAnimation();
    }

    protected abstract void setUpVisual();

    protected abstract void perk_onPointerClicked(Object sender, MouseEvent e);

    protected void perk_onPointerEntered(Object sender, MouseEvent e) {
//        gameObject.getTransform().translate(new Vector2(0, -hoverOffset));
        System.out.println("onPointerEntered");

    }

    protected void perk_onPointerExited(Object sender, MouseEvent e) {
        gameObject.getTransform().translate(new Vector2(0, hoverOffset));
    }

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
    protected void destroyComponent() {

    }

    private void setTextVisual() {
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFontSize(20);
        textUI.getText().setFill(Color.YELLOW);
        Vector2 pos = new Vector2(WIDTH / 2, 150);
        textUI.getTransform().setLocalPosition(pos);

    }

    private void idleAnimation() {
        var delta = Math.sin(PERK_FLUCTUATION_RATE * Time.time * Math.PI + randomTime) * MAX_PERK_FLUCTUATION_DISTANCE;
        var deltaVector = new Vector2(getTransform().getGlobalPosition().x,oldPos.y + delta);
        gameObject.getTransform().setGlobalPosition(deltaVector);
    }
}
