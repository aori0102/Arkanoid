package org;

public enum AnimationClipData {
    Bocchi_Idle("/Animation/Bocchi/Idle.json"),
    _Placeholder("/something.json"),
    Dui_Button_Idle("/Duiooooooo.json"),
    Dui_Button_Hovered("/Duiooooooo.json"),
    Dui_Button_Pressed("/Duiooooooo.json"),
    Dui_Button_Released("/Duiooooooo.json"),
    Dui_Button_Clicked("/Duiooooooo.json"),
    Voltraxis_Idle("/Animation/Voltraxis/Idle/AnimationData.json"),
    _None("/");

    private final String animationClipDataPath;

    AnimationClipData(String path) {
        animationClipDataPath = path;
    }

    public String getAnimationClipDataPath() {
        return animationClipDataPath;
    }

}