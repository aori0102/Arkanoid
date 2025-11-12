package org.Prefab;

import game.Audio.MusicHandlerPrefab;
import game.Ball.BallParticlePrefab;
import game.Ball.BallPrefab;
import game.Ball.BallsManagerPrefab;
import game.Brick.BrickPrefab;
import game.Brick.Sound.BrickSFXHandlerPrefab;
import game.Config.ConfigManagerPrefab;
import game.Damagable.HealthChangePopUpUIPrefab;
import game.Damagable.HealthChangeVisualizerPrefab;
import game.GameManager.GameManagerPrefab;
import game.GameOver.GameOverInfoDisplayUIPrefab;
import game.GameOver.GameOverManagerPrefab;
import game.GameObject.Shield.ShieldPrefab;
import game.LaserBeam.LaserBeamPrefab;
import game.LaserBeam.LaserBeamSFXHandlerPrefab;
import game.Level.LevelManagerPrefab;
import game.Level.LevelUIPrefab;
import game.MapGenerator.BrickMapManagerPrefab;
import game.Obstacle.Index.ObstacleManagerPrefab;
import game.Particle.ExplodingBrick.ExplodingBrickParticlePrefab;
import game.Perks.Index.PerkManagerPrefab;
import game.Player.PlayerData.DataManagerPrefab;
import game.Player.Prefab.*;
import game.Player.PlayerSkills.SkillSetUIPrefab;
import game.Player.PlayerSkills.SkillUIPrefab;
import game.PowerUp.Prefab.*;
import game.PowerUp.Shield.ShieldPowerUpPrefab;
import game.Rank.RankManagerPrefab;
import game.Rank.RankUIPrefab;
import game.Scoreboard.ScoreboardPrefab;
import game.Score.ScoreManagerPrefab;
import game.Score.ScorePopUpPrefab;
import game.Score.ScoreUIPrefab;
import game.StatBoard.StatBoardPrefab;
import game.UI.MainMenu.MainMenuManagerPrefab;
import game.UI.Prefab.*;
import game.UI.Prefab.GameOverMenu.GameOverMenuButtonPrefab;
import game.UI.Prefab.GameOverMenu.RestartButtonPrefab;
import game.UI.Prefab.MainMenu.*;
import game.UI.Prefab.OptionsMenu.*;
import game.UI.Prefab.RecordMenu.RecordMenuManagerPrefab;
import game.UI.Prefab.RecordMenu.RecordTitlePrefab;
import game.Voltraxis.Prefab.*;

public enum PrefabIndex {

    /// Brick
    Brick(new BrickPrefab()),
    BrickSFXHandler(new BrickSFXHandlerPrefab()),

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
    Manager_ObstacleManager(new ObstacleManagerPrefab()),
    Manager_BallsManager(new BallsManagerPrefab()),
    Manager_OptionsManager(new OptionsManagerPrefab()),
    Manager_GoBackButtonManager(new GoBackButtonManagerPrefab()),
    Manager_MainMenuManager(new MainMenuManagerPrefab()),
    Manager_LevelManager(new LevelManagerPrefab()),
    Manager_ProgressManager(new DataManagerPrefab()),
    Manager_ConfigManager(new ConfigManagerPrefab()),
    Manager_RecordMenuManager(new RecordMenuManagerPrefab()),
    Manager_GameOverManager(new GameOverManagerPrefab()),

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

    /// Game over
    GameOverInfoDisplayUI(new GameOverInfoDisplayUIPrefab()),

    GoBackButton(new GoBackButtonPrefab()),

    /// RecordsMenu
    RecordTitle(new RecordTitlePrefab()),

    /// GameOverMenu
    GameOverMenuButton(new GameOverMenuButtonPrefab()),
    RestartButton(new RestartButtonPrefab()),

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
    Voltraxis_SFX(new VoltraxisSFXPrefab()),

    /// Introduction
    Introduction(new IntroductionPrefab()),

    /// Ball
    Ball(new BallPrefab()),
    Ball_Particle(new BallParticlePrefab()),

    /// Skill
    LaserBeam(new LaserBeamPrefab()),
    LaserBeamSFXHandler(new LaserBeamSFXHandlerPrefab()),

    /// Power up
    PowerUp_Placeholder(new PowerUpPlaceholderPrefab()),
    PowerUp_FireBall(new FireBallPrefab()),
    PowerUp_BlizzardBall(new BlizzardBallPrefab()),
    PowerUp_Recovery(new RecoveryPrefab()),
    PowerUp_Shield(new ShieldPowerUpPrefab()),
    PowerUp_Duplicate(new DuplicateBallPrefab()),
    PowerUp_Triplicate(new TriplicateBallPrefab()),

    /// Player
    Player(new PlayerPrefab()),
    Player_Paddle(new PaddlePrefab()),
    Player_HealthBar(new PlayerHealthBarPrefab()),
    Player_HealthLossVignette(new PlayerHealthLossVignettePrefab()),

    /// Shield
    Shield(new ShieldPrefab()),
    /// Audio
    MusicHandler(new MusicHandlerPrefab()),

    /// Particle
    ExplodingBrickParticle(new ExplodingBrickParticlePrefab()),

    /// Stat
    PlayerStatUI(new PlayerStatUIPrefab()),
    PlayerStatUIHandler(new PlayerStatUIHandlerPrefab()),

    None(null);

    public final Prefab prefab;

    PrefabIndex(Prefab prefab) {
        this.prefab = prefab;
    }

}
