package game.Perks.Index;

import com.sun.javafx.util.Utils;
import game.Interface.IPointerClickHandler;
import game.Interface.IPointerEnterHandler;
import game.Interface.IPointerExitHandler;
import game.UI.Buttons.BaseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Rendering.SpriteRenderer;
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

    public EventHandler<MouseEvent> onPointerClicked = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerEntered = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerExited = new EventHandler<>(Perk.class);
    private static final double MAX_PERK_FLUCTUATION_DISTANCE = 3.2;
    private static final double PERK_FLUCTUATION_RATE = 0.49;
    private static final double HOVER_OFFSET = 100;
    private static final double FLUCTUATION_RANDOM_MAX = 3.6;
    protected double randomTime = Random.range(0, FLUCTUATION_RANDOM_MAX);

    protected TextUI textUI;
    protected SpriteAnimator spriteAnimator;

    protected AnimationClipData perkKey;
    private static final double TEXT_SIZE = 20.0;

    private Vector2 targetPosition;
    private final Vector2 TARGET_OFFSET = new Vector2(0, 50);
    private Vector2 ORIGIN_POSITION;
    private final double HOVER_SPEED = 8;

    //ButtonState
    protected enum ButtonState {Idle, Hover, Pressed, Released, Clicked}

    protected ButtonState buttonState = ButtonState.Idle;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Perk(GameObject owner) {
        super(owner);

        spriteAnimator = owner.addComponent(SpriteAnimator.class);

        textUI = GameObjectManager.instantiate("Text")
                .addComponent(TextUI.class);
        textUI.getGameObject().setParent(owner);
        textUI.setFont(FontDataIndex.Jersey_25);
        //attach pointer
        attachPointerClick(getTransform());
        attachPointerEnter(getTransform());
        attachPointerExited(getTransform());

        attachPointerClick(textUI.getTransform());
        attachPointerEnter(textUI.getTransform());
        attachPointerEnter(textUI.getTransform());

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
        spriteAnimator.playAnimation(perkKey, null);
        ORIGIN_POSITION = getTransform().getGlobalPosition();
        targetPosition = new Vector2(ORIGIN_POSITION);

    }

    @Override
    public void update() {
        hoverAnimation();
    }

    protected abstract void setUpVisual();

    protected void perk_onPointerClicked(Object sender, MouseEvent e){

    }

    protected void perk_onPointerEntered(Object sender, MouseEvent e) {
        targetPosition = new Vector2(ORIGIN_POSITION.x - TARGET_OFFSET.x
                , ORIGIN_POSITION.y - TARGET_OFFSET.y) ;
        buttonState = ButtonState.Hover;
        System.out.println("[Perk] Hover");

    }

    protected void perk_onPointerExited(Object sender, MouseEvent e) {
        targetPosition = new Vector2(ORIGIN_POSITION) ;
        buttonState = ButtonState.Idle;
        System.out.println("[Perk] Idle");
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

    private void setTextVisual() {
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFontSize(TEXT_SIZE);
        textUI.getText().setFill(Color.YELLOW);
    }


    private void hoverAnimation() {
        Vector2 currentPos = getTransform().getGlobalPosition();
        double newX = currentPos.x + (targetPosition.x - currentPos.x) * HOVER_SPEED * Time.getDeltaTime();
        double newY = currentPos.y + (targetPosition.y - currentPos.y) * HOVER_SPEED * Time.getDeltaTime();
        getTransform().setGlobalPosition(new Vector2(newX, newY));
    }

}
