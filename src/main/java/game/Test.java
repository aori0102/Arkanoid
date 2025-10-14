package game;

import org.AnimationClipData;
import org.GameObject;
import org.MonoBehaviour;
import org.SpriteAnimator;

public class Test extends MonoBehaviour {

    private SpriteAnimator animator;

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public Test(GameObject owner) {
        super(owner);

        animator = addComponent(SpriteAnimator.class);
        animator.addAnimationClip(AnimationClipData.Start_Button_Idle);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return new Test(newOwner);
    }

    @Override
    protected void destroyComponent() {
        animator = null;
    }

    @Override
    public void awake() {
        animator.playAnimation(AnimationClipData.Start_Button_Idle);
    }

}
