package game;

import org.Animation.AnimationClipData;
import org.GameObject.GameObject;
import org.GameObject.MonoBehaviour;
import org.Animation.SpriteAnimator;

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
    public void awake() {
        animator.playAnimation(AnimationClipData.Start_Button_Idle, null);
    }

}
