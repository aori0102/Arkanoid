package game.Brick;

import org.GameObject;
import org.MonoBehaviour;

public final class BrickObj {

    private int health;
    private final Type objType;

    private final int maxHealth;
    private final double width, height;

    public enum Type {Normal, Steel, Rock, Nuclear, Gift}
    public enum typeBrick {

        Normal ( 50, 10,32,  64, Type.Normal),
        Steel (1000, 1000,32, 64, Type.Steel),
        Rock (50, 30,32, 64, Type.Rock),
        Nuclear(50, 30, 32, 64, Type.Nuclear),
        Gift (10, 10, 32, 64, Type.Gift);

        private final int maxHealth;
        private final Type type;
        private final double width;
        private final double height;
        private int health;

        typeBrick(int _maxHealth, int _health, double _width, double _height, Type _type) {
            this.maxHealth = _maxHealth;
            this.health = _health;
            this.width = _width;
            this.height = _height;
            this.type = _type;
        }

        public int getMaxHealth() {
            return maxHealth;
        }

        public int getHealth() {
            return health;
        }

        public double getWidth() {
            return width;
        }

        public double getHeight() {
            return height;
        }

        public Type getType() {
            return type;
        }

        public static typeBrick getType(int num) {
            typeBrick[] arr= typeBrick.values();
            if(num < 0 || num > arr.length) num = 0;
            return arr[num];
        }
    }

    public double getWidth() {
        return this.width;
    }

    public double getHeight() {
        return this.height;
    }

    public BrickObj(typeBrick brick) {
        this.health = brick.getHealth();
        this.maxHealth =  brick.getMaxHealth();
        this.width = brick.getWidth();
        this.height = brick.getHeight();
        this.objType = brick.getType();
    }

    public void hitDamage(int damge) {
        this.health -= damge;
        
        if (this.health < 0) {
            this.health = 0;
        }
    }

    public void incHealth(int inc) {
        this.health += inc;
        if(this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public void autoDestroy() {
        this.health = 0;
    }

    public Type getObjType() {
        return this.objType;
    }

    public int getHealth() {
        return this.health;
    }
}
