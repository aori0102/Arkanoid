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
    public Vector2 oldPosition;

    protected TextUI textUI;
    protected SpriteAnimator spriteAnimator;

    protected AnimationClipData perkKey;
    private static final Vector2 PERK_SIZE = new Vector2(198, 288);
    private static final double TEXT_OFFSET = 150.0;
    private static final double TEXT_SIZE = 20.0;

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
        oldPosition = getTransform().getGlobalPosition();
        spriteAnimator.playAnimation(perkKey, null);
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
        gameObject.getTransform().translate(new Vector2(0, HOVER_OFFSET));
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
        Vector2 pos = new Vector2(PERK_SIZE.x / 2, TEXT_OFFSET);
        textUI.getTransform().setLocalPosition(pos);
    }

    private void idleAnimation() {
        var delta = Math.sin(PERK_FLUCTUATION_RATE * Time.getTime() * Math.PI + randomTime) * MAX_PERK_FLUCTUATION_DISTANCE;
        var deltaVector = new Vector2(getTransform().getGlobalPosition().x, oldPosition.y + delta);
        gameObject.getTransform().setGlobalPosition(deltaVector);
    }
}
