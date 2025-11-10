package org.Prefab;

import game.Ball.BallParticlePrefab;
import game.Ball.BallPrefab;
import game.Ball.BallsManagerPrefab;
import game.Brick.BrickPrefab;
import game.Config.ConfigManagerPrefab;
import game.Damagable.HealthChangePopUpUIPrefab;
import game.Damagable.HealthChangeVisualizerPrefab;
import game.GameManager.GameManagerPrefab;
import game.LaserBeam.LaserBeamPrefab;
import game.Level.LevelManagerPrefab;
import game.Level.LevelUIPrefab;
import game.MapGenerator.BrickMapManagerPrefab;
import game.Obstacle.Index.ObstacleManagerPrefab;
import game.Perks.Index.PerkManagerPrefab;
import game.Player.Prefab.PaddlePrefab;
import game.Player.Prefab.PlayerHealthBarPrefab;
import game.Player.Prefab.PlayerHealthLossVignettePrefab;
import game.Player.Prefab.PlayerPrefab;
import game.PlayerData.DataManagerPrefab;
import game.PlayerSkills.SkillSetUIPrefab;
import game.PlayerSkills.SkillUIPrefab;
import game.PowerUp.Prefab.*;
import game.Rank.RankManagerPrefab;
import game.Rank.RankUIPrefab;
import game.Scoreboard.ScoreboardPrefab;
import game.Score.ScoreManagerPrefab;
import game.Score.ScorePopUpPrefab;
import game.Score.ScoreUIPrefab;
import game.StatBoard.StatBoardPrefab;
import game.UI.MainMenu.MainMenuManagerPrefab;
import game.UI.Prefab.*;
import game.UI.Prefab.MainMenu.*;
import game.UI.Prefab.OptionsMenu.*;
import game.UI.Prefab.RecordMenu.RecordMenuManagerPrefab;
import game.UI.Prefab.RecordMenu.RecordTitlePrefab;
import game.UI.UIManagerPrefab;
import game.Voltraxis.Prefab.*;

public enum PrefabIndex {

    /// Brick
    Brick(new BrickPrefab()),

    /// Level notification
    LevelNotificationUI(new LevelUIPrefab()),

    /// Player info board
    Scoreboard(new ScoreboardPrefab()),
    Scoreboard_RankUI(new RankUIPrefab()),
    Scoreboard_ScoreUI(new ScoreUIPrefab()),
    Scoreboard_ScoreUI_PopUp(new ScorePopUpPrefab()),

    /// Stat Board
    StatBoard(new StatBoardPrefab()),

    /// Skill set
    SkillUI(new SkillUIPrefab()),
    SkillSetUI(new SkillSetUIPrefab()),

    /// Manager
    Manager_ScoreManager(new ScoreManagerPrefab()),
    Manager_RankManager(new RankManagerPrefab()),
    Manager_BrickMapManager(new BrickMapManagerPrefab()),
    Manager_GameManager(new GameManagerPrefab()),
    Manager_PerkManager(new PerkManagerPrefab()),
    Manager_UIManager(new UIManagerPrefab()),
    Manager_ObstacleManager(new ObstacleManagerPrefab()),
    Manager_BallsManager(new BallsManagerPrefab()),
    Manager_OptionsManager(new OptionsManagerPrefab()),
    Manager_GoBackButtonManager(new GoBackButtonManagerPrefab()),
    Manager_MainMenuManager(new MainMenuManagerPrefab()),
    Manager_LevelManager(new LevelManagerPrefab()),
    Manager_ProgressManager(new DataManagerPrefab()),
    Manager_ConfigManager(new ConfigManagerPrefab()),
    Manager_RecordMenuManager(new RecordMenuManagerPrefab()),

    /// Health change visualization
    HealthChange_PopUp(new HealthChangePopUpUIPrefab()),
    HealthChange_VisualizeHandler(new HealthChangeVisualizerPrefab()),
    /// UI
    /// MainMenu
    PauseButton(new PauseButtonPrefab()),
    ResumeButton(new ResumeButtonPrefab()),
    MenuButton(new MenuButtonPrefab()),
    StartButton(new StartButtonPrefab()),
    ContinueButton(new ContinueButtonPrefab()),
    RecordButton(new RecordButtonPrefab()),
    OptionsButton(new OptionsButtonPrefab()),
    QuitButton(new QuitButtonPrefab()),
    GameTitle(new GameTitlePrefab()),
    /// OptionsMenu
    MasterVolumeSlider(new MasterVolumeSliderPrefab()),
    MusicVolumeSlider(new MusicVolumeSliderPrefab()),
    SFXVolumeSlider(new SFXVolumeSliderPrefab()),
    MasterVolumeText(new MasterVolumeTextPrefab()),
    SFXVolumeText(new SFXVolumeTextPrefab()),
    MusicVolumeText(new MusicVolumeTextPrefab()),
    OptionsTitle(new OptionsTitlePrefab()),

    GoBackButton(new GoBackButtonPrefab()),

    /// RecordsMenu
    RecordTitle(new RecordTitlePrefab()),

    /// Voltraxis
    Voltraxis(new VoltraxisPrefab()),
    Voltraxis_ChargingUI(new ChargingUIPrefab()),
    Voltraxis_EffectBarUI(new EffectBarUIPrefab()),
    Voltraxis_EffectIconUI(new EffectIconUIPrefab()),
    Voltraxis_ElectricBall(new ElectricBallPrefab()),
    Voltraxis_GroggyUI(new GroggyUIPrefab()),
    Voltraxis_HealthBarUI(new HealthBarUIPrefab()),
    Voltraxis_PowerCore(new PowerCorePrefab()),
    Voltraxis_UltimateLaser(new UltimateLaserPrefab()),
    Voltraxis_Visual(new VoltraxisVisualPrefab()),

    /// Ball
    Ball(new BallPrefab()),
    Ball_Particle(new BallParticlePrefab()),

    /// Skill
    LaserBeam(new LaserBeamPrefab()),

    /// Power up
    PowerUp_Placeholder(new PowerUpPlaceholderPrefab()),
    PowerUp_FireBall(new FireBallPrefab()),
    PowerUp_BlizzardBall(new BlizzardBallPrefab()),
    PowerUp_Recovery(new RecoveryPrefab()),
    PowerUp_Shield(new ShieldPrefab()),
    PowerUp_Duplicate(new DuplicateBallPrefab()),
    PowerUp_Triplicate(new TriplicateBallPrefab()),

    /// Player
    Player(new PlayerPrefab()),
    Player_Paddle(new PaddlePrefab()),
    Player_HealthBar(new PlayerHealthBarPrefab()),
    Player_HealthLossVignette(new PlayerHealthLossVignettePrefab()),

    /// Particle;

    None(null);

    public final Prefab prefab;

    PrefabIndex(Prefab prefab) {
        this.prefab = prefab;
    }

}
