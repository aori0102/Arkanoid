package game.Player.PlayerSkills;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

public abstract class Skill extends MonoBehaviour {

    protected SkillIndex skillIndex = SkillIndex.None;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Skill(GameObject owner) {
        super(owner);
        owner.setLayer(Layer.Skill);
        addComponent(BoxCollider.class).setTrigger(true);
        addComponent(SpriteRenderer.class).setPivot(new Vector2(0.5, 0.5));

    }

    protected void setSkillIndex(SkillIndex skillIndex) {
        this.skillIndex = skillIndex;
        getComponent(SpriteRenderer.class).setImage(skillIndex.getImageIndex().getImage());
    }

    public abstract void invoke() ;
}
