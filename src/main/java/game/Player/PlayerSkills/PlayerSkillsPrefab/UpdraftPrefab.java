package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.PlayerSkills.Skills.Updraft;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Prefab.Prefab;

public class UpdraftPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        return GameObjectManager.instantiate("Magnet").addComponent(Updraft.class).getGameObject();
    }
}
