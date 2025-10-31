package game.Damagable;

import javafx.scene.paint.Color;

public enum DamageType {

    Normal(Color.LIGHTGRAY),
    Critical(Color.YELLOW),
    Burn(Color.ORANGERED),
    HitPlayer(Color.RED);

    public final Color damageColor;

    DamageType(Color damageColor) {
        this.damageColor = damageColor;
    }

}
