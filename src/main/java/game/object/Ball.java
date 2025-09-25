package game.object;

import org.*;
import utils.Vector2;
import utils.Time;

public class Ball extends MonoBehaviour {

    public enum BallState {
        IDLE,
    }

    private Vector2 direction;
    private double ballSpeed = 500;
    private BoxCollider ballCollider;

    public Ball(GameObject owner)
    {
        super(owner);
    }

    public void awake()
    {
        ballCollider = getComponent(BoxCollider.class);
        ballCollider.setLocalCenter(new Vector2(0,0));
        ballCollider.setLocalSize(new Vector2(64, 64));
        ballCollider.setOnCollisionEnterCallback(e->{
            var normal = e.hitNormal.normalize().multiply(2.0);
            direction = normal.add(direction.normalize());

        });
    }

    public void update()
    {
        handleMovement();
    }

    @Override
    protected void destroyMono() {

    }

    public void handleMovement()
    {
        transform().translate(direction.normalize().multiply(ballSpeed * Time.deltaTime));

    }

    public void setDirection(Vector2 direction)
    {
        this.direction = direction;
    }

    public void setBallSpeed(double ballSpeed)
    {
        this.ballSpeed = ballSpeed;
    }

    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
