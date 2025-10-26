package game.Player;

import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.Skill;
import game.Player.PlayerSkills.SkillPrefabGenerator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import utils.Time;

import java.util.HashMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private final HashMap<Class<? extends Skill>, SkillData> skillDataMap = createSkillDataMap();

    private class SkillData{
        public int skillCharge;
        public double skillCooldownTime;
        public final int maxSkillCharge;
        public final double baseSkillCooldown;

        public SkillData(int skillCharge, double skillCooldownTime, int maxSkillCharge, double baseSkillCooldown) {
            this.skillCharge = skillCharge;
            this.skillCooldownTime = skillCooldownTime;
            this.maxSkillCharge = maxSkillCharge;
            this.baseSkillCooldown = baseSkillCooldown;
        }

    }

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

    public void update() {
        handleSKillCooldown();
    }

    private void handleSkillRequest(Object o,ActionMap.Action action) {
        switch (action) {
            case Skill1 -> spawnSkill(LaserBeam.class);
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
                skillDataMap.get(key).skillCooldownTime -= Time.deltaTime;

                if (skillDataMap.get(key).skillCooldownTime <= 0) {
                    skillDataMap.get(key).skillCharge++;
                    skillDataMap.get(key).skillCooldownTime = skillDataMap.get(key).baseSkillCooldown;
                }
            }
        }
    }

    private HashMap<Class<? extends Skill>, SkillData> createSkillDataMap() {
        HashMap<Class<? extends Skill>, SkillData> skillMap = new HashMap<>();

        skillMap.put(LaserBeam.class, new SkillData(3, 5.0, 3, 5.0));

        return skillMap;
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
