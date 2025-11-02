package game.Brick.BrickEvent;

import game.Brick.Brick;
import game.Brick.BrickEvent.EventList.*;
import game.Brick.BrickGenMap.MapStyle;
import game.Brick.BrickGenMap.StyleGenerator;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BrickEvent {
    private final int row;
    private final int col;
    private final List<List<Brick>> brickGrid;
    private final Map<EventType, Event> eventMap = new EnumMap<>(EventType.class);;

    public BrickEvent(int rowT, int colT, List<List<Brick>> brickGridT) {
        this.row = rowT;
        this.col = colT;
        this.brickGrid = brickGridT;
        registerDefaults();
    }

    private void registerDefaults() {
        eventMap.put(EventType.Wave, new WaveEvent(row, col, brickGrid));
        eventMap.put(EventType.ResetDamagedStatus, new ResetDamagedStatus(row, col, brickGrid));
        eventMap.put(EventType.Collision, new CollisionEvent(row, col, brickGrid));
    }

    public void executeEvent(EventType eventType) {
        Event event = eventMap.get(eventType);
        if (event == null)
            throw new IllegalArgumentException("EventType not registered: " + eventType);
        event.runEvent();
    }

    public void register(EventType Type, Event event) {
        eventMap.put(Type, event);
    }


}
