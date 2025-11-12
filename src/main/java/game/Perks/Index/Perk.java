package game.Perks.Index;

import game.Interface.IPointerClickHandler;
import game.Interface.IPointerEnterHandler;
import game.Interface.IPointerExitHandler;
import game.Player.Paddle.PaddleStat;
import game.Player.Player;
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
import utils.Random;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

/**
 * Base class for all player perks (temporary power-ups/modifiers).
 * <p>
 * This class handles the core UI behavior, including animation (idle float, hover scale),
 * pointer interaction events, random generation of a modifier value, and the display
 * of the perk's description.
 * </p>
 */
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

    private double modifierValue = 0.0;

    public EventHandler<MouseEvent> onPointerClicked = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerEntered = new EventHandler<>(Perk.class);
    public EventHandler<MouseEvent> onPointerExited = new EventHandler<>(Perk.class);

    protected TextUI textUI;
    protected SpriteAnimator spriteAnimator;

    private Vector2 oldPosition;

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
        modifierValue = Random.range(getMinModifierValue(), getMaxModifierValue());
        textUI.setText(getPerkDescription(modifierValue));
        spriteAnimator.addAnimationClip(getPerkAnimationKey());

        onPointerEntered.addListener(this::perk_onPointerEntered);
        onPointerExited.addListener(this::perk_onPointerExited);
        onPointerClicked.addListener(this::perk_onPointerClicked);

    }

    @Override
    public void start() {
        oldPosition = getTransform().getGlobalPosition();

        spriteAnimator.playAnimation(getPerkAnimationKey(), null);
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

    /**
     * Internal handler for the pointer clicked event.
     * Applies the perk's effect to the player's paddle stats and plays a sound effect.
     *
     * @param sender The object that invoked the event.
     * @param e The mouse event data (currently ignored).
     */
    private void perk_onPointerClicked(Object sender, MouseEvent e) {
        applyPerk(Player.getInstance().getPlayerPaddle().getPaddleStat());
        AudioManager.playSFX(SFXAsset.SFXIndex.OnPerkReceived);
    }

    /**
     * Gets the randomly generated modifier value for this perk instance.
     *
     * @return The modifier value (e.g., speed increase, size change percentage).
     */
    public double getModifierValue() {
        return modifierValue;
    }

    /**
     * Internal handler for the pointer entered event.
     * Triggers the hover scale-up animation and updates the button state.
     *
     * @param sender The object that invoked the event.
     * @param e The mouse event data.
     */
    private void perk_onPointerEntered(Object sender, MouseEvent e) {
        hoverAnimation();
        buttonState = ButtonState.Hover;
        System.out.println("[Perk] Hover");

    }

    /**
     * Internal handler for the pointer exited event.
     * Triggers the exit (scale-down) animation and updates the button state.
     *
     * @param sender The object that invoked the event.
     * @param e The mouse event data.
     */
    private void perk_onPointerExited(Object sender, MouseEvent e) {
        if (!getGameObject().isDestroyed()) {
            exitAnimation();
            buttonState = ButtonState.Idle;
            System.out.println("[Perk] Idle");
        }

    }

    /**
     * Retrieves the {@link TextUI} component used to display the perk's description.
     *
     * @return The text UI component.
     */
    public TextUI getTextUI() {
        return textUI;
    }

    /**
     * Clears the text content of the description UI.
     */
    public void destroyText() {
        textUI.setText("");
    }

    /**
     * Executes the continuous subtle vertical floating animation (idle animation).
     * This uses a sine wave function based on real time to create a smooth,
     * low-amplitude vertical fluctuation (±5 pixels).
     */
    private void idleAnimation() {
        if (oldPosition == null) {
            oldPosition = getTransform().getGlobalPosition();
        }
        double time = Time.getRealTime();
        double phase = (getGameObject().hashCode() % 1000) / 1000.0 * Math.PI * 2; // unique offset
        double swing = Math.sin(FLUCTUATION_RATE * time * Math.PI + phase) * 5.0; // ±5 px
        getTransform().setGlobalPosition(oldPosition.add(new Vector2(0, swing)));
    }

    /**
     * Tween animation to scale the perk up when the pointer hovers over it.
     */
    private void hoverAnimation() {
        Tween.to(getGameObject())
                .scaleTo(TARGET_SCALE, ANIMATION_DURATION)
                .ease(Ease.IN_OUT)
                .play();
    }

    /**
     * Tween animation to scale the perk back down when the pointer exits.
     */
    private void exitAnimation() {
        Tween.to(getGameObject())
                .scaleTo(ORIGINAL_SCALE, ANIMATION_DURATION)
                .ease(Ease.IN_OUT)
                .play();
    }

    /**
     * Defines the description string for the perk, incorporating the modifier amount.
     *
     * @param amount The random modifier value generated for this instance.
     * @return The formatted description text.
     */
    protected abstract String getPerkDescription(double amount);

    /**
     * Defines the minimum value for the random modifier generation.
     *
     * @return The minimum modifier value.
     */
    protected abstract double getMinModifierValue();

    /**
     * Defines the maximum value for the random modifier generation.
     *
     * @return The maximum modifier value.
     */
    protected abstract double getMaxModifierValue();

    /**
     * Defines the animation clip data required for the {@link SpriteAnimator}.
     *
     * @return The {@link AnimationClipData} for the perk's sprite.
     */
    protected abstract AnimationClipData getPerkAnimationKey();

    /**
     * Applies the specific effect of the perk to the player's paddle statistics.
     *
     * @param paddleStat The {@link PaddleStat} object of the player's paddle.
     */
    protected abstract void applyPerk(PaddleStat paddleStat);

}
