package ecs;

import utils.Vector2;

public class BoxCollider extends MonoBehaviour {

    private Vector2 center = new Vector2();
    private Vector2 size = new Vector2();

    @Override
    protected MonoBehaviour clone(MonoBehaviour source) {
        BoxCollider newBoxCollider = new BoxCollider();
        newBoxCollider.center = this.center;
        newBoxCollider.size = this.size;
        return newBoxCollider;
    }

    @Override
    protected void clear() {
        center = null;
        size = null;
    }

}
