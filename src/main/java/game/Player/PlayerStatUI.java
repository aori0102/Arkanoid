package game.Player;

import game.Player.Prefab.PlayerStatUIPrefab;
import org.Annotation.LinkViaPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Text.TextUI;

public final class PlayerStatUI extends MonoBehaviour {

    @LinkViaPrefab
    private TextUI labelText = null;

    @LinkViaPrefab
    private TextUI amountText = null;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerStatUI(GameObject owner) {
        super(owner);
    }

    public void setLabel(String text) {
        labelText.setText(text);
    }

    public void setAmountText(double amount, double multiplier) {
        var multiplierDifference = Math.round((multiplier - 1) * 1000) / 10.0;
        String multiplierPrefix = "";
        if (multiplierDifference > 0) {
            multiplierPrefix = "+";
        } else if (multiplierDifference < 0) {
            multiplierPrefix = "-";
        }
        var text = String.format("%.1f", amount) + " ("
                + multiplierPrefix
                + String.format("%.1f", Math.abs(multiplierDifference)) + "%)";
        amountText.setText(text);
    }

    public void setAmountText(int amount, double multiplier) {
        var multiplierDifference = Math.round((multiplier - 1) * 1000) / 10.0;
        String multiplierPrefix = "";
        if (multiplierDifference > 0) {
            multiplierPrefix = "+";
        } else if (multiplierDifference < 0) {
            multiplierPrefix = "-";
        }
        var text = amount + " ("
                + multiplierPrefix
                + String.format("%.1f", Math.abs(multiplierDifference)) + "%)";
        amountText.setText(text);
    }

    /**
     * Link amount text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param amountText Amount text.
     */
    public void linkAmountText(TextUI amountText) {
        this.amountText = amountText;
    }

    /**
     * Link label text<br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerStatUIPrefab}
     * as part of component linking process.</i></b>
     *
     * @param labelText Label text.
     */
    public void linkLabelText(TextUI labelText) {
        this.labelText = labelText;
    }

}