package game.Player;

import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.PlayerSkillsPrefab.LaserVisualPrefab;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    // TODO : Finish this class
    private PlayerPaddle paddle;
    private ActionMap actionMap;



    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerSkillsHandler(GameObject owner) {
        super(owner);
        addComponent(ActionMap.class);
    }

    public void awake() {
        handleSkillButtonPressed();
    }

    public void handleSkillButtonPressed() {
        for (ActionMap.Action action : ActionMap.Action.values()) {
            if (actionMap.isActionPresented(action)) {
                switch (action) {
                    case Skill1 -> {
                        LaserBeam laserBeam = LaserVisualPrefab.instantiate();
                        laserBeam.getTransform().setGlobalPosition(paddle.getTransform().getGlobalPosition());
                    }

                    case Skill2 -> {

                    }

                    case Skill3 -> {


                    }
                }
            }
        }
    }


    /**
     * Link the paddle.
     * @param paddle .
     */
    public void linkPaddle(PlayerPaddle paddle) {
        this.paddle = paddle;
    }

    /**
     * Link Action map.
     * @param actionMap .
     */
    public void linkActionMap(ActionMap actionMap) {
        this.actionMap = actionMap;
    }


    @Override
    protected void destroyComponent() {

    }
}
