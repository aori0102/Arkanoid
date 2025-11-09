package game.UI.Options.Text;

import game.UI.Title;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Main;
import org.Text.FontDataIndex;
import org.Text.TextHorizontalAlignment;
import org.Text.TextUI;
import org.Text.TextVerticalAlignment;
import utils.Time;
import utils.UITween.Ease;
import utils.UITween.Tween;
import utils.Vector2;

public class OptionsTitle extends Title {
    private TextUI textUI;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public OptionsTitle(GameObject owner) {
        super(owner);

        textUI = owner.addComponent(TextUI.class);
        textUI.setVerticalAlignment(TextVerticalAlignment.Middle);
        textUI.setHorizontalAlignment(TextHorizontalAlignment.Center);
        textUI.setFont(FontDataIndex.Jersey_25);
        textUI.setText("OPTIONS");
        textUI.getText().getStyleClass().add("neon-text");
        textUI.setFontSize(100);

        getTransform().setGlobalPosition(new Vector2(Main.STAGE_WIDTH/2, - TWEEN_DISTANCE/2 ));

    }

    @Override
    public void startAnimation(){
        Tween.to(getGameObject())
                .moveY(TWEEN_DISTANCE, TWEEN_DURATION)
                .ease(Ease.OUT_BACK)
                .setDelay(0.0)
                .ignoreTimeScale(true)
                .play();
    }



}
