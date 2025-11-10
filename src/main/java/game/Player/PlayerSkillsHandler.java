package game.Player;

import game.Player.PlayerSkills.Skills.Invincible;
import game.Player.PlayerSkills.Skills.LaserBeam.LaserBeam;
import game.Player.PlayerSkills.Skills.Dash;
import game.Player.PlayerSkills.Skills.Updraft;
import game.Player.PlayerSkills.Skills.Skill;
import game.Player.PlayerSkills.SkillPrefabGenerator;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.InputAction.ActionMap;
import org.Prefab.PrefabManager;
import utils.Time;

import static game.Player.PlayerSkills.SkillPrefabGenerator.skillDataMap;

public class PlayerSkillsHandler extends MonoBehaviour {

    private Dash dash;
    private Updraft updraft;
    private Invincible invincible;

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

        dash = PrefabManager.instantiatePrefab(SkillPrefabGenerator
                .skillPrefabSet.get(Dash.class)).getComponent(Dash.class);
        updraft = PrefabManager.instantiatePrefab(SkillPrefabGenerator
                .skillPrefabSet.get(Updraft.class)).getComponent(Updraft.class);
        invincible = PrefabManager.instantiatePrefab(SkillPrefabGenerator
                .skillPrefabSet.get(Invincible.class)).getComponent(Invincible.class);
    }

    @Override
    public void update() {
        handleSKillCooldown();
    }

    private void handleSkillRequest(Object o, ActionMap.Action action) {
        switch (action) {
            // Press Q
            case Skill1 -> spawnSkill(LaserBeam.class);
            // Press E
            case Skill2 -> handleLogicBasedSkill(Updraft.class);
            // Press X
            case Skill3 -> handleLogicBasedSkill(Invincible.class);
            //Press SHIFT
            case Dash -> handleLogicBasedSkill(Dash.class);
        }
    }

    private void handleLogicBasedSkill(Class<? extends Skill> skillClass) {
        var skillData = skillDataMap.get(skillClass);
        if (skillData.skillCharge > 0) {
            skillData.skillCharge--;
            skillData.skillCooldownTime = skillData.baseSkillCooldown;

            if (skillClass == Dash.class) {
                dash.invoke();
            }
            if (skillClass == Updraft.class) {
                updraft.invoke();
            }
            if (skillClass == Invincible.class) {
                invincible.invoke();
            }
        }
    }

    private void spawnSkill(Class<? extends Skill> skillClass) {

        if (skillDataMap.get(skillClass).skillCharge > 0) {
            PrefabManager.instantiatePrefab(SkillPrefabGenerator.skillPrefabSet.get(skillClass));
            skillDataMap.get(skillClass).skillCharge--;
        }
    }

    private void handleSKillCooldown() {
        for (var key : skillDataMap.keySet()) {
            int currentSkillCharge = skillDataMap.get(key).skillCharge;
            int maxSkillCharge = skillDataMap.get(key).maxSkillCharge;

            if (currentSkillCharge < maxSkillCharge) {
                skillDataMap.get(key).skillCooldownTime -= Time.getDeltaTime();
//                System.out.println("This is called" +skillDataMap.get(key).skillCooldownTime);

                if (skillDataMap.get(key).skillCooldownTime <= 0) {
                    skillDataMap.get(key).skillCharge++;
                    skillDataMap.get(key).skillCooldownTime = skillDataMap.get(key).baseSkillCooldown;
                }
            }
        }
    }
}
