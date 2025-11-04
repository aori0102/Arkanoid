package utils.UITween;

public enum Ease {

    LINEAR {
        @Override public double apply(double t) {
            return t;
        }
    },

    IN {
        @Override public double apply(double t) {
            return t * t;
        }
    },

    OUT {
        @Override public double apply(double t) {
            return t * (2 - t);
        }
    },

    IN_OUT {
        @Override public double apply(double t) {
            return t < 0.5 ? 2 * t * t : -1 + (4 - 2 * t) * t;
        }
    },

    IN_CUBIC {
        @Override public double apply(double t) {
            return t * t * t;
        }
    },

    OUT_CUBIC {
        @Override public double apply(double t) {
            double p = t - 1;
            return p * p * p + 1;
        }
    },

    IN_OUT_CUBIC {
        @Override public double apply(double t) {
            return t < 0.5 ? 4 * t * t * t : 1 - Math.pow(-2 * t + 2, 3) / 2;
        }
    },

    // === BACK EASING ===
    IN_BACK {
        @Override public double apply(double t) {
            double s = 1.70158;
            return t * t * ((s + 1) * t - s);
        }
    },

    OUT_BACK {
        @Override public double apply(double t) {
            double s = 1.70158;
            t -= 1;
            return t * t * ((s + 1) * t + s) + 1;
        }
    },

    IN_OUT_BACK {
        @Override public double apply(double t) {
            double s = 1.70158 * 1.525;
            t *= 2;
            if (t < 1) return 0.5 * (t * t * ((s + 1) * t - s));
            t -= 2;
            return 0.5 * (t * t * ((s + 1) * t + s) + 2);
        }
    },

    // === ELASTIC EASING ===
    IN_ELASTIC {
        @Override public double apply(double t) {
            if (t == 0 || t == 1) return t;
            double p = 0.3;
            return -Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1 - p / 4) * (2 * Math.PI) / p);
        }
    },

    OUT_ELASTIC {
        @Override public double apply(double t) {
            if (t == 0 || t == 1) return t;
            double p = 0.3;
            return Math.pow(2, -10 * t) * Math.sin((t - p / 4) * (2 * Math.PI) / p) + 1;
        }
    },

    // === BOUNCE EASING ===
    OUT_BOUNCE {
        @Override public double apply(double t) {
            double n1 = 7.5625;
            double d1 = 2.75;

            if (t < 1 / d1) {
                return n1 * t * t;
            } else if (t < 2 / d1) {
                t -= 1.5 / d1;
                return n1 * t * t + 0.75;
            } else if (t < 2.5 / d1) {
                t -= 2.25 / d1;
                return n1 * t * t + 0.9375;
            } else {
                t -= 2.625 / d1;
                return n1 * t * t + 0.984375;
            }
        }
    },

    IN_BOUNCE {
        @Override public double apply(double t) {
            return 1 - OUT_BOUNCE.apply(1 - t);
        }
    },

    IN_OUT_BOUNCE {
        @Override public double apply(double t) {
            return t < 0.5
                    ? (1 - OUT_BOUNCE.apply(1 - 2 * t)) / 2
                    : (1 + OUT_BOUNCE.apply(2 * t - 1)) / 2;
        }
    };

    /** Apply this easing to normalized t (0 → 1). */
    public abstract double apply(double t);
}
