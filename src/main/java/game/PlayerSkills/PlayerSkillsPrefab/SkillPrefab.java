package game.PlayerSkills.PlayerSkillsPrefab;

import game.Player.Paddle.PlayerPaddle;
import game.PlayerSkills.Skills.Skill;

public abstract class SkillPrefab {
    public abstract Skill skillGenerator(PlayerPaddle playerPaddle);
}
