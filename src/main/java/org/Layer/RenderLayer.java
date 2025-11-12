package org.Layer;

/**
 * Layers to control rendering order.
 */
public enum RenderLayer {

    None(0),
    Background(1),
    _2(2),
    _3(3),
    Default(4),
    _5(5),
    _6(6),
    _7(7),
    Foreground(8),
    _9(9),
    Overlay(10),
    UI_0(11),
    UI_1(12),
    UI_2(13),
    UI_3(14),
    UI_4(15),
    UI_5(16);

    private static final int MAX_LAYER = 16;
    private final int layerIndex;
    private final int underlyingValue;
    public static final int EVERYTHING = 0xFF;

    RenderLayer(int index) {
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
     * Get the bit mask of the layer in 8-bit.
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
    public static int combineLayerMask(RenderLayer[] layers) {
        int layerMask = 0;
        for (var layer : layers) {
            layerMask |= layer.underlyingValue;
        }
        return layerMask;
    }

}
