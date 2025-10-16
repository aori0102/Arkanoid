package org;

public class InitScenes extends MonoBehaviour {

    /**
     * Create this MonoBehaviour.
     *
     * @param owner The owner of this component.
     */
    public InitScenes(GameObject owner) {
        super(owner);
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void destroyComponent() {

    }


}
