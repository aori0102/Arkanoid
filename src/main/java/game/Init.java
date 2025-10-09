package game;

import org.GameObjectManager;
import org.UI.StartButton;

public class Init {

    public static void Init_Kine() {

    }

    public static void Init_Dui() {
        final String buttonKey = "ButtonKey";
        var menuButton = GameObjectManager.instantiate("menuButton");
        var button = menuButton.addComponent(StartButton.class);
//        menuButton.getTransform().setGlobalPosition(new Vector2(100, 100));
//        button.getSpriteAnimator().addAnimationClip(buttonKey);
//        button.getSpriteAnimator().setSprite(buttonKey,"/Frame 1.png");
//        button.getSpriteAnimator().addFrame(buttonKey, new Vector2(1,1),
//                new Vector2(418, 118),
//                new Vector2(418,118),
//                1);
//        button.getSpriteAnimator().setLoop(buttonKey, true);
//        button.getSpriteAnimator().playAnimation(buttonKey);

    }

    public static void Init_Duc() {

    }

    public static void Init_Aori() {

    }

}
