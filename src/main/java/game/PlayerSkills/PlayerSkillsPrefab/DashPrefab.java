package game.PlayerSkills.PlayerSkillsPrefab;

import game.PlayerSkills.Skills.Dash;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class DashPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("Dash").addComponent(Dash.class).getGameObject();
    }
}
