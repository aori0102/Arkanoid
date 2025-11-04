package org.Prefab;

import game.Ball.BallPrefab;
import game.Brick.BrickPrefab;
import game.Damagable.HealthChangePopUpUIPrefab;
import game.Damagable.HealthChangeVisualizerPrefab;
import game.GameManager.GameManagerPrefab;
import game.GameManager.LevelUIPrefab;
import game.MapGenerator.BrickMapManagerPrefab;
import game.Obstacle.Index.ObstacleManagerPrefab;
import game.Perks.Index.PerkManagerPrefab;
import game.Player.Prefab.PaddlePrefab;
import game.Player.Prefab.PlayerHealthBarPrefab;
import game.Player.Prefab.PlayerHealthLossVignettePrefab;
import game.Player.Prefab.PlayerPrefab;
import game.Rank.RankManagerPrefab;
import game.Rank.RankUIPrefab;
import game.PlayerInfoBoard.PlayerInfoBoardPrefab;
import game.Score.ScoreManagerPrefab;
import game.Score.ScorePopUpPrefab;
import game.Score.ScoreUIPrefab;
import game.UI.UIManagerPrefab;
import game.Voltraxis.Prefab.*;

public enum PrefabIndex {

    /// Brick
    Brick(new BrickPrefab()),

    /// Level notification
    LevelNotification(new LevelUIPrefab()),

    /// Player info board
    PlayerInfoBoard(new PlayerInfoBoardPrefab()),
    PlayerInfoBoard_RankUI(new RankUIPrefab()),
    PlayerInfoBoard_ScoreUI(new ScoreUIPrefab()),
    PlayerInfoBoard_ScoreUI_PopUp(new ScorePopUpPrefab()),

    /// Manager
    Manager_ScoreManager(new ScoreManagerPrefab()),
    Manager_RankManager(new RankManagerPrefab()),
    Manager_BrickMapManager(new BrickMapManagerPrefab()),
    Manager_GameManager(new GameManagerPrefab()),
    Manager_PerkManager(new PerkManagerPrefab()),
    Manager_UIManager(new UIManagerPrefab()),
    Manager_ObstacleManager(new ObstacleManagerPrefab()),

    /// Health change visualization
    HealthChange_PopUp(new HealthChangePopUpUIPrefab()),
    HealthChange_VisualizeHandler(new HealthChangeVisualizerPrefab()),

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

    /// Player
    Player(new PlayerPrefab()),

    Player_Paddle(new PaddlePrefab()),

    Player_HealthBar(new PlayerHealthBarPrefab()),

    Player_HealthLossVignette(new PlayerHealthLossVignettePrefab()),

    None(null);

    public final Prefab prefab;

    PrefabIndex(Prefab prefab) {
        this.prefab = prefab;
    }

}
