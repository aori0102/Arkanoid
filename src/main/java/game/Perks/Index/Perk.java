package game.Perks.Index;

import game.Interface.IPointerClickHandler;
import game.Interface.IPointerEnterHandler;
import game.Interface.IPointerExitHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import org.Animation.AnimationClipData;
import org.Animation.SpriteAnimator;
import org.Audio.AudioManager;
import org.Audio.SFXAsset;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

public abstract class Perk extends MonoBehaviour
        implements IPointerClickHandler,
        IPointerEnterHandler,
        IPointerExitHandler {

    private static final double TEXT_SIZE = 20.0;
    private static final double ANIMATION_DURATION = 0.1;
    private static final double TEXT_DISPLAY_WIDTH = 160.0;
    private static final double ORIGINAL_SCALE = 1.0;
    private static final double TARGET_SCALE = 1.2;
    private static final double FLUCTUATION_RATE = 1.2;
    private static final Vector2 TEXT_OFFSET = new Vector2(0.0, -16.0);

    public EventHandler<MouseEvent> onPointerClicked = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerEntered = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerExited = new EventHandler<>(Perk.class);

    protected TextUI textUI;
    protected SpriteAnimator spriteAnimator;

    protected AnimationClipData perkKey;
    private Vector2 oldPosition;

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
        textUI.setWrapWidth(TEXT_DISPLAY_WIDTH);
        textUI.setVerticalAlignment(TextVerticalAlignment.Top);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFontSize(TEXT_SIZE);
        textUI.getText().setFill(Color.YELLOW);
        textUI.getTransform().setLocalPosition(TEXT_OFFSET);
        //attach pointer
        attachPointerClick(getTransform());
        attachPointerEnter(getTransform());
        attachPointerExited(getTransform());


    }

    @Override
    public void awake() {
        setUpVisual();
        spriteAnimator.addAnimationClip(perkKey);

        onPointerEntered.addListener(this::perk_onPointerEntered);
        onPointerExited.addListener(this::perk_onPointerExited);
        onPointerClicked.addListener(this::perk_onPointerClicked);

    }

    @Override
    public void start() {
        oldPosition = getTransform().getGlobalPosition();

        spriteAnimator.playAnimation(perkKey, null);
        idleAnimation();
    }

    @Override
    public void update() {
        idleAnimation();
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

    protected abstract void setUpVisual();

    protected void perk_onPointerClicked(Object sender, MouseEvent e) {
        System.out.println("perk_onPointerClicked");
        AudioManager.playSFX(SFXAsset.SFXIndex.OnPerkReceived);
    }

    protected void perk_onPointerEntered(Object sender, MouseEvent e) {
        hoverAnimation();
        buttonState = ButtonState.Hover;
        System.out.println("[Perk] Hover");

    }

    protected void perk_onPointerExited(Object sender, MouseEvent e) {
        if(!getGameObject().isDestroyed()){
            exitAnimation();
            buttonState = ButtonState.Idle;
            System.out.println("[Perk] Idle");
        }

    }

    public TextUI getTextUI() {
        return textUI;
    }

    public void destroyText() {
        textUI.setText("");
    }

    private void idleAnimation() {
        if (oldPosition == null) {
            oldPosition = getTransform().getGlobalPosition();
        }
        double time = Time.getRealTime();
        double phase = (getGameObject().hashCode() % 1000) / 1000.0 * Math.PI * 2; // unique offset
        double swing = Math.sin(FLUCTUATION_RATE * time * Math.PI + phase) * 5.0; // ±5 px
        getTransform().setGlobalPosition(oldPosition.add(new Vector2(0, swing)));
    }

    private void hoverAnimation() {
        Tween.to(getGameObject())
                .scaleTo(TARGET_SCALE, ANIMATION_DURATION)
                .ease(Ease.IN_OUT)
                .play();
    }

    private void exitAnimation() {
        Tween.to(getGameObject())
                .scaleTo(ORIGINAL_SCALE, ANIMATION_DURATION)
                .ease(Ease.IN_OUT)
                .play();
    }

    private void startAnimation() {
        Tween.to(getGameObject())
                .moveY(400, 0.3)
                .ease(Ease.IN_OUT)
                .play();
    }

}
