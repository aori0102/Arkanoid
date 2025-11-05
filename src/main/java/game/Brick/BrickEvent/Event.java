package game.Brick.BrickEvent;

public interface Event {
    int NumFrameForEachRunTime = 7;
    void runEvent();
    void getStartEvent(int r, int c);
}
