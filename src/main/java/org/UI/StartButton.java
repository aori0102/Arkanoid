package org.UI;

import org.GameObject;
import org.MonoBehaviour;
import org.SpriteAnimator;
import utils.Vector2;

public class StartButton extends MonoBehaviour {
    private SpriteAnimator spriteAnimator;
    private ButtonUI buttonUI;
    final String buttonImagePath = "/Frame 1.png";
    private final double WIDTH = 418;
    private final double HEIGHT = 118;
    private final double RENDER_WIDTH =  WIDTH / 1.5;
    private final double RENDER_HEIGHT = HEIGHT / 1.5;
    final String BUTTON_IDLE_KEY = "ButtonIdleKey";
    final String BUTTON_HOVER_KEY = "ButtonHoverKey";
    final String BUTTON_PRESSED_KEY = "ButtonPressedKey";
    final String BUTTON_RELEASED_KEY = "ButtonReleasedKey";
    final String BUTTON_CLICKED_KEY = "ButtonClickKey";

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
    public StartButton(GameObject owner) {
        super(owner);
        spriteAnimator = owner.addComponent(SpriteAnimator.class);
        buttonUI = owner.addComponent(ButtonUI.class);
    }

    public void update() {
        if(buttonPrevState != buttonState) {
            spriteAnimator.playAnimation(
                    switch (buttonState){
                        case Idle-> BUTTON_IDLE_KEY;
                        case Hover-> BUTTON_HOVER_KEY;
                        case Pressed-> BUTTON_PRESSED_KEY;
                        case Released-> BUTTON_RELEASED_KEY;
                        case Clicked-> BUTTON_CLICKED_KEY;
                    }
            );
            buttonPrevState = buttonState;
        }
    }

    public void awake() {

        initHoverAnimator();
        initIdleAnimator();
        initReleasedAnimation();
        initPressedAnimation();
        initClickAnimation();
        stateHandler();

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }

    private void initHoverAnimator() {
        spriteAnimator.addAnimationClip(BUTTON_HOVER_KEY);
        //spriteAnimator.setLoop(buttonHoverKey, true);
        spriteAnimator.setSprite(BUTTON_HOVER_KEY, buttonImagePath);
        spriteAnimator.addFrame(
                BUTTON_HOVER_KEY,
                new Vector2(1,1),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(RENDER_WIDTH, RENDER_HEIGHT),
                TIME_PER_FRAME
        );

    }

    private void initIdleAnimator() {
        spriteAnimator.addAnimationClip(BUTTON_IDLE_KEY);
        spriteAnimator.setSprite(BUTTON_IDLE_KEY, buttonImagePath);
        spriteAnimator.addFrame(
                BUTTON_IDLE_KEY,
                new Vector2(1, 239),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(RENDER_WIDTH, RENDER_HEIGHT),
                TIME_PER_FRAME
        );
        spriteAnimator.setLoop(BUTTON_IDLE_KEY, true);

    }

    private void initPressedAnimation() {
        spriteAnimator.addAnimationClip(BUTTON_PRESSED_KEY);
        spriteAnimator.setSprite(BUTTON_PRESSED_KEY, buttonImagePath);
        spriteAnimator.setLoop(BUTTON_PRESSED_KEY, true);
        spriteAnimator.addFrame(
                BUTTON_PRESSED_KEY,
                new Vector2(1, 120),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(RENDER_WIDTH, RENDER_HEIGHT),
                TIME_PER_FRAME
        );
    }

    private void initReleasedAnimation() {
        spriteAnimator.addAnimationClip(BUTTON_RELEASED_KEY);
        spriteAnimator.setSprite(BUTTON_RELEASED_KEY, buttonImagePath);
        spriteAnimator.addFrame(
                BUTTON_RELEASED_KEY,
                new Vector2(1, 1),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(RENDER_WIDTH, RENDER_HEIGHT),
                TIME_PER_FRAME);
        spriteAnimator.setLoop(BUTTON_RELEASED_KEY, true);
    }

    private void initClickAnimation() {
        spriteAnimator.addAnimationClip(BUTTON_CLICKED_KEY);
        spriteAnimator.setSprite(BUTTON_CLICKED_KEY, buttonImagePath);
        spriteAnimator.setLoop(BUTTON_CLICKED_KEY, true);

        spriteAnimator.addFrame(
                BUTTON_CLICKED_KEY,
                new Vector2(1, 1),
                new Vector2(WIDTH, HEIGHT),
                new Vector2(RENDER_WIDTH, RENDER_HEIGHT),
                TIME_PER_FRAME
        );
    }

    private void stateHandler() {
        buttonUI.setAdditionalOnClick(() -> {
            buttonState = ButtonState.Clicked;
            System.out.println(buttonState);

        });
        buttonUI.setOnExit(() -> {
            buttonState = ButtonState.Idle;
            System.out.println(buttonState);

        });
        buttonUI.setOnUp(() -> {
            buttonState = ButtonState.Released;
            System.out.println(buttonState);

        });
        buttonUI.setOnDown(() -> {
            buttonState = ButtonState.Pressed;
            System.out.println(buttonState);

        });
        buttonUI.setOnEnter(() -> {
            buttonState = ButtonState.Hover;
            System.out.println(buttonState);
        });
    }

}
