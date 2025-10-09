package game.Voltraxis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Data {

    /// Stat
    static final int BASE_ATTACK = 42;
    static final int BASE_MAX_HEALTH = 125;
    static final int BASE_DEFENSE = 36;

    /// Basic skill
    static final double BASIC_SKILL_COOLDOWN = 7.0;
    static final double ELECTRIC_BALL_ATTACK_PROPORTION = 0.632;

    /// Enhance skill
    static final double ENHANCE_SKILL_COOLDOWN = 30;
    static final double ENHANCE_SKILL_DURATION = 20;
    static final double ENHANCE_ATTACK_INCREMENT = 0.1;

    /// Groggy skill
    static final double GROGGY_BASIC_COOLDOWN_REDUCTION = 0.35;
    static final double GROGGY_TO_EX_CHARGE_TIME = 15.0;
    static final double GROGGY_ATTACK_INCREMENT = 0.2;

    /// Power core
    static final double POWER_CORE_PROPORTIONAL_HEALTH = 0.36;
    static final double POWER_CORE_DAMAGE_TAKEN_REDUCTION = 0.1;

    /// Image path
    enum ImageIndex {
        Skill_ArcDischarge,
        Skill_PowerSurge,
        Skill_Overhaul,
        Skill_SuperNova,
        State_Overhaul,
        State_Lockdown,
    }

    static final Map<ImageIndex, String> IMAGE_PATH_MAP;

    static {
        Map<ImageIndex, String> map = new HashMap<>();
        map.put(ImageIndex.Skill_ArcDischarge, "/Image/Skill/ArcDischarge.png");
        map.put(ImageIndex.Skill_PowerSurge, "/Image/Skill/PowerSurge.png");
        map.put(ImageIndex.Skill_Overhaul, "/Image/Skill/Overhaul.png");
        map.put(ImageIndex.Skill_SuperNova, "/Image/Skill/SuperNova.png");
        map.put(ImageIndex.State_Overhaul, "/Image/State/Overhaul.png");
        map.put(ImageIndex.State_Lockdown, "/Image/State/Lockdown.png");
        //noinspection Java9CollectionFactory
        IMAGE_PATH_MAP = Collections.unmodifiableMap(map);
    }

    /// Text path
    enum TextIndex {
        Skill_ArcDischarge,
        Skill_PowerSurge,
        Skill_Overhaul,
        Skill_SuperNova,
        State_Overhaul,
        State_Lockdown,
    }

    static final Map<TextIndex, String> TEXT_PATH_MAP;

    static {
        Map<TextIndex, String> map = new HashMap<>();
        map.put(TextIndex.Skill_ArcDischarge, "Description/Skill/ArcDischarge.txt");
        map.put(TextIndex.Skill_PowerSurge, "Description/Skill/PowerSurge.txt");
        map.put(TextIndex.Skill_Overhaul, "Description/Skill/Overhaul.txt");
        map.put(TextIndex.Skill_SuperNova, "Description/Skill/SuperNova.txt");
        map.put(TextIndex.State_Overhaul, "Description/State/Overhaul.txt");
        map.put(TextIndex.State_Lockdown, "Description/State/Lockdown.txt");
        //noinspection Java9CollectionFactory
        TEXT_PATH_MAP = Collections.unmodifiableMap(map);
    }

}