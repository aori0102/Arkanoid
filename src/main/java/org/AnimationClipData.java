package org;

public enum AnimationClipData {
    Bocchi_Idle("/Animation/Bocchi/Idle.json"),
    _Placeholder("/something.json"),
    Dui_Button_Idle("/Duiooooooo.json"),
    Dui_Button_Hovered("/Duiooooooo.json"),
    Dui_Button_Pressed("/Duiooooooo.json"),
    Dui_Button_Released("/Duiooooooo.json"),
    Dui_Button_Clicked("/Duiooooooo.json"),
    Voltraxis_Idle("/Voltraxis/AnimationData/Idle/AnimationData.json"),
    Start_Button_Idle("/AnimationButton/StartButton/Idle.json"),
    Start_Button_Hovered("/AnimationButton/StartButton/Hovered.json"),
    Start_Button_Pressed("/AnimationButton/StartButton/Pressed.json"),
    Start_Button_Released("/AnimationButton/StartButton/Released.json"),
    Start_Button_Clicked("/AnimationButton/StartButton/Clicked.json"),
    Continue_Button_Idle("/AnimationButton/ContinueButton/Idle.json"),
    Continue_Button_Hovered("/AnimationButton/ContinueButton/Hovered.json"),
    Continue_Button_Pressed("/AnimationButton/ContinueButton/Pressed.json"),
    Continue_Button_Released("/AnimationButton/ContinueButton/Released.json"),
    Continue_Button_Clicked("/AnimationButton/ContinueButton/Clicked.json"),
    Options_Button_Idle("/AnimationButton/OptionsButton/Idle.json"),
    Options_Button_Hovered("/AnimationButton/OptionsButton/Hovered.json"),
    Options_Button_Pressed("/AnimationButton/OptionsButton/Pressed.json"),
    Options_Button_Released("/AnimationButton/OptionsButton/Released.json"),
    Options_Button_Clicked("/AnimationButton/OptionsButton/Clicked.json"),
    Record_Button_Idle("/AnimationButton/RecordButton/Idle.json"),
    Record_Button_Hovered("/AnimationButton/RecordButton/Hovered.json"),
    Record_Button_Pressed("/AnimationButton/RecordButton/Pressed.json"),
    Record_Button_Released("/AnimationButton/RecordButton/Released.json"),
    Record_Button_Clicked("/AnimationButton/RecordButton/Clicked.json"),
    Quit_Button_Idle("/AnimationButton/QuitButton/Idle.json"),
    Quit_Button_Hovered("/AnimationButton/QuitButton/Hovered.json"),
    Quit_Button_Pressed("/AnimationButton/QuitButton/Pressed.json"),
    Quit_Button_Released("/AnimationButton/QuitButton/Released.json"),
    Quit_Button_Clicked("/AnimationButton/QuitButton/Clicked.json"),
    Menu_Button_Idle("/AnimationButton/MenuButton/Idle.json"),
    Menu_Button_Hovered("/AnimationButton/MenuButton/Hovered.json"),
    Menu_Button_Pressed("/AnimationButton/MenuButton/Pressed.json"),
    Menu_Button_Released("/AnimationButton/MenuButton/Released.json"),
    Menu_Button_Clicked("/AnimationButton/MenuButton/Clicked.json"),




    _None("/");

    private final String animationClipDataPath;

    AnimationClipData(String path) {
        animationClipDataPath = path;
    }

    public String getAnimationClipDataPath() {
        return animationClipDataPath;
    }

}