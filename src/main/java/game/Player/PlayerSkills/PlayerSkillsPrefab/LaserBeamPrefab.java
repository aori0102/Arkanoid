package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.Player;
import game.Player.Paddle.PlayerPaddle;
import game.Player.PlayerSkills.LaserBeam;
import game.Player.PlayerSkills.Skill;
import org.GameObject.GameObjectManager;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import javax.swing.*;

public class LaserBeamPrefab extends SkillPrefab {
    @Override
    public Skill skillGenerator(PlayerPaddle playerPaddle) {
        var laserBeam = GameObjectManager.instantiate("LaserBeam")
                .addComponent(LaserBeam.class);
        laserBeam.getComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));
        laserBeam.getComponent(BoxCollider.class);
        laserBeam.getTransform().setLocalScale(new Vector2(0.5, 0.5));
        laserBeam.getTransform().setGlobalPosition(
                Player.getInstance().getPlayerPaddle().getTransform().getGlobalPosition()
        );

        var collider = laserBeam.getComponent(BoxCollider.class);
        collider.setLocalCenter(new Vector2(0, 0));
        collider.setLocalSize(new Vector2(32, 256));

        return laserBeam;
    }
}
