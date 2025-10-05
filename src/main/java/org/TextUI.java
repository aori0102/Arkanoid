package org;

import javafx.scene.Node;
import org.UI.UI;
import javafx.geometry.VPos;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextUI extends UI implements IHasNode {
    private Text text = new Text();
    ;
    private String registeredSceneKey;

    public TextUI(GameObject owner) {
        super(owner);


    }

    @Override
    protected void destroyComponent() {

    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new TextUI(newOwner);
    }



    @Override
    public void update() {
        var transform = getTransform();
        var position = transform.getGlobalPosition();

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
        this.text = text;
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
    @Override
    public Node getNode(){
        return text;
    }
}
