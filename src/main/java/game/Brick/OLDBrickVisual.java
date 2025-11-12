package game.Brick;

import javafx.scene.effect.ColorAdjust;
import org.Rendering.SpriteRenderer;

/**
 * A utility class for managing the visual effects of {@link Brick} objects.
 * It provides static methods to apply color adjustments (brightness, hue, saturation)
 * to a brick's {@link SpriteRenderer}.
 */
public class OLDBrickVisual {

    /**
     * The maximum value used in the brightness wave calculation.
     */
    private static final int maxBrightnessWave = 4;

    /**
     * Maps a given index to a brightness value within a defined range.
     * This is used to create a "wave" or "pulse" effect for brightness.
     * The typical output range is [-0.25, 0.25].
     *
     * @param index    The current step or index in the wave.
     * @param maxWave  The maximum value for the wave's cycle (e.g., {@link #maxBrightnessWave}).
     * @return A calculated brightness value (double).
     */
    public static double mapWaveToBrightness(int index, int maxWave) {
        index += 2;
        double minB = -0.25;
        double maxB =  0.25;
        return minB + (maxB - minB) * ((double) index / maxWave);
    }

    /**
     * Sets the brightness of a brick based on a specific index in the brightness wave.
     *
     * @param idx   The index to use for the {@link #mapWaveToBrightness(int, int)} calculation.
     * @param value The {@link Brick} to modify.
     */
    public static void setBrightness(int idx, BrickVisual value) {
        double brightness = mapWaveToBrightness(idx, maxBrightnessWave);

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }

    /**
     * Sets the brightness of a brick to its maximum value (1.0).
     *
     * @param value The {@link Brick} to modify.
     */
    public static void setBrightnessMax(BrickVisual value) {
        double brightness = 1;

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }

    /**
     * Applies a color adjustment to the brick to make it appear red.
     * This is achieved by increasing saturation, shifting hue, and slightly decreasing brightness.
     *
     * @param value The {@link Brick} to modify.
     */
    public static void setRed(Brick value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(1.0);
        fx.setHue(-0.9);
        fx.setBrightness(-0.2);
        renderer.getNode().setEffect(fx);
    }

    /**
     * Applies a color adjustment to the brick to make it appear yellow.
     * This is achieved by adjusting saturation, shifting hue, and slightly decreasing brightness.
     *
     * @param value The {@link Brick} to modify.
     */
    public static void setYellow(BrickVisual value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(0.15);
        fx.setHue(1.0);
        fx.setBrightness(-0.2);
        renderer.getNode().setEffect(fx);
    }

    /**
     * Resets all color adjustments (Saturation, Hue, Brightness) on the brick's renderer
     * to their default (0.0) values, removing any visual effects.
     *
     * @param value The {@link Brick} to reset.
     */
    public static void resetRender(BrickVisual value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(0.0);
        fx.setHue(0.0);
        fx.setBrightness(0.0);
        renderer.getNode().setEffect(fx);
    }
}