package game.Brick.BrickEvent;

import game.Brick.Brick;
import game.Brick.BrickEvent.EventList.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BrickEvent {
    private final int row;
    private final int col;
    private final List<List<Brick>> brickGrid;
    private final Map<EventType, Event> eventMap = new EnumMap<>(EventType.class);
    ;

    public BrickEvent(int rowT, int colT, List<List<Brick>> brickGridT) {
        this.row = rowT;
        this.col = colT;
        this.brickGrid = brickGridT;
        registerDefaults();
    }

    private void registerDefaults() {
        eventMap.put(EventType.Wave, new WaveEvent(row, col, brickGrid));
        eventMap.put(EventType.Angel, new AngelEvent(row, col, brickGrid));
        eventMap.put(EventType.Bomb, new BombEvent(row, col, brickGrid));
        eventMap.put(EventType.Gift, new GiftEvent(row, col, brickGrid));
        eventMap.put(EventType.Rocket, new RocketEvent(row, col, brickGrid));
        eventMap.put(EventType.Reborn, new RebornEvent(row, col, brickGrid));
        eventMap.put(EventType.Rock, new RockEvent(row, col, brickGrid));
    }

    public void executeEvent() {
        Event eventT = eventMap.get(EventType.Wave);
        eventT.runEvent();

        for (EventType eventType : EventType.values()) {
            Event event = eventMap.get(eventType);
            if (event != null && eventType != EventType.Wave) {
                event.runEvent();
            }
        }

    }

    public void getStartEvent(EventType eventType, int r, int c) {
        Event event = eventMap.get(eventType);
        if (event != null) {
            event.getStartEvent(r, c);
        }
    }

    public void register(EventType Type, Event event) {
        eventMap.put(Type, event);
    }
}
