package game.Brick;

import game.Brick.Brick;
import javafx.scene.effect.ColorAdjust;
import org.Rendering.SpriteRenderer;

public class BrickVisual {

    private static final int maxBrightnessWave = 4;

    public static double mapWaveToBrightness(int index, int maxWave) {
        index += 2;
        double minB = -0.2;
        double maxB =  0.2;
        return minB + (maxB - minB) * ((double) index / maxWave);
    }

    public static void setBrightness(int idx, Brick value) {
        double brightness = mapWaveToBrightness(idx, maxBrightnessWave);

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }
}
