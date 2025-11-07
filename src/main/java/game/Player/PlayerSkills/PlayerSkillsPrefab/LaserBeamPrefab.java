package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.PlayerSkills.Skills.LaserBeam.LaserBeam;
import game.Player.PlayerSkills.Skills.LaserBeam.LaserBeamDamageDealer;
import game.Player.PlayerSkills.Skills.LaserBeam.LaserBeamStat;
import org.GameObject.GameObject;
import org.GameObject.GameObjectManager;
import org.Physics.BoxCollider;
import org.Prefab.Prefab;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class LaserBeamPrefab extends Prefab {
    @Override
    public GameObject instantiatePrefab() {
        var laserBeam = GameObjectManager.instantiate("LaserBeam")
                .addComponent(LaserBeamStat.class)
                .addComponent(LaserBeamDamageDealer.class)
                .addComponent(LaserBeam.class);
        laserBeam.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
        laserBeam.getComponent(BoxCollider.class);
        laserBeam.getTransform().setLocalScale(new Vector2(0.5, 0.5));

        var collider = laserBeam.getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(32, 256));

        return laserBeam.getGameObject();
    }
}
