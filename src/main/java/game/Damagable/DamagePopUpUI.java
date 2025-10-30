package game.Damagable;

import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.GameObject.MonoBehaviour;
import org.Layer.RenderLayer;
import org.Text.FontDataIndex;
import org.Text.TextUI;
import utils.Time;
import utils.Vector2;

public final class DamagePopUpUI extends MonoBehaviour {

    private static final double LIFE_TIME = 0.8;
    private static final double POP_UP_SPEED = 349.312;
    private static final double POP_UP_DECELERATION = 398.032;
    private static final double TEXT_SIZE = 34.0;

    private TextUI damageText = null;
    private Vector2 direction = Vector2.zero();
    private double speed = POP_UP_SPEED;
    private double spawnTime = 0.0;

    /**
     * Create this MonoBehaviour.d
     *
     * @param owner The owner of this component.
     */
    public DamagePopUpUI(GameObject owner) {
        super(owner);
        damageText = addComponent(TextUI.class);
    }

    @Override
    public void awake() {
        Time.addCoroutine(() -> GameObjectManager.destroy(gameObject), Time.getTime() + LIFE_TIME);
        spawnTime = Time.getTime();
        damageText.setRenderLayer(RenderLayer.UI);
        damageText.setFontSize(TEXT_SIZE);
        damageText.setFont(FontDataIndex.Jersey_25);
    }

    @Override
    public void update() {
        handleMovement();
        handleOpacity();
    }

    private void handleMovement() {
        getTransform().translate(direction.normalize().multiply(speed * Time.getDeltaTime()));
        speed -= POP_UP_DECELERATION * Time.getDeltaTime();
    }

    private void handleOpacity() {
        var timeInterval = (Time.getTime() - spawnTime) / LIFE_TIME;
        // Random function tried on Desmos
        var opacity = Math.sin(3.0 * timeInterval) * (-Math.log(timeInterval / 1.1));
        damageText.setOpacity(opacity);
    }

    public void setDamage(int amount) {
        damageText.setText(String.valueOf(amount));
    }

    public void setDamageType(DamageType type) {
        damageText.setTextColor(type.damageColor);
    }

    public void setDirection(Vector2 direction) {
        this.direction = direction;
    }

}