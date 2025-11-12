package org.Animation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/**
 * Enum class that loads and stores animation clips.
 */
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
    Pause_Button_Idle("/Animation/Button/PauseButton/Idle.json"),
    Pause_Button_Hovered("/Animation/Button/PauseButton/Hovered.json"),
    Pause_Button_Pressed("/Animation/Button/PauseButton/Pressed.json"),
    Pause_Button_Released("/Animation/Button/PauseButton/Released.json"),
    Pause_Button_Clicked("/Animation/Button/PauseButton/Clicked.json"),
    Resume_Button_Idle("/Animation/Button/ResumeButton/Idle.json"),
    Resume_Button_Hovered("/Animation/Button/ResumeButton/Hovered.json"),
    Resume_Button_Pressed("/Animation/Button/ResumeButton/Pressed.json"),
    Resume_Button_Released("/Animation/Button/ResumeButton/Released.json"),
    Resume_Button_Clicked("/Animation/Button/ResumeButton/Clicked.json"),
    GoBack_Button_Idle("/Animation/Button/GoBackButton/Idle.json"),
    GoBack_Button_Pressed("/Animation/Button/GoBackButton/Pressed.json"),
    GoBack_Button_Released("/Animation/Button/GoBackButton/Released.json"),
    GoBack_Button_Clicked("/Animation/Button/GoBackButton/Clicked.json"),
    GoBack_Button_Hovered("/Animation/Button/GoBackButton/Hovered.json"),
    Restart_Button_Idle("/Animation/Button/RestartButton/Idle.json"),
    Restart_Button_Pressed("/Animation/Button/RestartButton/Pressed.json"),
    Restart_Button_Released("/Animation/Button/RestartButton/Released.json"),
    Restart_Button_Clicked("/Animation/Button/RestartButton/Clicked.json"),
    Restart_Button_Hovered("/Animation/Button/RestartButton/Hovered.json"),
    /// Perk
    Health_Perk("/Animation/Perk/HealthPerk.json"),
    Attack_Perk("/Animation/Perk/AttackPerk.json"),
    Cooldown_Perk("/Animation/Perk/CooldownPerk.json"),
    Speed_Perk("/Animation/Perk/SpeedPerk.json"),

    _None("/");

    private SpriteAnimationClip animationClip;
    private final String path;

    private static final Gson animationClipLoader;

    static {
        animationClipLoader = new GsonBuilder()
                .registerTypeAdapter(SpriteAnimationClip.class, new AnimationClipAdapter())
                .create();
        for (var key : AnimationClipData.values()) {

            System.out.println("Loading animation clip: " + key + " at \"" + key.path + "\"");
            try {
                Reader reader = new InputStreamReader(
                        Objects.requireNonNull(AnimationClipData.class.getResourceAsStream(key.path))
                );
                key.animationClip = animationClipLoader
                        .fromJson(reader, SpriteAnimationClip.class);
            } catch (JsonSyntaxException e) {
                System.err.println(SpriteAnimator.class.getSimpleName() + " | Error while loading animation clip: " + e.getMessage());
            } catch (Exception e) {
                System.err.println(SpriteAnimator.class.getSimpleName() + " | Unknown error while loading animation clip: " + e.getMessage());
            }

        }
    }

    AnimationClipData(String path) {
        this.path = path;
    }

    public SpriteAnimationClip getAnimationClip() {
        return animationClip;
    }

}