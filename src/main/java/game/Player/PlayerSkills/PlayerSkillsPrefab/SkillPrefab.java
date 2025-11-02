package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.Paddle.PlayerPaddle;
import game.Player.PlayerSkills.Skill;

public abstract class SkillPrefab {
    public abstract Skill skillGenerator(PlayerPaddle playerPaddle);
}
