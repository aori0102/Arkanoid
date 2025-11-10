package game.PlayerSkills;

import org.Rendering.ImageAsset;

public enum SkillIndex {

    LaserBeam(ImageAsset.ImageIndex.Player_UI_Skill_Icon_LaserBeamSkillIcon, 5, 5.0,"Q"),
    Updraft(ImageAsset.ImageIndex.Player_UI_Skill_Icon_UpdraftSkillIcon, 1, 5.0,"E"),
    Invincible(ImageAsset.ImageIndex.Player_UI_Skill_Icon_InvincibleSkillIcon, 1, 10.0,"X"),
    Dash(ImageAsset.ImageIndex.Player_UI_Skill_Icon_DashSkillIcon, 2, 0.5,"SHIFT");

    public final ImageAsset.ImageIndex skillIcon;
    public final int maxSkillCharge;
    public final double baseSkillCooldown;
    public final String keybind;

    SkillIndex(ImageAsset.ImageIndex imageIndex, int maxSkillCharge, double baseSkillCooldown, String keybind) {
        this.skillIcon = imageIndex;
        this.maxSkillCharge = maxSkillCharge;
        this.baseSkillCooldown = baseSkillCooldown;
        this.keybind = keybind;
    }


}
