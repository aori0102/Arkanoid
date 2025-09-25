package org;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * EventHandler for the event system. This is built base on the EventHandler from Unity engine but for java.
 * @param <T> a variable type ( can be any like int, char, ... or any class).
 */
public class EventHandler<T> {

    /**
     * A list to store all of callbacks
     */
    private ArrayList<Consumer<T>> listeners = new ArrayList<>();

    /**
     * Add the callback to the list. Corresponding to += in C#.
     * @param listener : The action that we want to execute.
     */
    public void addListener(Consumer<T> listener) {
        listeners.add(listener);
    }

    /**
     * Remove the callback from the list.Corresponding to -= in C#.
     * @param listener : the action that we want to remove.
     */
    public void removeListener(Consumer<T> listener) {
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
     * @param arg : the data we want to pass into the action.
     */
    public void invoke(T arg) {
        if (!listeners.isEmpty()) {
            ArrayList<Consumer<T>> newListeners = new ArrayList<>(listeners);
            for (Consumer<T> listener : newListeners) {
                if(listener != null) {
                    listener.accept(arg);
                }
            }
        } else {
            System.out.println("There are no listeners to process!");
        }
    }

    /**
     * Check if there aren't any listeners.
     * @return true if the list is not empty.
     */
    public boolean isAnyListener() {
        return !listeners.isEmpty();
    }

}
