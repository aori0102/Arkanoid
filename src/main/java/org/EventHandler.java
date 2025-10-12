package org;

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
    private final HashSet<BiConsumer<Object, T>> listeners = new HashSet<>();

    public EventHandler(Object sender) {
        this.sender = sender;
    }

    /**
     * Add the callback to the list. Corresponding to += in C#.
     *
     * @param listener : The action that we want to execute.
     */
    public void addListener(BiConsumer<Object, T> listener) {
        listeners.add(listener);
    }

    /**
     * Remove the callback from the list.Corresponding to -= in C#.
     *
     * @param listener : the action that we want to remove.
     */
    public void removeListener(BiConsumer<Object, T> listener) {
        listeners.remove(listener);
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

            HashSet<BiConsumer<Object, T>> newListeners = new HashSet<>(listeners);
            for (BiConsumer<Object, T> listener : newListeners) {
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

    public int listenersSize() {
        return listeners.size();
    }

}