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
    Start_Button_Idle("/Animation/Button/StartButton/Idle.json"),
    Start_Button_Hovered("/Animation/Button/StartButton/Hovered.json"),
    Start_Button_Pressed("/Animation/Button/StartButton/Pressed.json"),
    Start_Button_Released("/Animation/Button/StartButton/Released.json"),
    Start_Button_Clicked("/Animation/Button/StartButton/Clicked.json"),
    Continue_Button_Idle("/Animation/Button/ContinueButton/Idle.json"),
    Continue_Button_Hovered("/Animation/Button/ContinueButton/Hovered.json"),
    Continue_Button_Pressed("/Animation/Button/ContinueButton/Pressed.json"),
    Continue_Button_Released("/Animation/Button/ContinueButton/Released.json"),
    Continue_Button_Clicked("/Animation/Button/ContinueButton/Clicked.json"),
    Options_Button_Idle("/Animation/Button/OptionsButton/Idle.json"),
    Options_Button_Hovered("/Animation/Button/OptionsButton/Hovered.json"),
    Options_Button_Pressed("/Animation/Button/OptionsButton/Pressed.json"),
    Options_Button_Released("/Animation/Button/OptionsButton/Released.json"),
    Options_Button_Clicked("/Animation/Button/OptionsButton/Clicked.json"),
    Record_Button_Idle("/Animation/Button/RecordButton/Idle.json"),
    Record_Button_Hovered("/Animation/Button/RecordButton/Hovered.json"),
    Record_Button_Pressed("/Animation/Button/RecordButton/Pressed.json"),
    Record_Button_Released("/Animation/Button/RecordButton/Released.json"),
    Record_Button_Clicked("/Animation/Button/RecordButton/Clicked.json"),
    Quit_Button_Idle("/Animation/Button/QuitButton/Idle.json"),
    Quit_Button_Hovered("/Animation/Button/QuitButton/Hovered.json"),
    Quit_Button_Pressed("/Animation/Button/QuitButton/Pressed.json"),
    Quit_Button_Released("/Animation/Button/QuitButton/Released.json"),
    Quit_Button_Clicked("/Animation/Button/QuitButton/Clicked.json"),
    Menu_Button_Idle("/Animation/Button/MenuButton/Idle.json"),
    Menu_Button_Hovered("/Animation/Button/MenuButton/Hovered.json"),
    Menu_Button_Pressed("/Animation/Button/MenuButton/Pressed.json"),
    Menu_Button_Released("/Animation/Button/MenuButton/Released.json"),
    Menu_Button_Clicked("/Animation/Button/MenuButton/Clicked.json"),

    Health_Perk("/Animation/Perk/HealthPerk.json"),
    Attach_Perk("/Animation/Perk/AttachPerk.json"),
    Cooldown_Perk("/Animation/Perk/CooldownPerk.json"),
    Speed_Perk("/Animation/Perk/SpeedPerk.json"),


    _None("/");

    private final String animationClipDataPath;

    AnimationClipData(String path) {
        animationClipDataPath = path;
    }

    public String getAnimationClipDataPath() {
        return animationClipDataPath;
    }

}