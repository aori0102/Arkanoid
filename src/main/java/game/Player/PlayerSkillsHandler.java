package game.Player;

import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.PlayerSkillsPrefab.Dash;
import game.Player.PlayerSkills.Skill;
import game.Player.PlayerSkills.SkillPrefabGenerator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import utils.Time;

import static game.Player.PlayerSkills.SkillPrefabGenerator.skillDataMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private static final int DASH_SPEED = 2500;
    private static final double DASH_TIME = 0.2;

    private PlayerPaddle playerPaddle;
    private Time.CoroutineID dashCoroutineID;

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
       playerPaddle = getComponent(PlayerPaddle.class);
    }

    public void update() {
        handleSKillCooldown();
    }

    private void handleSkillRequest(Object o,ActionMap.Action action) {
        switch (action) {
            case Skill1 -> spawnSkill(LaserBeam.class);
            case Skill2 -> {}
            case Skill3 -> {}
            case Dash -> {
                var dashData = skillDataMap.get(Dash.class);
                if (dashData.skillCharge > 0) {
                    dashData.skillCharge--;
                    dashData.skillCooldownTime = dashData.baseSkillCooldown;

                    Player.getInstance().setCurrentSpeed(DASH_SPEED);
                    dashCoroutineID = Time.addCoroutine(this::resetDashSpeed, Time.getTime() + DASH_TIME);
                }
            }

        }
    }

    private void spawnSkill(Class<? extends Skill> skillClass) {

        if (skillDataMap.get(skillClass).skillCharge > 0) {
            SkillPrefabGenerator.skillPrefabSet.get(skillClass).skillGenerator(playerPaddle);
            skillDataMap.get(skillClass).skillCharge --;
        }
    }

    private void handleSKillCooldown() {
        for (var key : skillDataMap.keySet()) {
            int currentSkillCharge = skillDataMap.get(key).skillCharge;
            int maxSkillCharge = skillDataMap.get(key).maxSkillCharge;

            if (currentSkillCharge < maxSkillCharge) {
                skillDataMap.get(key).skillCooldownTime -= Time.getDeltaTime();

                if (skillDataMap.get(key).skillCooldownTime <= 0) {
                    skillDataMap.get(key).skillCharge++;
                    skillDataMap.get(key).skillCooldownTime = skillDataMap.get(key).baseSkillCooldown;
                }
            }
        }
    }

    private void resetDashSpeed() {
        Player.getInstance().setCurrentSpeed(Player.getInstance().getBaseSpeed());
        Time.removeCoroutine(dashCoroutineID);
    }
}
