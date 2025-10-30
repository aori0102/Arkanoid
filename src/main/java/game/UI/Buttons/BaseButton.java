package game.UI.Buttons;

import javafx.scene.input.MouseEvent;
import org.Animation.AnimationClipData;
import org.Event.EventHandler;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Animation.SpriteAnimator;
import utils.MathUtils;
import utils.Vector2;

public abstract class BaseButton extends MonoBehaviour {
    //JavaFX nodes
    protected SpriteAnimator spriteAnimator;

    //Components
    protected ButtonUI buttonUI;

    //AnimationClip
    protected AnimationClipData idleKey;
    protected AnimationClipData hoverKey;
    protected AnimationClipData pressedKey;
    protected AnimationClipData releasedKey;
    protected AnimationClipData clickedKey;

    //ButtonState
    protected enum ButtonState {Idle, Hover, Pressed, Released, Clicked}

    protected ButtonState buttonState = ButtonState.Idle;
    private ButtonState prevState = null;

    //For scaling animations
    private double scale = 1.0;
    private double targetScale = 1.0;
    private final double SCALE_SPEED = 8.0; // higher = faster pop
    private final double CLICKED_SCALE = 1.15;
    private final double EXIT_SCALE = 1.0;
    private final double ENTER_SCALE = 1.05;
    private final double RELEASE_SCALE = 1.0;
    private final double PRESS_SCALE = 1.15;

    //Events
    public EventHandler<MouseEvent> onAnyMenuButtonClicked = new EventHandler<>(BaseButton.class);

    public BaseButton(GameObject owner) {
        super(owner);
        spriteAnimator = owner.addComponent(SpriteAnimator.class);
        buttonUI = owner.addComponent(ButtonUI.class);
    }

    // Each subclass defines its own visuals and animation keys
    protected abstract void setupButtonAppearance();

    @Override
    public void awake() {
        setupButtonAppearance();
        spriteAnimator.addAnimationClip(idleKey);
        spriteAnimator.addAnimationClip(hoverKey);
        spriteAnimator.addAnimationClip(pressedKey);
        spriteAnimator.addAnimationClip(releasedKey);
        spriteAnimator.addAnimationClip(clickedKey);
        setupEventHandler();


    }

    @Override
    public void update() {
        if (prevState != buttonState) {
            spriteAnimator.playAnimation(
                    switch (buttonState) {
                        case Idle -> idleKey;
                        case Hover -> hoverKey;
                        case Pressed -> pressedKey;
                        case Released -> releasedKey;
                        case Clicked -> clickedKey;
                    }
                    , null);
            prevState = buttonState;
        }

        scaleAnimation();
    }

    private void setupEventHandler() {
        buttonUI.onPointerClick.addListener((s, e) -> {
            onAnyMenuButtonClicked.invoke(this, e);
            buttonState = ButtonState.Clicked;
            targetScale = CLICKED_SCALE;
            System.out.println("[ButtonUI] → Clicked event triggered | State: " + buttonState);
        });

        buttonUI.onPointerExit.addListener((s, e) -> {
            buttonState = ButtonState.Idle;
            targetScale = EXIT_SCALE;
            System.out.println("[ButtonUI] → PointerExit event triggered | State: " + buttonState);
        });

        buttonUI.onPointerUp.addListener((s, e) -> {
            buttonState = ButtonState.Released;
            targetScale = RELEASE_SCALE;
            System.out.println("[ButtonUI] → PointerUp event triggered | State: " + buttonState);
        });

        buttonUI.onPointerDown.addListener((s, e) -> {
            buttonState = ButtonState.Pressed;
            targetScale = PRESS_SCALE;
            System.out.println("[ButtonUI] → PointerDown event triggered | State: " + buttonState);
        });

        buttonUI.onPointerEnter.addListener((s, e) -> {
            buttonState = ButtonState.Hover;
            targetScale = ENTER_SCALE;
            System.out.println("[ButtonUI] → PointerEnter event triggered | State: " + buttonState);
        });

        System.out.println("[ButtonUI] State handlers initialized successfully for " + gameObject.getName());
    }

    public ButtonUI getButtonUI() {
        return buttonUI;
    }

    private void scaleAnimation(){
        scale += (targetScale - scale) * SCALE_SPEED * utils.Time.deltaTime;
        getTransform().setGlobalScale(new Vector2(scale, scale));
    }


}


