package org.Event;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.BiConsumer;

/**
 * EventHandler for the event system. This is built base on the EventHandler from C# but for java.
 *
 * @param <T> a variable type (can be any like int, char, ... or any class).
 */
public class EventHandler<T> {

    protected Object sender = null;

    /**
     * A list to store all of callbacks
     */
    private final HashMap<EventActionID, BiConsumer<Object, T>> listeners = new HashMap<>();

    public EventHandler(Object sender) {
        this.sender = sender;
    }

    /**
     * Add the callback to the list. Corresponding to += in C#.
     *
     * @param listener : The action that we want to execute.
     * @return An ID attach to this method. Can be used for removing later.
     */
    public EventActionID addListener(BiConsumer<Object, T> listener) {
        var id = new EventActionID();
        listeners.put(id, listener);
        return id;
    }

    /**
     * Remove the callback from the list.Corresponding to -= in C#.
     *
     * @param id The ID of the event action to be removed.
     */
    public void removeListener(EventActionID id) {
        listeners.remove(id);
    }

    /**
     * Remove all the listeners in the list.
     */
    public void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Invoke all events. Can be comprehended like activating all the assigned action.
     *
     * @param argument : the data we want to pass into the action.
     */
    public void invoke(Object sender, T argument) {

        if (sender != this.sender) {
            throw new IllegalArgumentException(
                    "This event can only be called within " + this.sender.getClass().getName() + "."
            );
        }

        if (!listeners.isEmpty()) {

            var newListenerSet = new HashSet<>(listeners.values());
            for (var listener : newListenerSet) {
                if (listener != null) {
                    listener.accept(sender, argument);
                }
            }

        } else {
            System.out.println("There are no listeners to process!");
        }

    }

    /**
     * Check if there aren't any listeners.
     *
     * @return {@code true} if the list is not empty.
     */
    public boolean isAnyListener() {
        return !listeners.isEmpty();
    }

}