package game.Player;

import game.Obstacle.Index.ObstaclePrefabGenerator;
import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.PlayerSkillsPrefab.SkillPrefab;
import game.Player.PlayerSkills.Skill;
import game.Player.PlayerSkills.SkillPrefabGenerator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;

import java.util.HashMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private static final int MAX_SKILL_CHARGE = 3;

    private HashMap<Class<? extends Skill>, Integer> skillChargeMap = new HashMap<>() {
        {
            put(LaserBeam.class, 3);
        }
    };

    private PlayerPaddle playerPaddle;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public PlayerSkillsHandler(GameObject owner) {
        super(owner);
    }

    public void awake() {
       Player.getInstance().getPlayerController().getActionMap().
               onKeyPressed.addListener(this::handleSkillRequest);

    }

    private void handleSkillRequest(Object o,ActionMap.Action action) {
        switch (action) {
            case Skill1 -> spawnSkill(LaserBeam.class);
        }
    }

    private void spawnSkill(Class<? extends Skill> skillClass) {
        if (skillChargeMap.containsKey(skillClass)
            && skillChargeMap.get(skillClass) <= MAX_SKILL_CHARGE
            && skillChargeMap.get(skillClass) > 0) {
            SkillPrefabGenerator.skillPrefabSet.get(skillClass).skillGenerator(playerPaddle);
            skillChargeMap.put(skillClass, skillChargeMap.getOrDefault(skillClass, 0) - 1);
        }
    }

    /**
     * <br><br>
     * <b><i><u>NOTE</u> : Only use within {@link PlayerSkillsHandler }
     * as part of component linking process.</i></b>
     * @param playerPaddle .
     */
    public void linkPlayerPaddle(PlayerPaddle playerPaddle) {
        this.playerPaddle = playerPaddle;
    }

    @Override
    protected void destroyComponent() {

    }
}
