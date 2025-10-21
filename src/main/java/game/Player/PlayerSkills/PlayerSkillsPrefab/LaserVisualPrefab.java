package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.PlayerSkills.LaserBeam;
import org.GameObject.GameObjectManager;

public final class LaserVisualPrefab {

     public static LaserBeam instantiate() {
         var laserBeam = GameObjectManager.instantiate("LaserBeam");
         laserBeam.addComponent(LaserBeam.class);

         return laserBeam.getComponent(LaserBeam.class);
     }

}
