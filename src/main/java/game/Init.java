package game;


import game.Brick.BrickObj;
import org.BoxCollider;
import org.CollisionData;
import org.GameObjectManager;
import utils.Vector2;

public class Init {

    public static void Init_Kine() {

        var object = GameObjectManager.instantiate();
        object.addComponent(Test.class);

    }

    public static void Init_Dui() {

    }

    public static void Init_Duc() {

        /*
        - Create brick object
         */

        // Create a new empty game object, this game object has a Transform.
        var brickObject = GameObjectManager.instantiate("Brick");

        // Add BrickObj component (handling brick behaviour) to brickObject.
        var brickComponent = brickObject.addComponent(BrickObj.class);

        // Add BoxCollider component (handling collision) to brickObject.
        var brickCollider = brickObject.addComponent(BoxCollider.class);

        /*
        Brick (Game object)
        -> Transform
        -> BoxCollider
        -> BrickObj
         */

        // Set collider size to a box of (50, 50)
        brickCollider.setLocalSize(new Vector2(50.0, 50.0));

        // Set onCollisionEnter() to be the function to handle after Brick is hit
        brickCollider.setOnCollisionEnterCallback(Init::onCollisionEnter);

        // Link onBrickDestroyed() to invoke when brick object is destroyed
        brickComponent.onBrickDestroyed.addListener(Init::onBrickDestroyed);

        // Move the brick up 10 unit (auto collision detection)
        brickObject.getTransform().translate(new Vector2(10, 0));

        /*
        - Add an event within BrickObj, invoke when destroyed.
        - Link that event with brick matrix -> update accordingly.
         */

    }

    private static void onBrickDestroyed(Object sender, Void e){

    }

    private static void onCollisionEnter(CollisionData data) {
        // Do whatever
    }

    public static void Init_Aori() {

    }

}
