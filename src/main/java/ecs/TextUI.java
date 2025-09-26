package ecs;

import ecs.UI.UI;
import javafx.geometry.VPos;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextUI extends UI {
    private Text text = new Text();
    ;

    public TextUI(GameObject owner) {
        super(owner);

        RendererManager.RegisterNode(text);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new TextUI(newOwner);
    }

    @Override
    protected void clear() {

    }

    @Override
    public void update() {
        var transform = transform();
        var position = transform.getPosition();

        if(text.getTextOrigin() != VPos.TOP){
            text.setTextOrigin(VPos.TOP); //Set the box of text's pivot to the top left corner
        }


        text.setX(position.x);   // position on X
        text.setY(position.y);  // position on Y


    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        RendererManager.UnregisterNode(this.text); // remove old one if needed
        this.text = text;
        RendererManager.RegisterNode(this.text);   // add new one
    }

    public void setText(String text) {
        this.text.setText(text);
    }

    public String getTextString() {
        return text.getText();
    }

    public double getTextWidth() {
        return text.getBoundsInLocal().getWidth();
    }

    public double getTextHeight() {
        return text.getBoundsInLocal().getHeight();
    }
    @Override
    public double getWidth() {
        return text.getLayoutBounds().getWidth();
    }
    @Override
    public double getHeight() {
        return text.getLayoutBounds().getHeight();
    }
}
