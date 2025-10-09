package org;

public enum AnimationClipData {
    Bocchi_Idle("/Animation/Bocchi/Idle.json"),
    _Placeholder("/something.json"),
    _None("/");

    private final String animationClipDataPath;

    AnimationClipData(String path) {
        animationClipDataPath = path;
    }

    public String getAnimationClipDataPath() {
        return animationClipDataPath;
    }

}