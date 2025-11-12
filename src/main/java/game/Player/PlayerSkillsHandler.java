package game.Player;

import game.Player.PlayerSkills.SkillIndex;
import game.Player.PlayerSkills.Skills.Skill;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

import java.util.EnumMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private final EnumMap<SkillIndex, Skill> skillMap = new EnumMap<>(SkillIndex.class);

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerSkillsHandler(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Player.getInstance().getPlayerController().getActionMap().
                onKeyPressed.addListener(this::handleSkillRequest);
    }

    private void handleSkillRequest(Object o, ActionMap.Action action) {
        switch (action) {
            // Press Q
            case LaserBeam -> handleLogicBasedSkill(SkillIndex.LaserBeam);
            // Press E
            case Updraft -> handleLogicBasedSkill(SkillIndex.Updraft);
            // Press X
            case Invincible -> handleLogicBasedSkill(SkillIndex.Invincible);
            // Press SHIFT
            case Dash -> handleLogicBasedSkill(SkillIndex.Dash);
        }
    }

    private void handleLogicBasedSkill(SkillIndex skillIndex) {
        skillMap.get(skillIndex).useSkill();
    }

    public Skill getSkillData(SkillIndex skillIndex) {
        return skillMap.get(skillIndex);
    }

    public void assignSkill(SkillIndex skillIndex, Skill skill) {
        skillMap.put(skillIndex, skill);
    }

}
