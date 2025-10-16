package org.UI;

import javafx.scene.input.MouseEvent;
import org.AnimationClipData;
import org.GameObject;
import org.MonoBehaviour;
import org.SpriteAnimator;

public abstract class BaseButton extends MonoBehaviour {

    protected SpriteAnimator spriteAnimator;
    protected ButtonUI buttonUI;

    protected AnimationClipData idleKey;
    protected AnimationClipData hoverKey;
    protected AnimationClipData pressedKey;
    protected AnimationClipData releasedKey;
    protected AnimationClipData clickedKey;

    protected enum ButtonState { Idle, Hover, Pressed, Released, Clicked }
    protected ButtonState buttonState = ButtonState.Idle;
    private ButtonState prevState = null;

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
        setupStateHandlers();

        buttonUI.onPointerClick.addListener(this::baseButton_onPointerClicked);

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
            );
            prevState = buttonState;
        }
    }

    private void setupStateHandlers() {
        buttonUI.onPointerClick.addListener((s, e) -> {
            buttonState = ButtonState.Clicked;
            System.out.println("[ButtonUI] → Clicked event triggered | State: " + buttonState);
        });

        buttonUI.onPointerExit.addListener((s, e) -> {
            buttonState = ButtonState.Idle;
            System.out.println("[ButtonUI] → PointerExit event triggered | State: " + buttonState);
        });

        buttonUI.onPointerUp.addListener((s, e) -> {
            buttonState = ButtonState.Released;
            System.out.println("[ButtonUI] → PointerUp event triggered | State: " + buttonState);
        });

        buttonUI.onPointerDown.addListener((s, e) -> {
            buttonState = ButtonState.Pressed;
            System.out.println("[ButtonUI] → PointerDown event triggered | State: " + buttonState);
        });

        buttonUI.onPointerEnter.addListener((s, e) -> {
            buttonState = ButtonState.Hover;
            System.out.println("[ButtonUI] → PointerEnter event triggered | State: " + buttonState);
        });

        System.out.println("[ButtonUI] State handlers initialized successfully for " + gameObject.getName());
    }

    protected abstract void baseButton_onPointerClicked(Object sender, MouseEvent e);

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() { }
}

