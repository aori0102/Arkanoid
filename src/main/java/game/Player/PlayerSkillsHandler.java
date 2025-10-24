package game.Player;

import game.Obstacle.Index.ObstaclePrefabGenerator;
import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.PlayerSkillsPrefab.SkillPrefab;
import game.Player.PlayerSkills.Skill;
import game.Player.PlayerSkills.SkillPrefabGenerator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private int count = 0;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerSkillsHandler(GameObject owner) {
        super(owner);
    }

    public void awake() {
        Player.getInstance().getPlayerController().
                onSkillsInputRequested.addListener(this::handleSkillRequest);
    }

    private void handleSkillRequest(Object o,ActionMap.Action action) {
        switch (action) {
            case Skill1 -> {
                spawnSkill(LaserBeam.class);
            }
        }
    }

    private void spawnSkill(Class<? extends Skill> skillClass) {
        SkillPrefabGenerator.skillPrefabSet.get(skillClass).skillGenerator();
    }

    @Override
    protected void destroyComponent() {

    }
}
