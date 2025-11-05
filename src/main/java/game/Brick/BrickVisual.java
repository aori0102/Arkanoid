package game.Brick;

import javafx.scene.effect.ColorAdjust;
import org.Rendering.SpriteRenderer;

public class BrickVisual {

    private static final int maxBrightnessWave = 4;

    public static double mapWaveToBrightness(int index, int maxWave) {
        index += 2;
        double minB = -0.25;
        double maxB =  0.25;
        return minB + (maxB - minB) * ((double) index / maxWave);
    }

    public static void setBrightness(int idx, Brick value) {
        double brightness = mapWaveToBrightness(idx, maxBrightnessWave);

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }

    public static void setBrightnessMax(Brick value) {
        double brightness = 0.8;

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }

    public static void setRed(Brick value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(1.0);
        fx.setHue(-0.9);
        fx.setBrightness(-0.2);
        renderer.getNode().setEffect(fx);
    }

    public static void setYellow(Brick value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(0.15);
        fx.setHue(1.0);
        fx.setBrightness(-0.2);
        renderer.getNode().setEffect(fx);
    }




    public static void resetRender(Brick value) {
        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setSaturation(0.0);
        fx.setHue(0.0);
        fx.setBrightness(0.0);
        renderer.getNode().setEffect(fx);
    }
}
