package org.Animation;

public enum AnimationClipData {
    _Placeholder("/something.json"),

    /// Voltraxis
    Voltraxis_Idle("/Voltraxis/Main/Idle/AnimationData.json"),
    Voltraxis_NormalAttack("/Voltraxis/Main/NormalAttack/AnimationData.json"),
    Voltraxis_PowerCore_Idle("/Voltraxis/Object/PowerCore/Idle/AnimationData_Idle.json"),
    Voltraxis_PowerCore_Idle_ChargingLow("/Voltraxis/Object/PowerCore/Idle/AnimationData_Low.json"),
    Voltraxis_PowerCore_Idle_ChargingMedium("/Voltraxis/Object/PowerCore/Idle/AnimationData_Medium.json"),
    Voltraxis_PowerCore_Idle_ChargingHigh("/Voltraxis/Object/PowerCore/Idle/AnimationData_High.json"),
    Voltraxis_UltimateLaser("/Voltraxis/Object/UltimateLaser/UltimateLaser.json"),
    Voltraxis_Charging_Phase_1("/Voltraxis/Main/Charging/AnimationData_Phase_1.json"),
    Voltraxis_Charging_Phase_2("/Voltraxis/Main/Charging/AnimationData_Phase_2.json"),
    Voltraxis_Charging_Phase_3("/Voltraxis/Main/Charging/AnimationData_Phase_3.json"),
    Voltraxis_Charging_Phase_4("/Voltraxis/Main/Charging/AnimationData_Phase_4.json"),
    Voltraxis_Charging_Phase_5("/Voltraxis/Main/Charging/AnimationData_Phase_5.json"),
    Voltraxis_Charging_Phase_6("/Voltraxis/Main/Charging/AnimationData_Phase_6.json"),
    Voltraxis_Charging_EnterCharging("/Voltraxis/Main/Charging/EnterCharging.json"),
    Voltraxis_Charging_ExitUltimate("/Voltraxis/Main/Charging/ExitUltimate.json"),
    Voltraxis_Charging_FinishCharging("/Voltraxis/Main/Charging/PrepareUnleash.json"),
    Voltraxis_Charging_UnleashingLaser("/Voltraxis/Main/Charging/UnleashingLaser.json"),

    /// Button
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

    /// Perk
    Health_Perk("/Animation/Perk/HealthPerk.json"),
    Attack_Perk("/Animation/Perk/AttackPerk.json"),
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