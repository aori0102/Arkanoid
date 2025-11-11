package game.Entity;

import javafx.scene.paint.Color;

public enum EntityHealthAlterType {
    PlayerTakeDamage(Color.RED),
    NormalDamage(Color.GREY),
    CriticalDamage(Color.YELLOW),
    BurnDamage(Color.ORANGE),
    FrostDamage(Color.CYAN),
    ElectrifiedDamage(Color.MAGENTA),
    Regeneration(Color.GREEN);

    public final Color displayColor;

    EntityHealthAlterType(Color displayColor) {
        this.displayColor = displayColor;
    }

    public boolean isHealing() {
        return this == Regeneration;
    }

    public boolean isDamage() {
        return !isHealing();
    }

}
