package game.PlayerSkills.PlayerSkillsPrefab;

import game.PlayerSkills.Skills.Invincible;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class InvinciblePrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("InvinciblePrefab").addComponent(Invincible.class).getGameObject();
    }
}
