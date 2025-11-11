package game.GameOver;

import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Text.TextUI;
import utils.MathUtils;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

public final class GameOverInfoDisplayUI extends MonoBehaviour {

    private static final double FLY_IN_DISTANCE = 1200.0;
    private static final double MIDDLE_LINE_OFFSET = 32.0;
    private static final double FLY_IN_DURATION = 0.7;
    private static final String AMOUNT_DEFAULT_TEXT = "0";
    private static final Vector2 AMOUNT_POP_UP_SIZE = Vector2.one().multiply(1.4);
    private static final Vector2 BACKING_AMOUNT_POP_UP_SIZE = Vector2.one().multiply(3.1);
    private static final double BACKING_AMOUNT_OPACITY = 0.37;
    private static final double AMOUNT_SHRINK_RATE = 4.669;

    @LinkViaPrefab
    private TextUI labelText = null;

    @LinkViaPrefab
    private TextUI amountText = null;

    @LinkViaPrefab
    private TextUI backingText = null;

    private String amount = AMOUNT_DEFAULT_TEXT;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public GameOverInfoDisplayUI(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        resetPosition();
        hideInfo();
    }

    @Override
    public void update() {
        amountText.getTransform().setLocalScale(Vector2.lerp(
                amountText.getTransform().getLocalScale(),
                Vector2.one(),
                Time.getDeltaTime() * AMOUNT_SHRINK_RATE
        ));
        backingText.getTransform().setLocalScale(Vector2.lerp(
                backingText.getTransform().getLocalScale(),
                Vector2.one(),
                Time.getDeltaTime() * AMOUNT_SHRINK_RATE
        ));
        backingText.setOpacity(MathUtils.lerp(
                backingText.getOpacity(),
                0.0,
                Time.getDeltaTime() * AMOUNT_SHRINK_RATE)
        );
    }

    public void flyInInfo() {

        showInfo();
        resetPosition();
        amountText.setText(AMOUNT_DEFAULT_TEXT);

        Tween.to(labelText.getGameObject())
                .moveX(-FLY_IN_DISTANCE, FLY_IN_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
        Tween.to(amountText.getGameObject())
                .moveX(-FLY_IN_DISTANCE, FLY_IN_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
        Tween.to(backingText.getGameObject())
                .moveX(-FLY_IN_DISTANCE, FLY_IN_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }

    public void revealAmount() {
        amountText.setText(amount);
        amountText.getTransform().setLocalScale(AMOUNT_POP_UP_SIZE);

        backingText.setText(amount);
        backingText.setOpacity(BACKING_AMOUNT_OPACITY);
        backingText.getTransform().setLocalScale(BACKING_AMOUNT_POP_UP_SIZE);
    }

    private void showInfo() {
        labelText.getGameObject().setActive(true);
        amountText.getGameObject().setActive(true);
        backingText.getGameObject().setActive(true);
    }

    private void hideInfo() {
        backingText.getGameObject().setActive(false);
        labelText.getGameObject().setActive(false);
        amountText.getGameObject().setActive(false);
    }

    private void resetPosition() {
        labelText.getTransform().setGlobalPosition(new Vector2(
                Main.STAGE_WIDTH / 2.0 - MIDDLE_LINE_OFFSET + FLY_IN_DISTANCE,
                labelText.getTransform().getGlobalPosition().y
        ));
        amountText.getTransform().setGlobalPosition(new Vector2(
                Main.STAGE_WIDTH / 2.0 + MIDDLE_LINE_OFFSET + FLY_IN_DISTANCE,
                amountText.getTransform().getGlobalPosition().y
        ));
        backingText.getTransform().setGlobalPosition(new Vector2(
                Main.STAGE_WIDTH / 2.0 + MIDDLE_LINE_OFFSET + FLY_IN_DISTANCE,
                backingText.getTransform().getGlobalPosition().y
        ));
    }

    public void setLabelText(String text) {
        this.labelText.setText(text);
    }

    public void setAmountText(String text) {
        amount = text;
    }

    /**
     * Link the amount text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverInfoDisplayUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param amountText The amount text.
     */
    public void linkAmountText(TextUI amountText) {
        this.amountText = amountText;
    }

    /**
     * Link the label text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverInfoDisplayUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param labelText The label text.
     */
    public void linkLabelText(TextUI labelText) {
        this.labelText = labelText;
    }

    /**
     * Link backing amount text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link GameOverInfoDisplayUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param backingText The backing amount text.
     */
    public void linkBackingText(TextUI backingText) {
        this.backingText = backingText;
    }

}
