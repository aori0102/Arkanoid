package game.Player.PlayerSkills;

import org.Rendering.ImageAsset;

public enum SkillIndex {

    LaserBeam(ImageAsset.ImageIndex.Player_UI_Skill_Icon_LaserBeamSkillIcon, 5, "Q"),
    Updraft(ImageAsset.ImageIndex.Player_UI_Skill_Icon_UpdraftSkillIcon, 1, "E"),
    Invincible(ImageAsset.ImageIndex.Player_UI_Skill_Icon_InvincibleSkillIcon, 1, "X"),
    Dash(ImageAsset.ImageIndex.Player_UI_Skill_Icon_DashSkillIcon, 2, "SHIFT");

    public final ImageAsset.ImageIndex skillIcon;
    public final int maxSkillCharge;
    public final String keybind;

    SkillIndex(ImageAsset.ImageIndex imageIndex, int maxSkillCharge, String keybind) {
        this.skillIcon = imageIndex;
        this.maxSkillCharge = maxSkillCharge;
        this.keybind = keybind;
    }


}
