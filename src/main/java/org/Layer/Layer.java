package org.Layer;

/**
 * Logical layers for collision detection use.
 */
public enum Layer {

    None(0),
    Default(1),
    PowerUp(2),
    Boss(3),
    Player(4),
    _5(5),
    _6(6),
    _7(7),
    Paddle(8),
    Ball(9),
    Brick(10),
    Skill(11),
    _12(12),
    _13(13),
    _14(14),
    _15(15),
    _16(16),
    _17(17),
    _18(18),
    _19(19),
    _20(20),
    _21(21),
    _22(22),
    _23(23),
    _24(24),
    _25(25),
    _26(26),
    _27(27),
    _28(28),
    _29(29),
    _30(30),
    _31(31),
    _32(32);

    private static final int MAX_LAYER = 32;
    private final int layerIndex;
    private final int underlyingValue;
    public static final int EVERYTHING = 0xFFFFFFFF;

    Layer(int index) {
        if (index < 0 || index > MAX_LAYER) {
            throw new IllegalArgumentException("Enum value must be between 0 and " + MAX_LAYER);
        }
        layerIndex = index;
        underlyingValue = index == 0 ? 0 : 1 << (index - 1);
    }

    /**
     * Get the enum index of the layer.
     *
     * @return The enum index of the layer.
     */
    public int getLayerIndex() {
        return layerIndex;
    }

    /**
     * Get the bit mask of the layer in 32-bit.
     *
     * @return The bit mask of the layer.
     */
    public int getUnderlyingValue() {
        return underlyingValue;
    }

    /**
     * Combines all layers provided to a layer mask.
     *
     * @param layers The layers to be included.
     * @return An integer represent the layers included in bit mask.
     */
    public static int combineLayerMask(Layer[] layers) {
        int layerMask = 0;
        for (var layer : layers) {
            layerMask |= layer.underlyingValue;
        }
        return layerMask;
    }

}
