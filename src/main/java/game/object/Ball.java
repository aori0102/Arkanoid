package game.object;

import org.*;
import utils.Vector2;
import utils.Time;

public class Ball extends MonoBehaviour {

    public enum BallState {
        IDLE,
        Moving
    }

    private Vector2 direction;
    private double ballSpeed = 500;
    private BoxCollider ballCollider;
    private Paddle paddle;

    private boolean isMoving = false;

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

        paddle.onMouseReleased.addListener((e)->{
            setDirection(e);
            isMoving = true;
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
        if (!isMoving) return;
        transform().translate(direction.normalize().multiply(ballSpeed * Time.deltaTime));

    }

    private void setDirection(Vector2 direction)
    {
        System.out.println(direction);
        this.direction = direction;
    }

    public void setBallSpeed(double ballSpeed)
    {
        this.ballSpeed = ballSpeed;
    }

    public void setPaddle(Paddle paddle)
    {
        this.paddle = paddle;
    }


    @Override
    protected MonoBehaviour clone(GameObject newOwner) {
        return null;
    }

    @Override
    protected void clear() {

    }
}
