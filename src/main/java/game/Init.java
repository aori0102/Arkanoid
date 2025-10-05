package game;

import game.object.Ball;
import game.object.Paddle;
import org.*;
import utils.Vector2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Init {

    public static void Init_Kine() {
        var paddle = GameObjectManager.instantiate("paddle");
        paddle.addComponent(Paddle.class);
        paddle.addComponent(PlayerInput.class);
        paddle.addComponent(ActionMap.class);
        paddle.addComponent(BoxCollider.class);
        paddle.getTransform().setGlobalScale(new Vector2(1.25, 1.25));
        paddle.getTransform().setGlobalPosition(new Vector2(600, 550));

        var paddleVisual = GameObjectManager.instantiate("paddleVisual");
        paddleVisual.getTransform().setParent(paddle.getTransform());
        paddleVisual.getTransform().setLocalPosition(new Vector2(-47, 0));
        paddleVisual.addComponent(SpriteRenderer.class).setImage("/paddle.png");

        var ball = GameObjectManager.instantiate("ball");
        ball.addComponent(Ball.class);
        ball.addComponent(BoxCollider.class);
        ball.getComponent(Ball.class).setPaddle(paddle.getComponent(Paddle.class));
        ball.getTransform().setGlobalPosition(new Vector2(584,530 ));
        ball.getTransform().setGlobalScale(new Vector2(1.25, 1.25));

        var ballVisual = GameObjectManager.instantiate("ballVisual");
        ballVisual.getTransform().setParent(ball.getTransform());
        ballVisual.addComponent(SpriteRenderer.class).setImage("/ball.png");
    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

        var kita = GameObjectManager.instantiate("Kita");
        kita.addComponent(Kita.class);

        var borderLeft = GameObjectManager.instantiate("Border_Left");
        borderLeft.getTransform().setLocalPosition(new Vector2(5.0, 0));
        borderLeft.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderLeftVisual = GameObjectManager.instantiate();
        borderLeftVisual.getTransform().setParent(borderLeft.getTransform());
        //borderLeftVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderRight = GameObjectManager.instantiate("Border_Right");
        borderRight.getTransform().setLocalPosition(new Vector2(1190.0, 0));
        borderRight.addComponent(BoxCollider.class).setLocalSize(new Vector2(1.0, 20000.0));
        var borderRightVisual = GameObjectManager.instantiate();
        borderRightVisual.getTransform().setParent(borderRight.getTransform());
        //borderRightVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderTop = GameObjectManager.instantiate("Border_Top");
        borderTop.getTransform().setLocalPosition(new Vector2(0, 5.0));
        borderTop.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderTopVisual = GameObjectManager.instantiate();
        borderTopVisual.getTransform().setParent(borderTop.getTransform());
        //borderTopVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

        var borderBottom = GameObjectManager.instantiate("Border_Bottom");
        borderBottom.getTransform().setLocalPosition(new Vector2(1190, 750));
        borderBottom.addComponent(BoxCollider.class).setLocalSize(new Vector2(20000.0, 1.0));
        var borderBottomVisual = GameObjectManager.instantiate();
        borderBottomVisual.getTransform().setParent(borderBottom.getTransform());
        //borderBottomVisual.addComponent(SpriteRenderer.class).setImage("/bocchi.png");

    }

}
