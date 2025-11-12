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
            case Normal -> ImageAsset.ImageIndex.BrickNormal.getImage();
            case Steel -> ImageAsset.ImageIndex.BrickSteel.getImage();
            case Diamond -> ImageAsset.ImageIndex.BrickCrazyDiamond.getImage();
            case Rocket -> ImageAsset.ImageIndex.BrickRocket.getImage();
            case Rock -> ImageAsset.ImageIndex.BrickRock.getImage();
            case Bomb -> ImageAsset.ImageIndex.BrickBomb.getImage();
            case Gift -> ImageAsset.ImageIndex.BrickGift.getImage();
            case Reborn -> ImageAsset.ImageIndex.BrickReborn.getImage();
            case Angel -> ImageAsset.ImageIndex.BrickAngel.getImage();
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
