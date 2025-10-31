package game.BrickObj;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import org.Physics.BoxCollider;
import org.Rendering.ImageAsset;
import org.Rendering.SpriteRenderer;
import utils.Vector2;

import static game.BrickObj.BrickEvent.WaveEffect.mapWaveToBrightness;
import static game.BrickObj.BrickManager.BRICK_SIZE;

public class Render {

    private static final int maxBrightnessWave = 4;

    public static Image getImage(BrickType brickType) {
        return switch (brickType) {
            case Normal -> ImageAsset.ImageIndex.brick_normal.getImage();
            case Steel -> ImageAsset.ImageIndex.brick_steel.getImage();
            case Diamond -> ImageAsset.ImageIndex.brick_diamond.getImage();
            case Rocket -> ImageAsset.ImageIndex.brick_rocket.getImage();
            case Rock -> ImageAsset.ImageIndex.brick_rock.getImage();
            case Bomb -> ImageAsset.ImageIndex.brick_bomb.getImage();
            case Gift -> ImageAsset.ImageIndex.brick_gift.getImage();
            case Reborn -> ImageAsset.ImageIndex.brick_reborn.getImage();
            case Angel -> ImageAsset.ImageIndex.brick_angel.getImage();
        };
    }

    public static void setRender(Brick value) {
        var brickRenderer = value.addComponent(SpriteRenderer.class);
        brickRenderer.setImage(Render.getImage(value.getBrickType()));
        brickRenderer.setSize(BRICK_SIZE);
        brickRenderer.setPivot(new Vector2(0.5, 0.5));
        value.addComponent(BoxCollider.class).setLocalSize(BRICK_SIZE);
    }



    public static void setBrightness(int idx, Brick value) {
        double brightness = mapWaveToBrightness(idx, maxBrightnessWave);

        SpriteRenderer renderer = value.getComponent(SpriteRenderer.class);
        ColorAdjust fx = new ColorAdjust();
        fx.setBrightness(brightness);
        renderer.getNode().setEffect(fx);
    }
}
