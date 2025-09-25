package game;

import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Vector2;

public class Init {

    public static void Init_Kine() {
        var paddle = GameObjectManager.instantiate("paddle");
        paddle.addComponent(Paddle.class);
        paddle.addComponent(PlayerInput.class);
        paddle.addComponent(ActionMap.class);
        paddle.addComponent(BoxCollider.class);
        paddle.transform.setGlobalScale(new Vector2(0.2, 0.2));
        paddle.transform.setGlobalPosition(new Vector2(600, 550));

        var paddleVisual = GameObjectManager.instantiate("paddleVisual");
        paddleVisual.transform.setParent(paddle.transform);
        paddleVisual.addComponent(SpriteRenderer.class).setImage("/paddle.png");

        var ball1 = GameObjectManager.instantiate("ball1");
        ball1.addComponent(Ball.class).setDirection(new Vector2(1, -1));
        ball1.addComponent(BoxCollider.class);
        ball1.transform.setGlobalPosition(new Vector2(300, 300));
        ball1.transform.setGlobalScale(new Vector2(0.2, 0.2));

        var ball1Visual = GameObjectManager.instantiate("ball1Visual");
        ball1Visual.transform.setParent(ball1.transform);
        ball1Visual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");
    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.transform.setLocalPosition(new Vector2(5.0, 0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.transform.setParent(borderLeft.transform);
        //borderLeftVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.transform.setLocalPosition(new Vector2(1190.0, 0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.transform.setParent(borderRight.transform);
        //borderRightVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.transform.setLocalPosition(new Vector2(0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.transform.setParent(borderTop.transform);
        //borderTopVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.transform.setLocalPosition(new Vector2(1190, 750));
        borderBottom.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.transform.setParent(borderBottom.transform);
        //borderBottomVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

    }

}
