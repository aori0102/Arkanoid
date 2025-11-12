package game.Player;

import game.Player.PlayerSkills.SkillIndex;
import game.Player.PlayerSkills.Skills.Skill;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

import java.util.EnumMap;

/**
 * PlayerSkillsHandler manages all the skills that the Player can use.
 * - Stores skills in a map keyed by SkillIndex.
 * - Handles input from the ActionMap to invoke the corresponding skill.
 */
public class PlayerSkillsHandler extends MonoBehaviour {

    // Stores all skills mapped by their SkillIndex
    private final EnumMap<SkillIndex, Skill> skillMap = new EnumMap<>(SkillIndex.class);

    /**
     * Constructor for PlayerSkillsHandler.
     *
     * @param owner The GameObject that owns this component.
     */
    public PlayerSkillsHandler(GameObject owner) {
        super(owner);
    }

    @Override
    public void awake() {
        Player.getInstance().getPlayerController().getActionMap().
                onKeyPressed.addListener(this::handleSkillRequest);
    }

    /**
     * Handles the key press events to trigger the appropriate skill.
     *
     * @param o      The sender of the event (usually ActionMap).
     * @param action The action/key that was pressed.
     */
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

    /**
     * Invokes the skill corresponding to the given SkillIndex.
     *
     * @param skillIndex The index of the skill to use.
     */
    private void handleLogicBasedSkill(SkillIndex skillIndex) {
        skillMap.get(skillIndex).useSkill();
    }

    /**
     * Gets the Skill object for a specific SkillIndex.
     *
     * @param skillIndex The index of the skill.
     * @return The Skill object.
     */
    public Skill getSkillData(SkillIndex skillIndex) {
        return skillMap.get(skillIndex);
    }

    /**
     * Assigns a Skill instance to a specific SkillIndex.
     *
     * @param skillIndex The index of the skill.
     * @param skill      The Skill instance to assign.
     */
    public void assignSkill(SkillIndex skillIndex, Skill skill) {
        skillMap.put(skillIndex, skill);
    }

}
