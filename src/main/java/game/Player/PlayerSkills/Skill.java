package game.Player.PlayerSkills;

import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Layer.Layer;
import org.Physics.BoxCollider;
import org.Rendering.SpriteRenderer;

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

        addComponent(BoxCollider.class).setExcludeLayer(Layer.Ball.getUnderlyingValue());
        getComponent(BoxCollider.class).setExcludeLayer(Layer.Paddle.getUnderlyingValue());

        addComponent(SpriteRenderer.class).setImage(skillIndex.getImageIndex().getImage());

    }

    public void awake() {
        assignColliderInfo();
    }

    public abstract void assignColliderInfo();

    protected void setSkillIndex(SkillIndex skillIndex) {
        this.skillIndex = skillIndex;
    }

    protected void destroyComponent() {

    }
}
