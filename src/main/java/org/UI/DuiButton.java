package org.UI;

import org.AnimationClipData;
import org.GameObject;
import org.MonoBehaviour;
import org.SpriteAnimator;
import utils.Vector2;

public class DuiButton extends MonoBehaviour {

    private SpriteAnimator spriteAnimator;
    private ButtonUI buttonUI;
    final String buttonImagePath = "/Frame 1.png";
    private final double WIDTH = 418;
    private final double HEIGHT = 118;
    private final double RENDER_WIDTH = WIDTH / 1.5;
    private final double RENDER_HEIGHT = HEIGHT / 1.5;
    final AnimationClipData BUTTON_IDLE_KEY = AnimationClipData.Dui_Button_Idle;
    final AnimationClipData BUTTON_HOVER_KEY = AnimationClipData.Dui_Button_Hovered;
    final AnimationClipData BUTTON_PRESSED_KEY = AnimationClipData.Dui_Button_Pressed;
    final AnimationClipData BUTTON_RELEASED_KEY = AnimationClipData.Dui_Button_Released;
    final AnimationClipData BUTTON_CLICKED_KEY = AnimationClipData.Dui_Button_Clicked;

    private final double TIME_PER_FRAME = 0.01;

    enum ButtonState {
        Idle,
        Hover,
        Pressed,
        Released,
        Clicked
    }

    private ButtonState buttonState = ButtonState.Idle;
    private ButtonState buttonPrevState = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public DuiButton(GameObject owner) {
        super(owner);
        spriteAnimator = owner.addComponent(SpriteAnimator.class);
        buttonUI = owner.addComponent(ButtonUI.class);
    }

    public void update() {
        if (buttonPrevState != buttonState) {
            spriteAnimator.playAnimation(
                    switch (buttonState) {
                        case Idle -> BUTTON_IDLE_KEY;
                        case Hover -> BUTTON_HOVER_KEY;
                        case Pressed -> BUTTON_PRESSED_KEY;
                        case Released -> BUTTON_RELEASED_KEY;
                        case Clicked -> BUTTON_CLICKED_KEY;
                    }
            );
            buttonPrevState = buttonState;
        }
    }

    public void awake() {

        spriteAnimator.addAnimationClip(BUTTON_IDLE_KEY);
        spriteAnimator.addAnimationClip(BUTTON_HOVER_KEY);
        spriteAnimator.addAnimationClip(BUTTON_PRESSED_KEY);
        spriteAnimator.addAnimationClip(BUTTON_RELEASED_KEY);
        spriteAnimator.addAnimationClip(BUTTON_CLICKED_KEY);
        stateHandler();

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }

    private void stateHandler() {
        buttonUI.onPointerClick.addListener((sender, e) -> {
            buttonState = ButtonState.Clicked;
            System.out.println(buttonState);

        });
        buttonUI.onPointerExit.addListener((sender, e) -> {
            buttonState = ButtonState.Idle;
            System.out.println(buttonState);

        });
        buttonUI.onPointerUp.addListener((sender, e) -> {
            buttonState = ButtonState.Released;
            System.out.println(buttonState);

        });
        buttonUI.onPointerDown.addListener((sender, e) -> {
            buttonState = ButtonState.Pressed;
            System.out.println(buttonState);

        });
        buttonUI.onPointerEnter.addListener((sender, e) -> {
            buttonState = ButtonState.Hover;
            System.out.println(buttonState);
        });
    }

}
