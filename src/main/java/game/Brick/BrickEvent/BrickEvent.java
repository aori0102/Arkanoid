package game.Brick.BrickEvent;

import game.Brick.Brick;
import game.Brick.BrickEvent.EventList.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all special events associated with a single brick within the game grid.
 *
 * <p>This class acts as an event handler or orchestrator for a specific brick
 * (identified by {@code row} and {@code col}). It holds a map of all possible
 * event types (e.g., {@link EventType#Bomb}, {@link EventType#Rocket}) and their
 * concrete implementations. It is responsible for registering, triggering, and
 * executing these events, which may affect other bricks in the grid.
 */
public class BrickEvent {
    /** The row index of the brick that owns this event manager. */
    private final int row;

    /** The column index of the brick that owns this event manager. */
    private final int col;

    /** A reference to the entire game grid, allowing events to affect other bricks. */
    private final List<List<Brick>> brickGrid;

    /** A map storing the concrete implementation for each event type. */
    private final Map<EventType, Event> eventMap = new EnumMap<>(EventType.class);

    /**
     * Constructs a new BrickEvent manager for a specific brick.
     *
     * @param rowT The row index of the owner brick.
     * @param colT The column index of the owner brick.
     * @param brickGridT A reference to the complete game grid.
     */
    public BrickEvent(int rowT, int colT, List<List<Brick>> brickGridT) {
        this.row = rowT;
        this.col = colT;
        this.brickGrid = brickGridT;
        registerDefaults();
    }

    /**
     * Initializes and registers the default event implementations.
     * This method populates the {@code eventMap} with concrete event handlers
     * (e.g., {@link BombEvent}, {@link WaveEvent}), passing the brick's
     * context to them.
     */
    private void registerDefaults() {
        eventMap.put(EventType.Wave, new WaveEvent(row, col, brickGrid));
        eventMap.put(EventType.Angel, new AngelEvent(row, col, brickGrid));
        eventMap.put(EventType.Bomb, new BombEvent(row, col, brickGrid));
        eventMap.put(EventType.Gift, new GiftEvent(row, col, brickGrid));
        eventMap.put(EventType.Rocket, new RocketEvent(row, col, brickGrid));
        eventMap.put(EventType.Wheel, new WheelEvent(row, col, brickGrid));
        eventMap.put(EventType.Rock, new RockEvent(row, col, brickGrid));
    }

    /**
     * Executes the main logic for all registered events.
     *
     * <p>This method is intended to be called repeatedly (e.g., once per game tick)
     * to process any active events. It has a special, hard-coded behavior where the
     * {@link EventType#Wave} event is always run first, followed by all
     * other registered events in an undefined order.
     */
    public void executeEvent() {
        // Run the Wave Event
        Event eventT = eventMap.get(EventType.Wave);
        if (eventT != null) {
            eventT.runEvent();
        }

        // Run all other events
        for (EventType eventType : EventType.values()) {
            if (eventType == EventType.Wave) {
                continue; // Skip the wave event as it has already run
            }

            Event event = eventMap.get(eventType);
            if (event != null) {
                event.runEvent();
            }
        }
    }

    /**
     * Triggers the "start" sequence for a specific event type.
     *
     * <p>This method is used to activate a specific event (e.g., to detonate a Bomb
     * or launch a Rocket) at a given location. This typically sets the
     * event's internal state to "active," and its logic will then be
     * processed during subsequent calls to {@link #executeEvent()}.
     *
     * @param eventType The type of event to start.
     * @param r The row where the event originates.
     * @param c The column where the event originates.
     */
    public void getStartEvent(EventType eventType, int r, int c) {
        Event event = eventMap.get(eventType);
        if (event != null) {
            event.getStartEvent(r, c);
        }
    }

    /**
     * Registers a custom or new event implementation for a given type,
     * potentially overriding the default.
     *
     * @param Type The event type to register (e.g., {@link EventType#Bomb}).
     * @param event The concrete {@link Event} implementation to map to that type.
     */
    public void register(EventType Type, Event event) {
        eventMap.put(Type, event);
    }
}