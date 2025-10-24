package game.Player.PlayerSkills.PlayerSkillsPrefab;

import game.Player.PlayerPaddle;
import game.Player.PlayerSkills.Skill;

public abstract class SkillPrefab {
    public abstract Skill skillGenerator(PlayerPaddle playerPaddle);
}
