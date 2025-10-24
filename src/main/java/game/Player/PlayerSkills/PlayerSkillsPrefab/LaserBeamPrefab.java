package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.Skill;
import org.GameObject.GameObjectManager;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public class LaserBeamPrefab extends SkillPrefab {
    @Override
    public Skill skillGenerator() {
        var laserBeam = GameObjectManager.instantiate("LaserBeam").addComponent(LaserBeam.class);
        laserBeam.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
        laserBeam.getTransform().setLocalScale(new Vector2(0.5, 0.5));
        laserBeam.getTransform().setGlobalPosition(new Vector2(100, 100));
        System.out.println(laserBeam.getTransform().getGlobalPosition());

        return laserBeam;
    }
}
