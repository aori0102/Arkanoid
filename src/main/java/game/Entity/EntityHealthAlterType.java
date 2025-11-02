package game.Entity;

import javafx.scene.paint.Color;

public enum EntityHealthAlterType {
    PlayerTakeDamage(Color.RED),
    NormalDamage(Color.GREY),
    CriticalDamage(Color.YELLOW),
    BurnDamage(Color.ORANGE),
    Regeneration(Color.GREEN);

    public final Color displayColor;

    EntityHealthAlterType(Color displayColor) {
        this.displayColor = displayColor;
    }
}
