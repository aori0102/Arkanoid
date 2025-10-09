package game.Brick;

public final class BrickObj {

    private int health;
    private final Type objType;

    private final int maxHealth;
    private boolean isNewDeath = false;
    private boolean isDamaged = false;
    public enum Type {Normal, Steel, Rock, Bomb, Gift, Diamond, Ball, Rocket, Reborn, Angel}
    public enum typeBrick {

        Normal ( 50, 10,32,  64, Type.Normal),
        Steel (1000, 1000,32, 64, Type.Steel),
        Diamond(1000000000, 1000000000, 32, 64, Type.Diamond),
        Rocket(50, 10, 32, 64, Type.Rocket),
        Rock (50, 30,32, 64, Type.Rock),
        Bomb(50, 30, 32, 64, Type.Bomb),
        Gift (10, 10, 32, 64, Type.Gift),
        Ball (10, 10, 32, 64, Type.Ball),
        Reborn(10, 10, 32, 64, Type.Reborn),
        Angel(10, 10, 32, 64, Type.Angel);


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


    public BrickObj(typeBrick brick) {
        this.health = brick.getHealth();
        this.maxHealth =  brick.getMaxHealth();
        this.objType = brick.getType();
    }

    public int getHealth() {
        return this.health;
    }

    public void hitDamage(int damge) {
        if (this.health < 0) {
            return;
        }

        this.health -= damge;
        this.isDamaged = true;
        
        if (this.health < 0) {
            this.health = 0;
            this.isNewDeath = true;
        }
    }

    public void incHealth(int inc) {
        if(this.health <= 0) {
            return;
        }

        this.health += inc;
        if(this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
    }

    public void decHealth(int inc) {
        if(this.health <= 0) {
            return;
        }

        this.health -= inc;
        if(this.health < 0) {
            this.health = 0;
        }
    }

    public void beDestroy() {
        this.health = 0;
        this.isNewDeath = true;
    }

    public boolean isDestroyed() {
        return this.health <= 0;
    }

    public Type getObjType() {
        return this.objType;
    }

    public boolean isObjNewDeath() {
        return this.isNewDeath;
    }

    public void setObjDeathState() {
        if(this.isNewDeath && this.isDestroyed()) {
            this.isNewDeath = false;
            return;
        }
        this.isNewDeath = true;
    }

    public boolean isJustDamaged() {
        return this.isDamaged;
    }

    public void resetIsDamaged() {
        this.isDamaged = false;
    }
    public void resetNewDeath() {
        this.isNewDeath = false;
    }

}
