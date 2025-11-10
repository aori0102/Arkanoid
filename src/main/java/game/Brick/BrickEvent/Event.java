package game.Brick.BrickEvent;

public interface Event {
    double UPDATE_INTERVAL = 0.05; // 20 TimeExecute per seconds
    void runEvent();
    void getStartEvent(int r, int c);
}
