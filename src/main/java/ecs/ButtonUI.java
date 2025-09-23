package ecs;
import ecs.MouseEvent.*;
import javafx.scene.text.Text;


public class ButtonUI extends UI
        implements IPointerDownHandler,
        IPointerClickHandler,
        IPointerUpHandler,
        IPointerEnterHandler,
        IPointerExitHandler {

    private SpriteRenderer image;
    private Runnable buttonClickFunction;
    private Runnable buttonEnterFunction;
    private Runnable buttonUpFunction;
    private Runnable buttonExitFunction;
    private Runnable buttonDownFunction;
    private Text text;



    /**
     * Get the game object this MonoBehaviour is attached to.
     *
     * @param owner The owner of this component.
     */
    public ButtonUI(GameObject owner) {

        super(owner);

        image = owner.addComponent(SpriteRenderer.class);

        buttonClickFunction = null;
        attachPointerClick(image.getView());
        attachPointerEnter(image.getView());
        attachPointerUp(image.getView());
        attachPointerDown(image.getView());
        attachPointerExited(image.getView());

    }

    public void setImage(String path){
        image.setImage(path);

        attachPointerClick(image.getView());
        attachPointerEnter(image.getView());
        attachPointerUp(image.getView());
        attachPointerDown(image.getView());
        attachPointerExited(image.getView());

    }

    public void setAdditionalOnClick(Runnable action){
        if(buttonClickFunction == null){
            buttonClickFunction = action;
        } else{
            Runnable oldFunction = buttonClickFunction;
            buttonClickFunction = ()->{
              oldFunction.run();
              action.run();
            };
        }
    }
    public void setOnClick(Runnable action){
        buttonClickFunction = action;
    }

    @Override
    public void onPointerClicked() {
        if (buttonClickFunction != null) {
            buttonClickFunction.run();
        }
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new ButtonUI(newOwner);
    }

    @Override
    protected void clear() {

    }

    @Override
    public void onPointerEntered() {
        if (buttonEnterFunction != null) {
            buttonEnterFunction.run();
        }
    }

    @Override
    public void onPointerUp() {
        if (buttonUpFunction != null) {
            buttonUpFunction.run();
        }
    }

    @Override
    public void onPointerExited() {
        if (buttonExitFunction != null) {
            buttonExitFunction.run();
        }
    }

    @Override
    public void onPointerDown() {
        if(buttonClickFunction != null) {
            buttonClickFunction.run();
        }
    }
}
