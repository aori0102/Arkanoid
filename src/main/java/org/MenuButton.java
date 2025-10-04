package org;

import utils.Vector2;

public class MenuButton extends MonoBehaviour {
    private SpriteAnimator spriteAnimator;
    private ButtonUI buttonUI;
    final String buttonImagePath = "/Frame 1.png";
    private double width = 418;
    private double height = 118;
    private double renderWidth =  width / 1.5;
    private double renderHeight = height / 1.5;
    final String buttonIdleKey = "ButtonIdleKey";
    final String buttonHoverKey = "ButtonHoverKey";
    final String buttonPressedKey = "ButtonPressedKey";
    final String buttonReleasedKey = "ButtonReleasedKey";

    final String buttonClickKey = "ButtonClickKey";
    private double timePerFrame = 0.01;

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
    public MenuButton(GameObject owner) {
        super(owner);
        spriteAnimator = owner.addComponent(SpriteAnimator.class);
        buttonUI = owner.addComponent(ButtonUI.class);
    }

    public void update() {
        if(buttonPrevState != buttonState) {

            spriteAnimator.playAnimation(
                    switch (buttonState){
                        case Idle->buttonIdleKey;
                        case Hover->buttonHoverKey;
                        case Pressed->buttonPressedKey;
                        case Released->buttonReleasedKey;
                        case Clicked->buttonClickKey;
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
        spriteAnimator.addAnimationClip(buttonHoverKey);
        //spriteAnimator.setLoop(buttonHoverKey, true);
        spriteAnimator.setSprite(buttonHoverKey, buttonImagePath);
        spriteAnimator.addFrame(
                buttonHoverKey,
                new Vector2(1,1),
                new Vector2(width, height),
                new Vector2(renderWidth, renderHeight),
                timePerFrame
        );

    }

    private void initIdleAnimator() {
        spriteAnimator.addAnimationClip(buttonIdleKey);
        spriteAnimator.setSprite(buttonIdleKey, buttonImagePath);
        spriteAnimator.addFrame(
                buttonIdleKey,
                new Vector2(1, 239),
                new Vector2(width, height),
                new Vector2(renderWidth, renderHeight),
                timePerFrame
        );
        spriteAnimator.setLoop(buttonIdleKey, true);

    }

    private void initPressedAnimation() {
        spriteAnimator.addAnimationClip(buttonPressedKey);
        spriteAnimator.setSprite(buttonPressedKey, buttonImagePath);
        spriteAnimator.setLoop(buttonPressedKey, true);
        spriteAnimator.addFrame(
                buttonPressedKey,
                new Vector2(1, 120),
                new Vector2(width, height),
                new Vector2(renderWidth, renderHeight),
                timePerFrame
        );
    }

    private void initReleasedAnimation() {
        spriteAnimator.addAnimationClip(buttonReleasedKey);
        spriteAnimator.setSprite(buttonReleasedKey, buttonImagePath);
        spriteAnimator.addFrame(
                buttonReleasedKey,
                new Vector2(1, 1),
                new Vector2(width, height),
                new Vector2(renderWidth, renderHeight),
                timePerFrame);
        spriteAnimator.setLoop(buttonReleasedKey, true);
    }

    private void initClickAnimation() {
        spriteAnimator.addAnimationClip(buttonClickKey);
        spriteAnimator.setSprite(buttonClickKey, buttonImagePath);
        spriteAnimator.setLoop(buttonClickKey, true);

        spriteAnimator.addFrame(
                buttonClickKey,
                new Vector2(1, 1),
                new Vector2(width, height),
                new Vector2(renderWidth, renderHeight),
                timePerFrame
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
